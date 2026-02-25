package com.cbfacademy.stockportfoliomanager.stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record CreateStockRequest(
    @NotBlank(message = "Symbol is required")
    @Size(max = 10, message = "Symbol must be at most 10 characters")
    @Pattern(regexp = "^[A-Za-z]{1,10}$", message = "Symbol must contain only letters")
    String symbol,

    @NotBlank(message = "Company name is required")
    String companyName,

    @NotBlank(message = "Exchange is required")
    String exchange,

    @NotBlank(message = "Industry is required")
    String industry
) {

}
