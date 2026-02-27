package com.cbfacademy.stockportfoliomanager.order.dto;

import java.math.BigDecimal;

public record PortfolioItemResponse(
        String stockSymbol,
        String companyName,
        int quantity,
        BigDecimal currentPrice,
        BigDecimal marketValue
) {}
