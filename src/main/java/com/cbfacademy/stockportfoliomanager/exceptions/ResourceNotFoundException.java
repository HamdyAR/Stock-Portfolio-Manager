package com.cbfacademy.stockportfoliomanager.exceptions;

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

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

