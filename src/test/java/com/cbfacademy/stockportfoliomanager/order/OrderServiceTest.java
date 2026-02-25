package com.cbfacademy.stockportfoliomanager.order;

import com.cbfacademy.stockportfoliomanager.stock.Stock;
import com.cbfacademy.stockportfoliomanager.stock.StockService;
import com.cbfacademy.stockportfoliomanager.exceptions.InvalidOrderException;
import com.cbfacademy.stockportfoliomanager.exceptions.InsufficientHoldingsException;
import com.cbfacademy.stockportfoliomanager.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StockService stockService;

    @InjectMocks
    private OrderService orderService;

    private Stock testStock;
    private Order testBuyOrder;
    private Order testSellOrder;
    private UUID testOrderId;

    @BeforeEach
    void setUp() {
        testOrderId = UUID.randomUUID();
        testStock = new Stock("AAPL", "Apple Inc.", "NASDAQ", "Technology");
        
        testBuyOrder = new Order(testStock, OrderSide.BUY, 100, new BigDecimal("150.00"));
        testSellOrder = new Order(testStock, OrderSide.SELL, 30, new BigDecimal("155.00"));
    }

    @Test
    void placeOrder_ValidBuyOrder_ReturnsCreatedOrder() {
        // Arrange
        when(stockService.getStockBySymbol("AAPL")).thenReturn(testStock);
        when(orderRepository.save(any(Order.class))).thenReturn(testBuyOrder);

        // Act
        Order result = orderService.placeOrder("AAPL", OrderSide.BUY, 100, new BigDecimal("150.00"));

        // Assert
        assertNotNull(result);
        assertEquals(OrderSide.BUY, result.getSide());
        assertEquals(100, result.getVolume());
        assertEquals(new BigDecimal("150.00"), result.getPrice());
        verify(stockService).getStockBySymbol("AAPL");
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void placeOrder_ValidSellOrderWithSufficientHoldings_ReturnsCreatedOrder() {
        // Arrange
        when(stockService.getStockBySymbol("AAPL")).thenReturn(testStock);
        when(orderRepository.findByStockSymbol("AAPL")).thenReturn(Arrays.asList(testBuyOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testSellOrder);

        // Act
        Order result = orderService.placeOrder("AAPL", OrderSide.SELL, 30, new BigDecimal("155.00"));

        // Assert
        assertNotNull(result);
        assertEquals(OrderSide.SELL, result.getSide());
        assertEquals(30, result.getVolume());
        verify(stockService).getStockBySymbol("AAPL");
        verify(orderRepository).findByStockSymbol("AAPL");
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void placeOrder_NullStockSymbol_ThrowsInvalidOrderException() {
        // Act & Assert
        InvalidOrderException exception = assertThrows(
            InvalidOrderException.class,
            () -> orderService.placeOrder(null, OrderSide.BUY, 100, new BigDecimal("150.00"))
        );
        assertEquals("Stock symbol cannot be null or empty", exception.getMessage());
        verify(stockService, never()).getStockBySymbol(any());
    }

    @Test
    void placeOrder_EmptyStockSymbol_ThrowsInvalidOrderException() {
        // Act & Assert
        InvalidOrderException exception = assertThrows(
            InvalidOrderException.class,
            () -> orderService.placeOrder("", OrderSide.BUY, 100, new BigDecimal("150.00"))
        );
        assertEquals("Stock symbol cannot be null or empty", exception.getMessage());
    }

    @Test
    void placeOrder_NullOrderSide_ThrowsInvalidOrderException() {
        // Act & Assert
        InvalidOrderException exception = assertThrows(
            InvalidOrderException.class,
            () -> orderService.placeOrder("AAPL", null, 100, new BigDecimal("150.00"))
        );
        assertEquals("Order side cannot be null", exception.getMessage());
    }

    @Test
    void placeOrder_NegativeVolume_ThrowsInvalidOrderException() {
        // Act & Assert
        InvalidOrderException exception = assertThrows(
            InvalidOrderException.class,
            () -> orderService.placeOrder("AAPL", OrderSide.BUY, -10, new BigDecimal("150.00"))
        );
        assertEquals("Volume must be positive and greater than zero", exception.getMessage());
    }

    @Test
    void placeOrder_ZeroPrice_ThrowsInvalidOrderException() {
        // Act & Assert
        InvalidOrderException exception = assertThrows(
            InvalidOrderException.class,
            () -> orderService.placeOrder("AAPL", OrderSide.BUY, 100, BigDecimal.ZERO)
        );
        assertEquals("Price must be positive and greater than zero", exception.getMessage());
    }

    @Test
    void placeOrder_SellOrderWithInsufficientHoldings_ThrowsInsufficientHoldingsException() {
        // Arrange
        
        when(stockService.getStockBySymbol("AAPL")).thenReturn(testStock);
        // 100 shares
        when(orderRepository.findByStockSymbol("AAPL")).thenReturn(Arrays.asList(testBuyOrder)); 

        // Act & Assert
        InsufficientHoldingsException exception = assertThrows(
            InsufficientHoldingsException.class,
            () -> orderService.placeOrder("AAPL", OrderSide.SELL, 150, 
            new BigDecimal("155.00")) // Try to sell 150
        );
        assertTrue(exception.getMessage().contains("AAPL"));
        verify(stockService).getStockBySymbol("AAPL");
        verify(orderRepository).findByStockSymbol("AAPL");
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_NonExistentStock_ThrowsResourceNotFoundException() {
        // Arrange
        when(stockService.getStockBySymbol("INVALID")).thenThrow(
            new ResourceNotFoundException("Stock not found with symbol: INVALID")
        );

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> orderService.placeOrder("INVALID", OrderSide.BUY, 100, new BigDecimal("150.00"))
        );
        assertTrue(exception.getMessage().contains("INVALID"));
        verify(stockService).getStockBySymbol("INVALID");
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getAllOrders_ReturnsOrdersOrderedByTimestamp() {
        // Arrange
        List<Order> expectedOrders = Arrays.asList(testSellOrder, testBuyOrder);
        when(orderRepository.findAllByOrderByTimestampDesc()).thenReturn(expectedOrders);

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testSellOrder, result.get(0));
        assertEquals(testBuyOrder, result.get(1));
        verify(orderRepository).findAllByOrderByTimestampDesc();
    }

    @Test
    void getOrderById_ExistingOrder_ReturnsOrder() {
        // Arrange
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testBuyOrder));

        // Act
        Order result = orderService.getOrderById(testOrderId);

        // Assert
        assertNotNull(result);
        assertEquals(testBuyOrder, result);
        verify(orderRepository).findById(testOrderId);
    }

    @Test
    void getOrderById_NonExistentOrder_ThrowsResourceNotFoundException() {
        // Arrange
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> orderService.getOrderById(testOrderId)
        );
        assertTrue(exception.getMessage().contains(testOrderId.toString()));
    }

    @Test
    void getOrdersByStock_ReturnsFilteredOrders() {
        // Arrange
        List<Order> appleOrders = Arrays.asList(testBuyOrder, testSellOrder);
        when(orderRepository.findByStockSymbol("AAPL")).thenReturn(appleOrders);

        // Act
        List<Order> result = orderService.getOrdersByStock("AAPL");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository).findByStockSymbol("AAPL");
    }

    @Test
    void getOrdersBySide_ReturnsFilteredOrders() {
        // Arrange
        List<Order> buyOrders = Arrays.asList(testBuyOrder);
        when(orderRepository.findBySide(OrderSide.BUY)).thenReturn(buyOrders);

        // Act
        List<Order> result = orderService.getOrdersBySide(OrderSide.BUY);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(OrderSide.BUY, result.get(0).getSide());
        verify(orderRepository).findBySide(OrderSide.BUY);
    }

    @Test
    void getCurrentPortfolio_ReturnsPortfolioItems() {
        Object[] portfolioData = {"AAPL", "Apple Inc.", "NASDAQ", "Technology", 70L};
        List<Object[]> portfolioResults = Arrays.asList(new Object[][]{portfolioData});
        when(orderRepository.calculatePortfolioHoldings()).thenReturn(portfolioResults);

        // Act
        List<PortfolioItem> result = orderService.getCurrentPortfolio();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        PortfolioItem item = result.get(0);
        assertEquals("AAPL", item.getSymbol());
        assertEquals("Apple Inc.", item.getCompany());
        assertEquals("NASDAQ", item.getExchange());
        assertEquals("Technology", item.getIndustry());
        assertEquals(70, item.getHoldings());
        verify(orderRepository).calculatePortfolioHoldings();
    }

    @Test
    void getCurrentHoldingsForStock_CalculatesCorrectHoldings() {
        // This tests the private method indirectly through placeOrder
        // Arrange
        Order additionalBuyOrder = new Order(testStock, OrderSide.BUY, 50, new BigDecimal("160.00"));
        List<Order> orders = Arrays.asList(testBuyOrder, additionalBuyOrder, testSellOrder); // 100 + 50 - 30 = 120
        
        when(stockService.getStockBySymbol("AAPL")).thenReturn(testStock);
        when(orderRepository.findByStockSymbol("AAPL")).thenReturn(orders);
        when(orderRepository.save(any(Order.class))).thenReturn(testSellOrder);

        // Act - try to sell 50 shares (should succeed since we have 120)
        Order result = orderService.placeOrder("AAPL", OrderSide.SELL, 50, new BigDecimal("155.00"));

        // Assert
        assertNotNull(result);
        verify(orderRepository).findByStockSymbol("AAPL");
        verify(orderRepository).save(any(Order.class));
    }
}
