package com.cbfacademy.stockportfoliomanager.stock.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateStockRequest(
    @NotBlank(message = "Company name is required")
    String companyName,

    @NotBlank(message = "Exchange is required")
    String exchange,

    @NotBlank(message = "Industry is required")
    String industry
) {

}
