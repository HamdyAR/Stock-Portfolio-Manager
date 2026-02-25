package com.cbfacademy.stockportfoliomanager.order.dto;

import com.cbfacademy.stockportfoliomanager.order.OrderSide;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
    @NotBlank String stockSymbol,
    @NotNull OrderSide side,
    @NotNull @Positive Integer volume
) {}
