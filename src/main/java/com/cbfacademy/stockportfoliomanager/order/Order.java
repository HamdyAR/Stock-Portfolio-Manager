package com.cbfacademy.stockportfoliomanager.order;

import com.cbfacademy.stockportfoliomanager.stock.Stock;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude= "stock")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSide side;
    
    @Column(nullable = false)
    private Integer volume;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "order_timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;
    

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

    public String getStockSymbol() {
        return stock != null ? stock.getSymbol() : "N/A";
    }
    
    
}
