package com.cbfacademy.stockportfoliomanager.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cbfacademy.stockportfoliomanager.order.dto.CreateOrderRequest;
import com.cbfacademy.stockportfoliomanager.order.dto.OrderResponse;
import com.cbfacademy.stockportfoliomanager.order.dto.PortfolioItemResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "APIs for managing stock orders and portfolio")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @Operation(
        summary = "Place a new stock order",
        description = "Creates and places a new buy or sell order for a stock"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Order placed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid order request",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Parameter(description = "Order creation request containing stock symbol, side, volume, and price", required = true)
            @Valid @RequestBody CreateOrderRequest request) {
        OrderResponse order = orderService.placeOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @Operation(
        summary = "Retrieve orders",
        description = "Get all orders with optional filtering by order side (BUY/SELL) or stock symbol"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Orders retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @Parameter(description = "Filter orders by side (BUY or SELL)", example = "BUY")
            @RequestParam(required = false) OrderSide side,
            @Parameter(description = "Filter orders by stock symbol", example = "AAPL")
            @RequestParam(required = false) String symbol) {
        
        List<OrderResponse> orders;
        
        if (side != null) {
            orders = orderService.getOrdersBySide(side);
        } else if (symbol != null && !symbol.isBlank()) {
            orders = orderService.getOrdersByStock(symbol);
        } else {
            orders = orderService.getAllOrders();
        }
        
        return ResponseEntity.ok(orders);
    }
    
    @Operation(
        summary = "Get order by ID",
        description = "Retrieve a specific order by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Order found and retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Order not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid UUID format",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @Parameter(description = "Unique identifier of the order", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    @Operation(
        summary = "Get current portfolio",
        description = "Retrieve the current portfolio showing all stock positions"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Portfolio retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PortfolioItem.class))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error",
            content = @Content
        )
    })
    @GetMapping("/portfolio")
    public ResponseEntity<List<PortfolioItemResponse>> getCurrentPortfolio() {
        List<PortfolioItemResponse> portfolio = orderService.getCurrentPortfolio();
        return ResponseEntity.ok(portfolio);
    }
}