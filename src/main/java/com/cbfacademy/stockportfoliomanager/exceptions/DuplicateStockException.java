package com.cbfacademy.stockportfoliomanager.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when an attempt is made to create or update a Stock
 * with a symbol that already exists in the system.
 * 
 * Typical use cases:
 *
 *  - Creating a Stock with an existing symbol
 *  - Updating a Stock's symbol to one already in use
 * 
 *
 * 
 */

@Schema(
    description = "Exception thrown when attempting to create or update a stock with a symbol that already exists",
    example = "Stock with symbol 'AAPL' already exists."
)
public class DuplicateStockException extends RuntimeException {
    
    public DuplicateStockException(String symbol) {
        super("Stock with symbol '" + symbol + "' already exists.");
    }
}