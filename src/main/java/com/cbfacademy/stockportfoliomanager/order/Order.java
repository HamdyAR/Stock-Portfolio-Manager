package com.cbfacademy.stockportfoliomanager.order;

import com.cbfacademy.stockportfoliomanager.stock.Stock;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Schema(
    description = "Order entity representing a buy or sell transaction",
    example = """
    {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "stock": {
            "id": 1,
            "symbol": "AAPL",
            "name": "Apple Inc."
        },
        "side": "BUY",
        "volume": 100,
        "price": 150.50,
        "timestamp": "2025-01-15T10:30:00"
    }
    """
)
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(
        description = "Unique identifier for the order",
        example = "123e4567-e89b-12d3-a456-426614174000",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private UUID id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_id", nullable = false)
    @Schema(
        description = "Stock being traded in this order",
        required = true,
        implementation = Stock.class
    )
    private Stock stock;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(
        description = "Order side indicating buy or sell transaction",
        example = "BUY",
        allowableValues = {"BUY", "SELL"},
        required = true
    )
    private OrderSide side;
    
    @Column(nullable = false)
    @Schema(
        description = "Number of shares to trade",
        example = "100",
        minimum = "1",
        required = true
    )
    private Integer volume;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(
        description = "Price per share in USD",
        example = "150.50",
        minimum = "0.01",
        required = true,
        type = "number",
        format = "decimal"
    )
    private BigDecimal price;
    
    @Column(name = "order_timestamp", nullable = false)
    @Schema(
        description = "Timestamp when the order was created",
        example = "2025-01-15T10:30:00",
        type = "string",
        format = "date-time",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    public Order() {
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor for creating new orders
    public Order(Stock stock, OrderSide side, Integer volume, BigDecimal price) {
        this();
        this.stock = stock;
        this.side = side;
        this.volume = volume;
        this.price = price;
    }
    
    // Getters
    @Schema(description = "Get the unique order identifier")
    public UUID getId() {
        return id;
    }
    
    @Schema(description = "Get the stock associated with this order")
    public Stock getStock() {
        return stock;
    }
    
    @Schema(description = "Get the order side (BUY or SELL)")
    public OrderSide getSide() {
        return side;
    }
    
    @Schema(description = "Get the number of shares")
    public Integer getVolume() {
        return volume;
    }
    
    @Schema(description = "Get the price per share")
    public BigDecimal getPrice() {
        return price;
    }
    
    @Schema(description = "Get the order creation timestamp")
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    // Setters
    @Schema(description = "Set the stock for this order")
    public void setStock(Stock stock) {
        this.stock = stock;
    }
    
    @Schema(description = "Set the order side")
    public void setSide(OrderSide side) {
        this.side = side;
    }
    
    @Schema(description = "Set the number of shares")
    public void setVolume(Integer volume) {
        this.volume = volume;
    }
    
    @Schema(description = "Set the price per share")
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    @Schema(hidden = true)
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", stock=" + (stock != null ? stock.getSymbol() : "null") +
                ", side=" + side +
                ", volume=" + volume +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }
}