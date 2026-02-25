package com.cbfacademy.stockportfoliomanager.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.cbfacademy.stockportfoliomanager.order.OrderSide;

public record CreateOrderResponse(
        UUID id,
        String stockSymbol,
        OrderSide side,
        Integer volume,
        BigDecimal executedPrice,
        LocalDateTime timestamp
) {}
