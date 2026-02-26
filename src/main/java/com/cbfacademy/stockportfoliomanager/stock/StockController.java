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

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbfacademy.stockportfoliomanager.stock.dto.CreateStockRequest;
import com.cbfacademy.stockportfoliomanager.stock.dto.StockResponse;
import com.cbfacademy.stockportfoliomanager.stock.dto.UpdateStockRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/stocks")
@Tag(name = "Stock Management", description = "API for managing stock catalog")
public class StockController {
    
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    //Create a new stock
    @Operation(
        summary = "Create new stock",
        description = "Add a new stock to the catalog"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Stock created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = StockResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid stock data or duplicate symbol"),
        @ApiResponse(responseCode = "409", description = "Stock symbol already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<StockResponse> createStock(@Valid @RequestBody CreateStockRequest request){
        StockResponse createdStock = stockService.createStock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStock);
    }
    
    
    //Get all stocks
@GetMapping
public ResponseEntity<List<StockResponse>> getAllStocks(
        @RequestParam(required = false) String industry,
        @RequestParam(required = false) String exchange) {

    industry = (industry == null) ? null : industry.trim();
    exchange = (exchange == null) ? null : exchange.trim();

    List<StockResponse> stocks;

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

    // Get stock by ID
     @Operation(
        summary = "Get stock by ID",
        description = "Retrieve a specific stock by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = StockResponse.class))),
        @ApiResponse(responseCode = "404", description = "Stock not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getStockById(@PathVariable UUID id) {
        StockResponse stock = stockService.getStockById(id);
        return ResponseEntity.ok(stock);
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
    public ResponseEntity<StockResponse> updateStock(@PathVariable UUID id, @Valid @RequestBody UpdateStockRequest request) {
        StockResponse updatedStock = stockService.updateStock(id, request);
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
    
    //Get stock by symbol
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<StockResponse> getStockBySymbol(@PathVariable String symbol) {
        StockResponse stock = stockService.getStockBySymbol(symbol);
        return ResponseEntity.ok(stock);
    }
}
