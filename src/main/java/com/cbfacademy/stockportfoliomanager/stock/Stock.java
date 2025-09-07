package com.cbfacademy.stockportfoliomanager.stock;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stocks")
@Schema(
    description = "Stock entity representing a tradeable security on the stock market",
    example = """
    {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "symbol": "AAPL",
        "companyName": "Apple Inc.",
        "exchange": "NASDAQ",
        "industry": "Technology"
    }
    """
)
public class Stock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(
        description = "Unique identifier for the stock",
        example = "123e4567-e89b-12d3-a456-426614174000",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private UUID id;
    
    @Column(unique = true, nullable = false, length = 10)
    @Schema(
        description = "Stock ticker symbol (automatically converted to uppercase)",
        example = "AAPL",
        required = true,
        minLength = 1,
        maxLength = 10,
        pattern = "^[A-Z]{1,10}$"
    )
    private String symbol;
    
    @Column(name = "company_name", nullable = false)
    @Schema(
        description = "Full company name",
        example = "Apple Inc.",
        required = true,
        maxLength = 255
    )
    private String companyName;
    
    @Column(nullable = false)
    @Schema(
        description = "Stock exchange where the stock is traded",
        example = "NASDAQ",
        allowableValues = {"NYSE", "NASDAQ", "AMEX", "LSE", "TSE"},
        required = true
    )
    private String exchange;
    
    @Column(nullable = false)
    @Schema(
        description = "Industry sector the company operates in",
        example = "Technology",
        required = true
    )
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

    @Schema(hidden = true)
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