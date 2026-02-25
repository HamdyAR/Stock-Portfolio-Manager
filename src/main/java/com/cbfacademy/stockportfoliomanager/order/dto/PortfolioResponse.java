package com.cbfacademy.stockportfoliomanager.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record PortfolioResponse(
    List<PortfolioItemResponse> items,
    BigDecimal totalPortfolioValue
) {}
