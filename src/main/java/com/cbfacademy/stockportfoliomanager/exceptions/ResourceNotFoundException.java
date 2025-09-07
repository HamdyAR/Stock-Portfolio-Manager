package com.cbfacademy.stockportfoliomanager.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when a requested resource cannot be found.
 * 
 * Typical use cases:
 *
 *  - Stock with given ID or symbol does not exist
 *  - Order with given ID is missing
 * 
 *
 * 
 */
@Schema(
    description = "Exception thrown when a requested resource (stock, order, etc.) cannot be found",
    example = "Stock with symbol 'INVALID' not found"
)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}