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

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService){
         this.stockService = stockService;
    }

    //Create a new stock
    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock){
        Stock createdStock = stockService.createStock(stock);
        return new ResponseEntity<>(createdStock, HttpStatus.CREATED);
    }
    
    //Get all stocks
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks(
        @RequestParam(required = false) String industry,
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

    // Get stock by ID
    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable UUID id) {
        Stock stock = stockService.getStockById(id);
        return ResponseEntity.ok(stock);
    }

    // Update stock
    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable UUID id, @RequestBody Stock stock) {
        Stock updatedStock = stockService.updateStock(id, stock);
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
    public ResponseEntity<Stock> getStockBySymbol(@PathVariable String symbol) {
        Stock stock = stockService.getStockBySymbol(symbol);
        return ResponseEntity.ok(stock);
    }
}
