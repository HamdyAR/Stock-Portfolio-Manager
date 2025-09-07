package com.cbfacademy.stockportfoliomanager.exceptions;

/**
 * Exception thrown when an attempt is made to create or update a Stock
 * with a symbol that already exists in the system.
 * 
 * Typical use cases:
 *
 *  - Creating a Stock with an existing symbol
 *  - Updating a Stock’s symbol to one already in use
 * 
 *
 * 
 */

public class DuplicateStockException extends RuntimeException {
    public DuplicateStockException(String symbol) {
        super("Stock with symbol '" + symbol + "' already exists.");
    }
}

