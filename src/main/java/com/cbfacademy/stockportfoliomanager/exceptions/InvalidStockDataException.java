package com.cbfacademy.stockportfoliomanager.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when input data for a Stock is invalid.
 *
 * Example scenarios:
 * 
 *   - Stock symbol is null or empty
 *   - Attempting to create a Stock that already has an ID assigned
 * 
 * 
 *
 * 
 */
@Schema(
    description = "Exception thrown when stock input data is invalid (e.g., null/empty symbol, pre-assigned ID)",
    example = "Invalid stock data: symbol cannot be null or empty"
)
public class InvalidStockDataException extends RuntimeException {
    public InvalidStockDataException(String message) {
        super(message);
    }
}

