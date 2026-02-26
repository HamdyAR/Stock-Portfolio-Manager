package com.cbfacademy.stockportfoliomanager.order.dto;

import com.cbfacademy.stockportfoliomanager.order.OrderSide;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
        @NotBlank(message = "Stock symbol is required")
        String stockSymbol,

        @NotNull(message = "Order side is required")
        OrderSide side,

        @NotNull(message = "Volume is required")
        @Positive(message = "Volume must be greater than zero")
        int volume
) {}
