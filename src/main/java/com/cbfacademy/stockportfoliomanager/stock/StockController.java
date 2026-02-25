package com.cbfacademy.stockportfoliomanager.stock;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService){
         this.stockService = stockService;
    }

    //Create a new stock
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
    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getStockById(@PathVariable UUID id) {
        StockResponse stock = stockService.getStockById(id);
        return ResponseEntity.ok(stock);
    }

    // Update stock
    @PutMapping("/{id}")
    public ResponseEntity<StockResponse> updateStock(@PathVariable UUID id, @Valid @RequestBody UpdateStockRequest request) {
        StockResponse updatedStock = stockService.updateStock(id, request);
        return ResponseEntity.ok(updatedStock);
    }

    // Delete stock
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable UUID id) {
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
