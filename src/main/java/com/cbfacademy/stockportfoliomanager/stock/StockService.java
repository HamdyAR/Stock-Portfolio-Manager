package com.cbfacademy.stockportfoliomanager.stock;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbfacademy.stockportfoliomanager.exceptions.DuplicateStockException;
import com.cbfacademy.stockportfoliomanager.exceptions.InvalidStockDataException;
import com.cbfacademy.stockportfoliomanager.exceptions.ResourceNotFoundException;
import com.cbfacademy.stockportfoliomanager.stock.dto.CreateStockRequest;
import com.cbfacademy.stockportfoliomanager.stock.dto.StockResponse;
import com.cbfacademy.stockportfoliomanager.stock.dto.UpdateStockRequest;

@Service
@Transactional
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public StockResponse createStock(CreateStockRequest stockRequest) {
        if (stockRequest.symbol() == null || stockRequest.symbol().isBlank()) {
            throw new InvalidStockDataException("Stock symbol cannot be null or blank");
        }


        String symbol = stockRequest.symbol().trim().toUpperCase();

        if (stockRepository.existsBySymbol(symbol)) {
            throw new DuplicateStockException(symbol);
        }

        Stock stock = Stock.builder()
                .symbol(symbol)
                .companyName(stockRequest.companyName())
                .exchange(stockRequest.exchange())
                .industry(stockRequest.industry())
                .build();

        Stock saved = stockRepository.save(stock);
        return toResponse(saved);
    }

    public List<StockResponse> getAllStocks() {
       return stockRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public StockResponse getStockById(UUID id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + id));
        return toResponse(stock);
    }

    public StockResponse updateStock(UUID id, UpdateStockRequest stockRequest) {
       Stock existing = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + id));

        existing.setCompanyName(stockRequest.companyName().trim());
        existing.setExchange(stockRequest.exchange().trim());
        existing.setIndustry(stockRequest.industry().trim());

        Stock saved = stockRepository.save(existing);
        return toResponse(saved);
    }

    public void deleteStock(UUID id) {
        if (!stockRepository.existsById(id)) {
            throw new ResourceNotFoundException("Stock not found with id: " + id);
        }
        stockRepository.deleteById(id);
    }

    public StockResponse getStockBySymbol(String symbol) {
        String normalized = symbol == null ? null : symbol.trim().toUpperCase();

        Stock stock = stockRepository.findBySymbol(normalized)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with symbol: " + symbol));
        return toResponse(stock);
    }

    public List<StockResponse> getStocksByIndustry(String industry) {
         return stockRepository.findByIndustryIgnoreCase(industry).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<StockResponse> getStocksByExchange(String exchange) {
        return stockRepository.findByExchangeIgnoreCase(exchange).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<StockResponse> getStocksByIndustryAndExchange(String industry, String exchange) {
        return stockRepository.findByIndustryIgnoreCaseAndExchangeIgnoreCase(industry, exchange).stream()
                .map(this::toResponse)
                .toList();
    }

    public boolean stockExists(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            return false;
        }
        return stockRepository.existsBySymbol(symbol.trim().toUpperCase());
    }

    private StockResponse toResponse(Stock stock) {
        return new StockResponse(
                stock.getId(),
                stock.getSymbol(),
                stock.getCompanyName(),
                stock.getExchange(),
                stock.getIndustry()
        );
    }
}
