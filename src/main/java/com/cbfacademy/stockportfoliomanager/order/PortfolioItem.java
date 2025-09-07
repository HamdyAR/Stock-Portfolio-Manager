package com.cbfacademy.stockportfoliomanager.order;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Portfolio item representing a stock position in the user's portfolio",
    example = """
    {
        "symbol": "AAPL",
        "company": "Apple Inc.",
        "exchange": "NASDAQ",
        "industry": "Technology",
        "holdings": 250
    }
    """
)
public class PortfolioItem {
    
    @Schema(
        description = "Stock ticker symbol",
        example = "AAPL",
        required = true,
        minLength = 1,
        maxLength = 10
    )
    private String symbol;
    
    @Schema(
        description = "Company name",
        example = "Apple Inc.",
        required = true
    )
    private String company;
    
    @Schema(
        description = "Stock exchange where the stock is traded",
        example = "NASDAQ",
        allowableValues = {"NYSE", "NASDAQ", "AMEX", "LSE", "TSE"},
        required = true
    )
    private String exchange;
    
    @Schema(
        description = "Industry sector the company operates in",
        example = "Technology",
        required = true
    )
    private String industry;
    
    @Schema(
        description = "Total number of shares owned in the portfolio",
        example = "250",
        minimum = "0"
    )
    private Integer holdings;
    
    public PortfolioItem() {}
    
    
    public PortfolioItem(String symbol, String company, String exchange, String industry, Integer holdings) {
        this.symbol = symbol;
        this.company = company;
        this.exchange = exchange;
        this.industry = industry;
        this.holdings = holdings;
    }
    
    // Getters
    public String getSymbol() {
        return symbol;
    }
    
    public String getCompany() {
        return company;
    }
    
    public String getExchange() {
        return exchange;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public Integer getHoldings() {
        return holdings;
    }
    
    // Setters
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public void setCompany(String company) {
        this.company = company;
    }
    
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public void setHoldings(Integer holdings) {
        this.holdings = holdings;
    }
    
    @Schema(
        description = "Check if this portfolio item has any holdings",
        example = "true",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    public boolean hasHoldings() {
        return holdings != null && holdings > 0;
    }
    
    @Schema(hidden = true)
    @Override
    public String toString() {
        return "PortfolioItem{" +
                "symbol='" + symbol + '\'' +
                ", company='" + company + '\'' +
                ", exchange='" + exchange + '\'' +
                ", industry='" + industry + '\'' +
                ", holdings=" + holdings +
                '}';
    }
}