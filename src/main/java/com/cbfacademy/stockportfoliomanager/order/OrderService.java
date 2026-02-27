package com.cbfacademy.stockportfoliomanager.order;

import com.cbfacademy.stockportfoliomanager.stock.Stock;
import com.cbfacademy.stockportfoliomanager.stock.StockService;
import com.cbfacademy.stockportfoliomanager.exceptions.InvalidOrderException;
import com.cbfacademy.stockportfoliomanager.client.AlphaVantageClient;
import com.cbfacademy.stockportfoliomanager.exceptions.InsufficientHoldingsException;
import com.cbfacademy.stockportfoliomanager.exceptions.ResourceNotFoundException;
import com.cbfacademy.stockportfoliomanager.order.dto.CreateOrderRequest;
import com.cbfacademy.stockportfoliomanager.order.dto.OrderResponse;
import com.cbfacademy.stockportfoliomanager.order.dto.PortfolioItemResponse;
import com.cbfacademy.stockportfoliomanager.order.dto.PortfolioResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final StockService stockService;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final AlphaVantageClient alphaVantageClient;
    
    public OrderService(OrderRepository orderRepository, StockService stockService, AlphaVantageClient alphaVantageClient) {
        this.orderRepository = orderRepository;
        this.stockService = stockService;
        this.alphaVantageClient = alphaVantageClient;
    }
    
    public OrderResponse placeOrder(CreateOrderRequest request) {
    String symbol = request.stockSymbol().trim().toUpperCase();

    Stock stock = stockService.getStockEntityBySymbol(symbol)
        .orElseThrow(() -> new ResourceNotFoundException("Stock not found: " + symbol));
    
    if (request.side() == OrderSide.SELL) {
        int currentHoldings = getCurrentHoldingsForStock(symbol);
        if (currentHoldings < request.volume()) {
            throw new InsufficientHoldingsException(symbol, request.volume(), currentHoldings);
        }
    }
    
    BigDecimal executedPrice = alphaVantageClient.getStockPrice(symbol); 

    if(executedPrice.compareTo(BigDecimal.ZERO) <= 0){
        throw new InvalidOrderException("Could not retrieve market price for" + symbol);
    }

    Order order = Order.builder()
            .stock(stock)
            .side(request.side())
            .volume(request.volume())
            .price(executedPrice) 
            .build();

    Order saved = orderRepository.save(order);
    return toResponse(saved);
}
    
    public List<OrderResponse> getAllOrders() {
        logger.info("Retrieving all orders");
        return orderRepository.findAllByOrderByTimestampDesc().stream()
                .map(this::toResponse)
                .toList();
    }
    
    public OrderResponse getOrderById(UUID id) {
         Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return toResponse(order);
    }
    
    public List<OrderResponse> getOrdersByStock(String symbol) {
        return orderRepository.findByStock_Symbol(symbol).stream()
                .map(this::toResponse)
                .toList();
    }
    public List<OrderResponse> getOrdersBySide(OrderSide side) {
        return orderRepository.findBySide(side).stream()
                .map(this::toResponse)
                .toList();
    }
    
    // Portfolio calculation methods
   public PortfolioResponse getCurrentPortfolio() {
    List<Object[]> holdings = orderRepository.calculatePortfolioHoldings();

    List<PortfolioItemResponse> items = holdings.stream().map(row -> {
        String symbol = (String) row[0];
        String name = (String) row[1];
        // Cast quantity to int to match your record
        int quantity = (row[4] instanceof Number n) ? n.intValue() : 0;

        BigDecimal currentPrice = alphaVantageClient.getStockPrice(symbol);
        BigDecimal marketValue = currentPrice.multiply(BigDecimal.valueOf(quantity));

        return new PortfolioItemResponse(symbol, name, quantity, currentPrice, marketValue);
    }).toList();

    BigDecimal totalPortfolioValue = items.stream()
            .map(PortfolioItemResponse::marketValue) // Points to your record's field
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new PortfolioResponse(items, totalPortfolioValue);
   }
    
    private Integer getCurrentHoldingsForStock(String stockSymbol) {
        List<Order> orders = orderRepository.findByStock_Symbol(stockSymbol);
        int holdings = 0;
        
        for (Order order : orders) {
            if (order.getSide() == OrderSide.BUY) {
                holdings += order.getVolume();
            } else {
                holdings -= order.getVolume();
            }
        }
        
        return holdings;
    }

    private OrderResponse toResponse(Order order) {
    return new OrderResponse(
            order.getId(),
            order.getStock().getSymbol(), 
            order.getSide(),
            order.getVolume(),
            order.getPrice(),
            order.getTimestamp()
    );
}
}