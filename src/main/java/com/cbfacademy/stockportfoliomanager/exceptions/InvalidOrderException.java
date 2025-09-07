package com.cbfacademy.stockportfoliomanager.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when input data for an Order is invalid.
 *
 * Example scenarios:
 * 
 *   - Volume is negative
 *   - Price is negative
 * 
 *
 */
@Schema(
    description = "Exception thrown when order input data is invalid (e.g., negative volume or price)",
    example = "Invalid order: volume must be positive"
)
public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
}