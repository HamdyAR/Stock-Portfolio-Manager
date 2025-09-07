package com.cbfacademy.stockportfoliomanager.stock;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stocks")
@Tag(name = "Stock Management", description = "API for managing stock catalog")
public class StockController {
    
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Operation(
        summary = "Get all stocks",
        description = "Retrieve all stocks in the catalog with optional filtering by industry and exchange"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved stocks",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks(
            @Parameter(description = "Filter by industry (e.g., Technology, Healthcare)")
            @RequestParam(required = false) String industry,
            @Parameter(description = "Filter by exchange (e.g., NASDAQ, NYSE)")
            @RequestParam(required = false) String exchange) {
        
        List<Stock> stocks;
        
        if (industry != null && exchange != null) {
            stocks = stockService.getStocksByIndustryAndExchange(industry, exchange);
        } else if (industry != null) {
            stocks = stockService.getStocksByIndustry(industry);
        } else if (exchange != null) {
            stocks = stockService.getStocksByExchange(exchange);
        } else {
            stocks = stockService.getAllStocks();
        }
        
        return ResponseEntity.ok(stocks);
    }

    @Operation(
        summary = "Get stock by ID",
        description = "Retrieve a specific stock by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))),
        @ApiResponse(responseCode = "404", description = "Stock not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<Stock> getStockById(
            @Parameter(description = "Stock UUID", required = true)
            @PathVariable UUID id) {
        Stock stock = stockService.getStockById(id);
        return ResponseEntity.ok(stock);
    }

    @Operation(
        summary = "Get stock by symbol",
        description = "Retrieve a specific stock by its trading symbol"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))),
        @ApiResponse(responseCode = "404", description = "Stock not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<Stock> getStockBySymbol(
            @Parameter(description = "Stock trading symbol (e.g., AAPL, GOOGL)", required = true)
            @PathVariable String symbol) {
        Stock stock = stockService.getStockBySymbol(symbol);
        return ResponseEntity.ok(stock);
    }

    @Operation(
        summary = "Create new stock",
        description = "Add a new stock to the catalog"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Stock created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))),
        @ApiResponse(responseCode = "400", description = "Invalid stock data or duplicate symbol"),
        @ApiResponse(responseCode = "409", description = "Stock symbol already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Stock> createStock(
            @Parameter(description = "Stock data", required = true)
            @RequestBody Stock stock) {
        Stock createdStock = stockService.createStock(stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStock);
    }

    @Operation(
        summary = "Update existing stock",
        description = "Update stock information by ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock updated successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))),
        @ApiResponse(responseCode = "400", description = "Invalid stock data"),
        @ApiResponse(responseCode = "404", description = "Stock not found"),
        @ApiResponse(responseCode = "409", description = "Stock symbol already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(
            @Parameter(description = "Stock UUID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated stock data", required = true)
            @RequestBody Stock stock) {
        Stock updatedStock = stockService.updateStock(id, stock);
        return ResponseEntity.ok(updatedStock);
    }

    @Operation(
        summary = "Delete stock",
        description = "Remove a stock from the catalog"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Stock deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Stock not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(
            @Parameter(description = "Stock UUID", required = true)
            @PathVariable UUID id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}