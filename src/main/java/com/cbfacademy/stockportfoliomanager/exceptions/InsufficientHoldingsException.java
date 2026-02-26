package com.cbfacademy.stockportfoliomanager.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Exception thrown when a SELL order is initiated such that amount requested to be sold 
 * is less than the current holdings for the stock.
 *
 * 
 */
@Schema(
    description = "Exception thrown when attempting to sell more shares than currently owned",
    example = "Insufficient holdings for stock 'AAPL'. Requested: 100, Available: 50"
)
public class InsufficientHoldingsException extends RuntimeException {
    public InsufficientHoldingsException(String symbol, int requested, int available) {
        super("Insufficient holdings for stock '" + symbol + "'. Requested: " 
              + requested + ", Available: " + available);
    }
}