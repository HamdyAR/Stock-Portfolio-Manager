package com.cbfacademy.stockportfoliomanager.order;

public class PortfolioItem {
    
    private String symbol;
    private String company;
    private String exchange;
    private String industry;
    private Integer holdings;
    
    // Default constructor
    public PortfolioItem() {}
    
    // Constructor with all fields
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