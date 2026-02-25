package com.cbfacademy.stockportfoliomanager.stock;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(unique = true, nullable = false, length = 10)
    private String symbol;
    
    @Column(name = "company_name", nullable = false)
    private String companyName;
    
    @Column(nullable = false)
    private String exchange;
    
    @Column(nullable = false)
    private String industry;


    // This runs automatically before an object is saved(Persist) or updated in the database
    @PrePersist
    @PreUpdate
    public void formatData() {
        if (this.symbol != null) {
            this.symbol = this.symbol.toUpperCase().trim();
        }
    }

   

}
