package com.cbfacademy.stockportfoliomanager.order;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Order side enumeration indicating the direction of the trade",
    type = "string",
    allowableValues = {"BUY", "SELL"},
    example = "BUY"
)
public enum OrderSide {
    
    @Schema(description = "Buy order - purchasing shares")
    BUY,
    
    @Schema(description = "Sell order - selling shares")
    SELL
}