package com.cbfacademy.stockportfoliomanager.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cbfacademy.stockportfoliomanager.order.dto.CreateOrderRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    // Place a new order
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.placeOrder(
            request.getStockSymbol(),
            request.getSide(),
            request.getVolume(),
            request.getPrice()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    // Get all orders with optional filtering
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(required = false) OrderSide side,
            @RequestParam(required = false) String symbol) {
        
        List<Order> orders;
        
        if (side != null) {
            orders = orderService.getOrdersBySide(side);
        } else if (symbol != null) {
            orders = orderService.getOrdersByStock(symbol);
        } else {
            orders = orderService.getAllOrders();
        }
        
        return ResponseEntity.ok(orders);
    }
    
    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    // Get current portfolio
    @GetMapping("/portfolio")
    public ResponseEntity<List<PortfolioItem>> getCurrentPortfolio() {
        List<PortfolioItem> portfolio = orderService.getCurrentPortfolio();
        return ResponseEntity.ok(portfolio);
    }
}

