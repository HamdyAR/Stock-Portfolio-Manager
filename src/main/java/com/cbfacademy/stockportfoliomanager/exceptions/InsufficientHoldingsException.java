package com.cbfacademy.stockportfoliomanager.exceptions;


/**
 * Exception thrown when a SELL order is initiated such that amount requested to be sold 
 * is less than the current holdings for the stock.
 *
 * 
 */
public class InsufficientHoldingsException extends RuntimeException {
    public InsufficientHoldingsException(String symbol, int requested, int available) {
        super("Insufficient holdings for stock '" + symbol + "'. Requested: " 
              + requested + ", Available: " + available);
    }
}

