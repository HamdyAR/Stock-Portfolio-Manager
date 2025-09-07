package com.cbfacademy.stockportfoliomanager.stock;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }

    public Stock createStock(Stock stock) throws IllegalArgumentException{
         if (stock.getSymbol() == null || stock.getSymbol().trim().isEmpty()) {
            throw new IllegalArgumentException("Stock symbol cannot be null or empty");
        }

        if(stock.getId() != null){
            throw new IllegalArgumentException("Stock already has an ID, cannot create");
        }
        if(stockRepository.existsBySymbol(stock.getSymbol())){
            throw new IllegalArgumentException("Stock with symbol " + stock.getSymbol() + " already exists");
        }
        return stockRepository.save(stock);
    }

    public List<Stock> getAllStocks(){
       return stockRepository.findAll();
    }

    public Stock getStockById(UUID id) throws NoSuchElementException{
       return stockRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Stock not found with id: " + id));
    }

    public Stock updateStock(UUID id, Stock stock) throws NoSuchElementException{
        Stock existingStock = stockRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Stock not found with id: " + id));

        if(!existingStock.getSymbol().equals(stock.getSymbol()) && stockRepository.existsBySymbol(stock.getSymbol())){
            throw new IllegalArgumentException("Stock with symbol " + stock.getSymbol() + " already exists");
        }
        existingStock.setSymbol(stock.getSymbol());
        existingStock.setCompanyName(stock.getCompanyName());
        existingStock.setExchange(stock.getExchange());
        existingStock.setIndustry(stock.getIndustry());

        return stockRepository.save(existingStock);

    }

    public void deleteStock(UUID id){
       Stock existingStock = stockRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Stock not found with id: " + id));
       stockRepository.delete(existingStock);
    }


    public Stock getStockBySymbol(String symbol) throws NoSuchElementException {
        return stockRepository.findBySymbol(symbol)
            .orElseThrow(() -> new NoSuchElementException("Stock not found with symbol: " + symbol));
    }

    public List<Stock> getStocksByIndustry(String industry) {
        return stockRepository.findByIndustry(industry);
    }

    public List<Stock> getStocksByExchange(String exchange) {
        return stockRepository.findByExchange(exchange);
    }

    public List<Stock> getStocksByIndustryAndExchange(String industry, String exchange) {
        return stockRepository.findByIndustryAndExchange(industry, exchange);
    }

    public boolean stockExists(String symbol) {
        return stockRepository.existsBySymbol(symbol);
    }


}
