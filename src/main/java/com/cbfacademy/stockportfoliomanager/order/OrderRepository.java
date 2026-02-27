package com.cbfacademy.stockportfoliomanager.order;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends ListCrudRepository<Order, UUID> {
    
    // Filtering methods
    List<Order> findBySide(OrderSide side);
    List<Order> findByStock_Symbol(String symbol);
    
    // Portfolio aggregation custom query with JPQL
    @Query("SELECT o.stock.symbol, o.stock.companyName, o.stock.exchange, o.stock.industry, " +
           "SUM(CASE WHEN o.side = 'BUY' THEN o.volume ELSE -o.volume END) as holdings " +
           "FROM Order o " +
           "GROUP BY o.stock.symbol, o.stock.companyName, o.stock.exchange, o.stock.industry " +
           "HAVING SUM(CASE WHEN o.side = 'BUY' THEN o.volume ELSE -o.volume END) > 0")
    List<Object[]> calculatePortfolioHoldings();
    
    // Order history with most recent first
    List<Order> findAllByOrderByTimestampDesc();
    
}
