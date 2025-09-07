package com.cbfacademy.stockportfoliomanager.exceptions;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Global exception handler for REST controllers.
 * 
 * - This class centralizes exception handling logic,
 * ensuring consistent error responses across the API
 */

// @ControllerAdvice
@Tag(name = "Error Responses", description = "Standard error response formats")
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ApiResponse(
        responseCode = "404",
        description = "Resource not found",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Map.class),
            examples = @ExampleObject(
                name = "Stock Not Found",
                summary = "When a requested stock doesn't exist",
                value = """
                {
                    "timestamp": "2025-01-15T10:30:00",
                    "status": 404,
                    "error": "Not Found",
                    "message": "Stock with symbol 'INVALID' not found"
                }
                """
            )
        )
    )
    @Hidden
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not Found",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler({DuplicateStockException.class, InvalidOrderException.class})
    @ApiResponse(
        responseCode = "400",
        description = "Bad request - invalid input data",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Map.class),
            examples = {
                @ExampleObject(
                    name = "Duplicate Stock",
                    summary = "When trying to create a stock that already exists",
                    value = """
                    {
                        "timestamp": "2025-01-15T10:30:00",
                        "status": 400,
                        "error": "Bad Request",
                        "message": "Stock with symbol 'AAPL' already exists"
                    }
                    """
                ),
                @ExampleObject(
                    name = "Invalid Order",
                    summary = "When order data is invalid",
                    value = """
                    {
                        "timestamp": "2025-01-15T10:30:00",
                        "status": 400,
                        "error": "Bad Request",
                        "message": "Invalid order: price must be positive"
                    }
                    """
                )
            }
        )
    )
    @Hidden
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(InsufficientHoldingsException.class)
    @ApiResponse(
        responseCode = "409",
        description = "Conflict - insufficient holdings for sell order",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Map.class),
            examples = @ExampleObject(
                name = "Insufficient Holdings",
                summary = "When trying to sell more shares than owned",
                value = """
                {
                    "timestamp": "2025-01-15T10:30:00",
                    "status": 409,
                    "error": "Conflict",
                    "message": "Insufficient holdings: trying to sell 100 shares but only own 50"
                }
                """
            )
        )
    )
    @Hidden
    public ResponseEntity<Map<String, Object>> handleConflict(InsufficientHoldingsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Conflict",
                "message", ex.getMessage()
        ));
    }
}