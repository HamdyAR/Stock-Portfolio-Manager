package com.cbfacademy.stockportfoliomanager.order.dto;

import java.math.BigDecimal;

public record PortfolioItemResponse(
    String stockSymbol,
        String companyName,
        String exchange,
        String industry,
        Integer quantity,
        BigDecimal currentPrice,
        BigDecimal marketValue
) {}
