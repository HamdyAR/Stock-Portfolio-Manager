package com.cbfacademy.stockportfoliomanager.order.dto;

import com.cbfacademy.stockportfoliomanager.order.OrderSide;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String stockSymbol,
        OrderSide side,
        int volume,
        BigDecimal executedPrice,
        LocalDateTime timestamp
) {}
