package com.cbfacademy.stockportfoliomanager.stock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends ListCrudRepository<Stock, UUID> {
    
    // Custom query methods for filtering
    Optional<Stock> findBySymbol(String symbol);
    List<Stock> findByIndustry(String industry);
    List<Stock> findByExchange(String exchange);
    
    //Method to validate if a stock already exists to ensure no duplicate symbols
    boolean existsBySymbol(String symbol);
    
    // Advanced filtering (for extra functionality)
    List<Stock> findBySymbolContainingIgnoreCase(String symbol);
    List<Stock> findByIndustryAndExchange(String industry, String exchange);
}
