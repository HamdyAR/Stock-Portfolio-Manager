package com.cbfacademy.stockportfoliomanager.stock;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stocks")
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


    // Default constructor
    public Stock() {}

    // Constructor for creating new stocks
    public Stock(String symbol, String companyName, String exchange, String industry) {
        this.symbol = symbol != null ? symbol.toUpperCase() : null; //converts stock symbol to uppercase
        this.companyName = companyName;
        this.exchange = exchange;
        this.industry = industry;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getExchange() {
        return exchange;
    }

    public String getIndustry() {
        return industry;
    }

    // Setters
    public void setSymbol(String symbol) {
        this.symbol = symbol != null ? symbol.toUpperCase() : null;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }



    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", exchange='" + exchange + '\'' +
                ", industry='" + industry + '\'' +
                '}';
    }

}
