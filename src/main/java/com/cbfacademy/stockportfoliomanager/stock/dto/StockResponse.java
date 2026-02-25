package com.cbfacademy.stockportfoliomanager.stock.dto;

import java.util.UUID;

public record StockResponse(
    UUID id,
    String symbol,
    String companyName,
    String exchange,
    String industry
) {}
