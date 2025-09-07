package com.cbfacademy.stockportfoliomanager.exceptions;

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
public class InvalidStockDataException extends RuntimeException {
    public InvalidStockDataException(String message) {
        super(message);
    }
}

