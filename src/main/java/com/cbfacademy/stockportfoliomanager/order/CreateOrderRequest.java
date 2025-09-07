package com.cbfacademy.stockportfoliomanager.order;
import java.math.BigDecimal;

class CreateOrderRequest {
    private String stockSymbol;
    private OrderSide side;
    private Integer volume;
    private BigDecimal price;
    
    // Default constructor
    public CreateOrderRequest() {}
    
    // Constructor
    public CreateOrderRequest(String stockSymbol, OrderSide side, Integer volume, BigDecimal price) {
        this.stockSymbol = stockSymbol;
        this.side = side;
        this.volume = volume;
        this.price = price;
    }
    
    // Getters
    public String getStockSymbol() {
        return stockSymbol;
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
    
    // Setters
    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
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
}