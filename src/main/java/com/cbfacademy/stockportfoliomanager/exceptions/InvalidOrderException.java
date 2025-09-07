package com.cbfacademy.stockportfoliomanager.exceptions;


/**
 * Exception thrown when input data for an Order is invalid.
 *
 * Example scenarios:
 * 
 *   - Volume is negative
 *   - Price is negative
 * 
 * 
 *
 * 
 */
public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
}

