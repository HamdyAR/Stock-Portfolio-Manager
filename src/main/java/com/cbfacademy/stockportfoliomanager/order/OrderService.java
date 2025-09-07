package com.cbfacademy.stockportfoliomanager.order;

import com.cbfacademy.stockportfoliomanager.stock.Stock;
import com.cbfacademy.stockportfoliomanager.stock.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final StockService stockService;
    
    public OrderService(OrderRepository orderRepository, StockService stockService) {
        this.orderRepository = orderRepository;
        this.stockService = stockService;
    }
    
    public Order placeOrder(String stockSymbol, OrderSide side, Integer volume, BigDecimal price) 
            throws IllegalArgumentException {
        
        // Validate input parameters
        if (stockSymbol == null || stockSymbol.trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Stock symbol cannot be null or empty"
            );
        }
        
        if (side == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Order side cannot be null"
            );
        }
        
        if (volume == null || volume <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Volume must be positive"
            );
        }
        
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Price must be positive"
            );
        }
        
        // Verify stock exists
        Stock stock = stockService.getStockBySymbol(stockSymbol);
        
        // For sell orders, check if user has sufficient holdings
        if (side == OrderSide.SELL) {
            Integer currentHoldings = getCurrentHoldingsForStock(stockSymbol);
            if (currentHoldings < volume) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Insufficient holdings. Current holdings: " + currentHoldings + ", attempted to sell: " + volume
                );
            }
        }
        
        // Create and save the order
        Order order = new Order(stock, side, volume, price);
        return orderRepository.save(order);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByTimestampDesc();
    }
    
    public Order getOrderById(UUID id) throws NoSuchElementException {
        return orderRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Order not found with id: " + id
            ));
    }
    
    public List<Order> getOrdersByStock(String stockSymbol) {
        return orderRepository.findByStockSymbol(stockSymbol);
    }
    
    public List<Order> getOrdersBySide(OrderSide side) {
        return orderRepository.findBySide(side);
    }
    
    // Portfolio calculation methods
    public List<PortfolioItem> getCurrentPortfolio() {
        List<Object[]> results = orderRepository.calculatePortfolioHoldings();
        List<PortfolioItem> portfolio = new ArrayList<>();
        
        for (Object[] result : results) {
            String symbol = (String) result[0];
            String company = (String) result[1];
            String exchange = (String) result[2];
            String industry = (String) result[3];
            Long holdings = (Long) result[4];
            
            portfolio.add(new PortfolioItem(symbol, company, exchange, industry, holdings.intValue()));
        }
        
        return portfolio;
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
}
