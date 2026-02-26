package com.cbfacademy.stockportfoliomanager.order;

import com.cbfacademy.stockportfoliomanager.stock.Stock;
import com.cbfacademy.stockportfoliomanager.stock.StockService;
import com.cbfacademy.stockportfoliomanager.stock.dto.StockResponse;
import com.cbfacademy.stockportfoliomanager.exceptions.InvalidOrderException;
import com.cbfacademy.stockportfoliomanager.exceptions.InsufficientHoldingsException;
import com.cbfacademy.stockportfoliomanager.exceptions.ResourceNotFoundException;
import com.cbfacademy.stockportfoliomanager.order.dto.CreateOrderRequest;
import com.cbfacademy.stockportfoliomanager.order.dto.OrderResponse;
import com.cbfacademy.stockportfoliomanager.order.dto.PortfolioItemResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final StockService stockService;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    
    public OrderService(OrderRepository orderRepository, StockService stockService) {
        this.orderRepository = orderRepository;
        this.stockService = stockService;
    }
    
    public OrderResponse placeOrder(CreateOrderRequest request) {

        String symbol = request.stockSymbol().trim().toUpperCase();

        Stock stock = stockService.getStockEntityBySymbol(symbol)
            .orElseThrow(() -> new ResourceNotFoundException("Stock not found with symbol: " + symbol));
        
        
        // For sell orders, check if user has sufficient holdings
        if (request.side() == OrderSide.SELL) {
            int currentHoldings = getCurrentHoldingsForStock(symbol);
            if (currentHoldings < request.volume()) {
                throw new InsufficientHoldingsException(symbol, request.volume(), currentHoldings);
            }
        }
        
        // Create and save the order
        BigDecimal executedPrice = BigDecimal.ZERO;

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
        return orderRepository.findByStockSymbol(symbol).stream()
                .map(this::toResponse)
                .toList();
    }
    public List<OrderResponse> getOrdersBySide(OrderSide side) {
        return orderRepository.findBySide(side).stream()
                .map(this::toResponse)
                .toList();
    }
    
    // Portfolio calculation methods
    public List<PortfolioItemResponse> getCurrentPortfolio() {
    List<Object[]> results = orderRepository.calculatePortfolioHoldings();
    
    return results.stream()
            .map(result -> {
                // For now, we set price and market value to ZERO 
                // until we implement the Alpha Vantage Client
                BigDecimal mockPrice = BigDecimal.ZERO; 
                BigDecimal mockMarketValue = BigDecimal.ZERO;

                return new PortfolioItemResponse(
                    (String) result[0], // symbol
                    (String) result[1], // companyName
                    (String) result[2], // exchange
                    (String) result[3], // industry
                    ((Long) result[4]).intValue(), // quantity
                    mockPrice,
                    mockMarketValue
                );
            })
            .toList();
}
    
    private Integer getCurrentHoldingsForStock(String stockSymbol) {
        List<Order> orders = orderRepository.findByStockSymbol(stockSymbol);
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