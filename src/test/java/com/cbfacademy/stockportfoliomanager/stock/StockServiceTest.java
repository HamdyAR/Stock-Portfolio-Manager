package com.cbfacademy.stockportfoliomanager.stock;

import com.cbfacademy.stockportfoliomanager.exceptions.DuplicateStockException;
import com.cbfacademy.stockportfoliomanager.exceptions.InvalidStockDataException;
import com.cbfacademy.stockportfoliomanager.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private Stock testStock;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testStock = new Stock("AAPL", "Apple Inc.", "NASDAQ", "Technology");
        
    }

    @Test
    void createStock_ValidStock_ReturnsCreatedStock() {
        when(stockRepository.existsBySymbol("AAPL")).thenReturn(false);
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

        Stock result = stockService.createStock(testStock);

        assertNotNull(result);
        assertEquals("AAPL", result.getSymbol());
        assertEquals("Apple Inc.", result.getCompanyName());
        verify(stockRepository).existsBySymbol("AAPL");
        verify(stockRepository).save(testStock);
    }

    @Test
    void createStock_NullSymbol_ThrowsInvalidStockDataException() {
        Stock invalidStock = new Stock(null, "Apple Inc.", "NASDAQ", "Technology");

        InvalidStockDataException exception = assertThrows(
            InvalidStockDataException.class,
            () -> stockService.createStock(invalidStock)
        );
        assertEquals("Stock symbol cannot be null or empty", exception.getMessage());
        verify(stockRepository, never()).save(any());
    }

    @Test
    void createStock_EmptySymbol_ThrowsInvalidStockDataException() {
        Stock invalidStock = new Stock("", "Apple Inc.", "NASDAQ", "Technology");

        InvalidStockDataException exception = assertThrows(
            InvalidStockDataException.class,
            () -> stockService.createStock(invalidStock)
        );
        assertEquals("Stock symbol cannot be null or empty", exception.getMessage());
    }

    @Test
    void createStock_DuplicateSymbol_ThrowsDuplicateStockException() {
       
        when(stockRepository.existsBySymbol("AAPL")).thenReturn(true);
    
        DuplicateStockException exception = assertThrows(
            DuplicateStockException.class,
            () -> stockService.createStock(testStock)
        );
        assertTrue(exception.getMessage().contains("AAPL"));
        verify(stockRepository, never()).save(any());
    }

    @Test
    void getStockById_ExistingId_ReturnsStock() {
        
        when(stockRepository.findById(testId)).thenReturn(Optional.of(testStock));
        
        Stock result = stockService.getStockById(testId);
    
        assertNotNull(result);
        assertEquals("AAPL", result.getSymbol());
        verify(stockRepository).findById(testId);
    }

    @Test
    void getStockById_NonExistentId_ThrowsResourceNotFoundException() {
        
        when(stockRepository.findById(testId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> stockService.getStockById(testId)
        );
        assertTrue(exception.getMessage().contains(testId.toString()));
    }

    @Test
    void getStockBySymbol_ExistingSymbol_ReturnsStock() {
        
        when(stockRepository.findBySymbol("AAPL")).thenReturn(Optional.of(testStock));

        Stock result = stockService.getStockBySymbol("AAPL");

        assertNotNull(result);
        assertEquals("AAPL", result.getSymbol());
        verify(stockRepository).findBySymbol("AAPL");
    }

    @Test
    void getStockBySymbol_NonExistentSymbol_ThrowsResourceNotFoundException() {
        
        when(stockRepository.findBySymbol("INVALID")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> stockService.getStockBySymbol("INVALID")
        );
        assertTrue(exception.getMessage().contains("INVALID"));
    }

    @Test
    void getAllStocks_ReturnsAllStocks() {
        
        Stock stock2 = new Stock("GOOGL", "Alphabet Inc.", "NASDAQ", "Technology");
        List<Stock> expectedStocks = Arrays.asList(testStock, stock2);
        when(stockRepository.findAll()).thenReturn(expectedStocks);
       
        List<Stock> result = stockService.getAllStocks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("AAPL", result.get(0).getSymbol());
        assertEquals("GOOGL", result.get(1).getSymbol());
        verify(stockRepository).findAll();
    }

    @Test
    void updateStock_ValidUpdate_ReturnsUpdatedStock() {
        Stock updateData = new Stock("AAPL", "Apple Corporation", "NASDAQ", "Technology");
        when(stockRepository.findById(testId)).thenReturn(Optional.of(testStock));
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

        Stock result = stockService.updateStock(testId, updateData);

        assertNotNull(result);
        verify(stockRepository).findById(testId);
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    void updateStock_NonExistentStock_ThrowsResourceNotFoundException() {
        
        Stock updateData = new Stock("AAPL", "Apple Corporation", "NASDAQ", "Technology");
        when(stockRepository.findById(testId)).thenReturn(Optional.empty());

       
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> stockService.updateStock(testId, updateData)
        );
        assertTrue(exception.getMessage().contains(testId.toString()));
    }

    @Test
    void deleteStock_ExistingStock_DeletesSuccessfully() {
        
        when(stockRepository.findById(testId)).thenReturn(Optional.of(testStock));

        
        stockService.deleteStock(testId);

       
        verify(stockRepository).findById(testId);
        verify(stockRepository).delete(testStock);
    }

    @Test
    void deleteStock_NonExistentStock_ThrowsResourceNotFoundException() {
       
        when(stockRepository.findById(testId)).thenReturn(Optional.empty());
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> stockService.deleteStock(testId)
        );
        assertTrue(exception.getMessage().contains(testId.toString()));
    }

    @Test
    void getStocksByIndustry_ReturnsFilteredStocks() {
        
        List<Stock> techStocks = Arrays.asList(testStock);
        when(stockRepository.findByIndustryIgnoreCase("Technology")).thenReturn(techStocks);

       
        List<Stock> result = stockService.getStocksByIndustry("Technology");

       
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("AAPL", result.get(0).getSymbol());
        verify(stockRepository).findByIndustryIgnoreCase("Technology");
    }

    @Test
    void stockExists_ExistingSymbol_ReturnsTrue() {
        
        when(stockRepository.existsBySymbol("AAPL")).thenReturn(true);

       
        boolean result = stockService.stockExists("AAPL");

        
        assertTrue(result);
        verify(stockRepository).existsBySymbol("AAPL");
    }

    @Test
    void stockExists_NonExistentSymbol_ReturnsFalse() {
       
        when(stockRepository.existsBySymbol("INVALID")).thenReturn(false);

        boolean result = stockService.stockExists("INVALID");

        assertFalse(result);
        verify(stockRepository).existsBySymbol("INVALID");
    }
}
