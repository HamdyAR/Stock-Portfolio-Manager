package com.cbfacademy.stockportfoliomanager.order;

import com.cbfacademy.stockportfoliomanager.stock.Stock;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSide side;
    
    @Column(nullable = false)
    private Integer volume;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "order_timestamp", nullable = false)
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
    public UUID getId() {
        return id;
    }
    
    public Stock getStock() {
        return stock;
    }
    
    public OrderSide getSide() {
        return side;
    }
    
    public Integer getVolume() {
        return volume;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    // Setters
    public void setStock(Stock stock) {
        this.stock = stock;
    }
    
    public void setSide(OrderSide side) {
        this.side = side;
    }
    
    public void setVolume(Integer volume) {
        this.volume = volume;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    
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
