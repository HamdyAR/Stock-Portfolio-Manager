package com.cbfacademy.stockportfoliomanager.stock;

import com.cbfacademy.stockportfoliomanager.exceptions.DuplicateStockException;
import com.cbfacademy.stockportfoliomanager.exceptions.InvalidStockDataException;
import com.cbfacademy.stockportfoliomanager.exceptions.ResourceNotFoundException;
import com.cbfacademy.stockportfoliomanager.stock.dto.CreateStockRequest;
import com.cbfacademy.stockportfoliomanager.stock.dto.StockResponse;
import com.cbfacademy.stockportfoliomanager.stock.dto.UpdateStockRequest;

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
        testStock = Stock.builder()
                .symbol("AAPL")
                .companyName("Apple Inc.")
                .exchange("NASDAQ")
                .industry("Technology")
                .build();
        
    }

    @Test
    void createStock_ValidStock_ReturnsStockResponse() {
        //Arrange
        CreateStockRequest request = new CreateStockRequest("AAPL", "Apple Inc.", "NASDAQ", "Technology");
        when(stockRepository.existsBySymbol("AAPL")).thenReturn(false);
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

       // Act
        StockResponse result = stockService.createStock(request);

        // Assert
        assertNotNull(result);
        assertEquals("AAPL", result.symbol()); // Record accessor style
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    void createStock_NullSymbol_ThrowsInvalidStockDataException() {
        CreateStockRequest request = new CreateStockRequest(null, "Apple Inc.", "NASDAQ", "Technology");

        // Act & Assert
        assertThrows(InvalidStockDataException.class, () -> stockService.createStock(request));
        verify(stockRepository, never()).save(any());
    }

    @Test
    void createStock_EmptySymbol_ThrowsInvalidStockDataException() {
       CreateStockRequest request = new CreateStockRequest("", "Apple Inc.", "NASDAQ", "Technology");

        // Act & Assert
        assertThrows(InvalidStockDataException.class, () -> stockService.createStock(request));
        verify(stockRepository, never()).save(any());
    }

    @Test
    void createStock_DuplicateSymbol_ThrowsDuplicateStockException() {
        // Arrange
        CreateStockRequest request = new CreateStockRequest("AAPL", "Apple Inc.", "NASDAQ", "Technology");
        
        when(stockRepository.existsBySymbol("AAPL")).thenReturn(true);
    
        // Act & Assert
        DuplicateStockException exception = assertThrows(
            DuplicateStockException.class,
            () -> stockService.createStock(request)
        );
        
        assertTrue(exception.getMessage().contains("AAPL"));
        verify(stockRepository, never()).save(any());
    }

    @Test
    void getStockById_ExistingId_ReturnsStock() {
        
        // Arrange
        when(stockRepository.findById(testId)).thenReturn(Optional.of(testStock));
        
        // Act
        StockResponse result = stockService.getStockById(testId);
    
        // Assert
        assertNotNull(result);
        assertEquals("AAPL", result.symbol());
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
    void getStockBySymbol_ExistingSymbol_ReturnsStockResponse() {
        // Arrange
        when(stockRepository.findBySymbol("AAPL")).thenReturn(Optional.of(testStock));

        // Act
        StockResponse result = stockService.getStockBySymbol("AAPL");

        // Assert
        assertNotNull(result);
        assertEquals("AAPL", result.symbol()); // Record accessor style
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
    void getAllStocks_ReturnsAllStockResponse() {
        
       // Arrange
        List<Stock> stocks = Arrays.asList(testStock);
        when(stockRepository.findAll()).thenReturn(stocks);
       
        // Act
        List<StockResponse> result = stockService.getAllStocks();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof StockResponse);
    }

    @Test
    void updateStock_ValidUpdate_ReturnsUpdatedStockResponse() {
        // Arrange
        UpdateStockRequest updateRequest = new UpdateStockRequest("Apple Corporation", "NASDAQ", "Technology");
        when(stockRepository.findById(testId)).thenReturn(Optional.of(testStock));
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

        // Act
        StockResponse result = stockService.updateStock(testId, updateRequest);

        // Assert
        assertNotNull(result);
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    void updateStock_NonExistentStock_ThrowsResourceNotFoundException() {
    // Arrange 
    UpdateStockRequest updateRequest = new UpdateStockRequest("Apple", "NASDAQ", "Technology");
    
    // Mock the repository returning empty to trigger the exception
    when(stockRepository.findById(testId)).thenReturn(Optional.empty());

    // Act & Assert
    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class,
        () -> stockService.updateStock(testId, updateRequest)
    );
    
    assertTrue(exception.getMessage().contains(testId.toString()));
    verify(stockRepository, never()).save(any());
}

    @Test
    void deleteStock_ExistingStock_DeletesSuccessfully() {
        
       // Arrange - Your StockService uses existsById in deleteStock
        when(stockRepository.existsById(testId)).thenReturn(true);

        // Act
        stockService.deleteStock(testId);

        // Assert
        verify(stockRepository).deleteById(testId);
    }

    @Test
    void deleteStock_NonExistentStock_ThrowsResourceNotFoundException() {
       
        when(stockRepository.existsById(testId)).thenReturn(false);
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> stockService.deleteStock(testId)
        );
        assertTrue(exception.getMessage().contains(testId.toString()));
        verify(stockRepository).existsById(testId);
        verify(stockRepository, never()).deleteById(any());
    }

    @Test
    void getStocksByIndustry_ReturnsFilteredStocks() {
    // Arrange
    List<Stock> techStocks = Arrays.asList(testStock);
    when(stockRepository.findByIndustryIgnoreCase("Technology")).thenReturn(techStocks);

    // Act
    List<StockResponse> result = stockService.getStocksByIndustry("Technology");

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("AAPL", result.get(0).symbol());
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
