package com.cbfacademy.stockportfoliomanager.order;

import com.cbfacademy.stockportfoliomanager.stock.Stock;
import com.cbfacademy.stockportfoliomanager.stock.StockService;
import com.cbfacademy.stockportfoliomanager.client.AlphaVantageClient;
import com.cbfacademy.stockportfoliomanager.exceptions.InsufficientHoldingsException;
import com.cbfacademy.stockportfoliomanager.exceptions.InvalidOrderException;
import com.cbfacademy.stockportfoliomanager.exceptions.ResourceNotFoundException;
import com.cbfacademy.stockportfoliomanager.order.dto.CreateOrderRequest;
import com.cbfacademy.stockportfoliomanager.order.dto.OrderResponse;
import com.cbfacademy.stockportfoliomanager.order.dto.PortfolioResponse;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StockService stockService;

    @Mock
    private AlphaVantageClient alphaVantageClient;

    @InjectMocks
    private OrderService orderService;

    private Stock testStock;
    private Order buyOrder;
    private Order sellOrder;
    private UUID testOrderId;

    @BeforeEach
    void setUp() {
        testOrderId = UUID.randomUUID();
        testStock = Stock.builder()
                .symbol("AAPL")
                .companyName("Apple Inc.")
                .exchange("NASDAQ")
                .industry("Technology")
                .build();

        // A standard Buy order to represent "Existing Holdings"
    buyOrder = Order.builder()
            .id(testOrderId)
            .stock(testStock)
            .side(OrderSide.BUY)
            .volume(100)
            .price(new BigDecimal("150.00"))
            .build();

    // A standard Sell order for testing sell logic
    sellOrder = Order.builder()
            .id(UUID.randomUUID())
            .stock(testStock)
            .side(OrderSide.SELL)
            .volume(30)
            .price(new BigDecimal("155.00"))
            .build();        
    }

    @Test
    void placeOrder_ValidBuyOrder_ReturnsOrderResponse() {
    // Arrange - Remove the BigDecimal argument to match your 3-field Record
    CreateOrderRequest request = new CreateOrderRequest("AAPL", OrderSide.BUY, 100);
    BigDecimal marketPrice = new BigDecimal("150.00");

    // Mocking the Entity return
    when(stockService.getStockEntityBySymbol("AAPL")).thenReturn(Optional.of(testStock));
    
    when(alphaVantageClient.getStockPrice("AAPL")).thenReturn(marketPrice);

    // Mocking the save operation
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);

    // Act
    OrderResponse result = orderService.placeOrder(request);

    // Assert
    assertNotNull(result);
    assertEquals("AAPL", result.stockSymbol());
    assertEquals(OrderSide.BUY, result.side());
    // Verify interactions
    verify(stockService).getStockEntityBySymbol("AAPL");
    verify(orderRepository).save(any(Order.class));
}

    @Test
    void placeOrder_ValidSellOrderWithSufficientHoldings_ReturnsOrderResponse() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest("AAPL", OrderSide.SELL, 30);
        
        // Use the Entity search method and wrap in Optional
        when(stockService.getStockEntityBySymbol("AAPL")).thenReturn(Optional.of(testStock));
        when(orderRepository.findByStock_Symbol("AAPL")).thenReturn(List.of(buyOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(sellOrder);

        when(alphaVantageClient.getStockPrice("AAPL")).thenReturn(new BigDecimal("150.00"));

        // Act
        OrderResponse result = orderService.placeOrder(request);

        // Assert
        assertNotNull(result);
        assertEquals(OrderSide.SELL, result.side());
        assertEquals(30, result.volume());
        verify(stockService).getStockEntityBySymbol("AAPL");
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void placeOrder_NullStockSymbol_ThrowsException() {
        CreateOrderRequest request = new CreateOrderRequest(null, OrderSide.BUY, 100);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.placeOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_EmptyStockSymbol_ThrowsException() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest("", OrderSide.BUY, 100);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.placeOrder(request));
    }

    @Test
    void placeOrder_NullOrderSide_ThrowsInvalidOrderException() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest("AAPL", null, 100);

       // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.placeOrder(request));
    }

    @Test
    void placeOrder_NegativeVolume_ThrowsInvalidOrderException() {
         // Arrange
        CreateOrderRequest request = new CreateOrderRequest("AAPL", OrderSide.BUY, -2);

       // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.placeOrder(request));
    }

    @Test
    void placeOrder_ValidRequest_ReturnsOrderResponse(){
        CreateOrderRequest request = new CreateOrderRequest("AAPL", OrderSide.BUY, 20);
        BigDecimal mockedPrice = new BigDecimal(150.00);

        when(stockService.getStockEntityBySymbol("AAPL")).thenReturn(Optional.of(testStock));
    
        // Stub the API call
        when(alphaVantageClient.getStockPrice("AAPL")).thenReturn(mockedPrice);
        
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderResponse result = orderService.placeOrder(request);

        // Assert
        assertNotNull(result);
        assertEquals(mockedPrice, result.executedPrice());
        verify(alphaVantageClient).getStockPrice("AAPL");
    }

    @Test
    void placeOrder_ApiFailure_ThrowsInvalidOrderException() {
    // Arrange
    CreateOrderRequest request = new CreateOrderRequest("AAPL", OrderSide.BUY, 40);
    
    // Ensure the stock is found
    when(stockService.getStockEntityBySymbol("AAPL")).thenReturn(Optional.of(testStock));

    // Simulate API returning zero or error
    when(alphaVantageClient.getStockPrice("AAPL")).thenReturn(BigDecimal.ZERO);
    
    // Act & Assert
    assertThrows(InvalidOrderException.class, () -> orderService.placeOrder(request));
    }

    @Test
    void placeOrder_SellOrderWithInsufficientHoldings_ThrowsInsufficientHoldingsException() {
    CreateOrderRequest request = new CreateOrderRequest("AAPL", OrderSide.SELL, 200);
    
    when(stockService.getStockEntityBySymbol("AAPL")).thenReturn(Optional.of(testStock));
    
    // Mock 100 shares (Buying 100 means selling 200 should fail)
    when(orderRepository.findByStock_Symbol("AAPL")).thenReturn(List.of(buyOrder));

    // Act & Assert
    assertThrows(InsufficientHoldingsException.class, () -> orderService.placeOrder(request));
    verify(orderRepository, never()).save(any());  
    }

    @Test
    void placeOrder_NonExistentStock_ThrowsResourceNotFoundException() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest("INVALID", OrderSide.BUY, 100);
        // Mock the entity search returning empty
        when(stockService.getStockEntityBySymbol("INVALID")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.placeOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getAllOrders_ReturnsOrdersOrderedByTimestamp() {
        // Arrange
        when(orderRepository.findAllByOrderByTimestampDesc()).thenReturn(Arrays.asList(sellOrder, buyOrder));

        // Act
        List<OrderResponse> result = orderService.getAllOrders();

        // Assert
        assertEquals(2, result.size());
        assertEquals("AAPL", result.get(0).stockSymbol());
    }

    @Test
    void getOrderById_ExistingOrder_ReturnsOrderResponse() {
    // Arrange
    when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(buyOrder));

    // Act
    OrderResponse result = orderService.getOrderById(testOrderId);

    // Assert
    assertNotNull(result);
    assertEquals(testOrderId, result.id()); // Compare IDs instead of the whole object
    assertEquals("AAPL", result.stockSymbol());
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
    List<Order> appleOrders = Arrays.asList(buyOrder, sellOrder);
    when(orderRepository.findByStock_Symbol("AAPL")).thenReturn(appleOrders);

    // Act
    List<OrderResponse> result = orderService.getOrdersByStock("AAPL");

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("AAPL", result.get(0).stockSymbol());
    verify(orderRepository).findByStock_Symbol("AAPL");
}

    @Test
    void getOrdersBySide_ReturnsFilteredOrders() {
    // Arrange
    List<Order> buyOrders = Arrays.asList(buyOrder);
    when(orderRepository.findBySide(OrderSide.BUY)).thenReturn(buyOrders);

    // Act
    List<OrderResponse> result = orderService.getOrdersBySide(OrderSide.BUY);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    // Use record accessor side() instead of getSide()
    assertEquals(OrderSide.BUY, result.get(0).side());
    verify(orderRepository).findBySide(OrderSide.BUY);
}

    @Test
    void getCurrentPortfolio_ReturnsPortfolioResponseWithRealValues() {
    // Arrange
    Object[] portfolioData = {"AAPL", "Apple Inc.", "NASDAQ", "Technology", 70L};
    List<Object[]> portfolioResults = List.of(new Object[][]{portfolioData});
    
    BigDecimal currentPrice = new BigDecimal("150.00");
    // 70 shares * 150.00 = 10,500.00
    BigDecimal expectedTotalValue = new BigDecimal("10500.00");

    when(orderRepository.calculatePortfolioHoldings()).thenReturn(portfolioResults);
    
    // Mock the API call for the symbol in the portfolio
    when(alphaVantageClient.getStockPrice("AAPL")).thenReturn(currentPrice);

    // Act
    PortfolioResponse result = orderService.getCurrentPortfolio();

    // Assert
    assertNotNull(result);
    assertEquals(1, result.items().size());
    assertEquals("AAPL", result.items().get(0).stockSymbol());
    assertEquals(70, result.items().get(0).quantity());
    
    // Now we assert the actual calculated value instead of ZERO
    // Note: Use compareTo for BigDecimal assertions to ignore scale differences
    assertEquals(0, expectedTotalValue.compareTo(result.totalPortfolioValue()), 
        "Portfolio value should be quantity * current price");
}

    @Test
    void getCurrentHoldingsForStock_CalculatesCorrectHoldings() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest("AAPL", OrderSide.SELL, 50);
        List<Order> orders = Arrays.asList(buyOrder, sellOrder); // 100 - 30 = 70
        
        when(stockService.getStockEntityBySymbol("AAPL")).thenReturn(Optional.of(testStock));
        when(orderRepository.findByStock_Symbol("AAPL")).thenReturn(orders);
        when(orderRepository.save(any(Order.class))).thenReturn(sellOrder);

        when(alphaVantageClient.getStockPrice(anyString())).thenReturn(new BigDecimal("100.00"));

        // Act
        OrderResponse result = orderService.placeOrder(request);

        // Assert
        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }
}
