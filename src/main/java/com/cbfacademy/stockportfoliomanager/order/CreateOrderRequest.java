package com.cbfacademy.stockportfoliomanager.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(
    description = "Request payload for creating a new stock order",
    example = """
    {
        "stockSymbol": "AAPL",
        "side": "BUY",
        "volume": 100,
        "price": 150.50
    }
    """
)
public class CreateOrderRequest {
    
    @Schema(
        description = "Stock symbol to trade (ticker symbol)",
        example = "AAPL",
        required = true,
        minLength = 1,
        maxLength = 10
    )
    @NotBlank(message = "Stock symbol is required")
    private String stockSymbol;
    
    @Schema(
        description = "Order side indicating buy or sell transaction",
        example = "BUY",
        allowableValues = {"BUY", "SELL"},
        required = true
    )
    @NotNull(message = "Order side is required")
    private OrderSide side;
    
    @Schema(
        description = "Number of shares to trade",
        example = "100",
        minimum = "1",
        required = true
    )
    @NotNull(message = "Volume is required")
    @Positive(message = "Volume must be positive")
    private Integer volume;
    
    @Schema(
        description = "Price per share in USD",
        example = "150.50",
        minimum = "0.01",
        required = true,
        type = "number",
        format = "decimal"
    )
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    public CreateOrderRequest() {}
    
    public CreateOrderRequest(String stockSymbol, OrderSide side, Integer volume, BigDecimal price) {
        this.stockSymbol = stockSymbol;
        this.side = side;
        this.volume = volume;
        this.price = price;
    }
    
    // Getters
    @Schema(description = "Get the stock symbol")
    public String getStockSymbol() {
        return stockSymbol;
    }
    
    @Schema(description = "Get the order side")
    public OrderSide getSide() {
        return side;
    }
    
    @Schema(description = "Get the volume of shares")
    public Integer getVolume() {
        return volume;
    }
    
    @Schema(description = "Get the price per share")
    public BigDecimal getPrice() {
        return price;
    }
    
    // Setters
    @Schema(description = "Set the stock symbol")
    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }
    
    @Schema(description = "Set the order side")
    public void setSide(OrderSide side) {
        this.side = side;
    }
    
    @Schema(description = "Set the volume of shares")
    public void setVolume(Integer volume) {
        this.volume = volume;
    }
    
    @Schema(description = "Set the price per share")
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}