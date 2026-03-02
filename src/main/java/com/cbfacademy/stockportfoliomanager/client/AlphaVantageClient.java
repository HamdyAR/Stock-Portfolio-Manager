package com.cbfacademy.stockportfoliomanager.client;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.cbfacademy.stockportfoliomanager.stock.Stock;
import com.cbfacademy.stockportfoliomanager.stock.StockRepository;

@Component
public class AlphaVantageClient {
   private final RestClient restClient;
   private final String apiKey;
   private final String baseUrl;
   private static final Logger logger = LoggerFactory.getLogger(AlphaVantageClient.class);
   private final StockRepository stockRepository;


   public AlphaVantageClient(RestClient restClient, 
                            @Value("${alphavantage.api.key}") String apiKey, 
                            @Value("${alphavantage.base.url}") String baseUrl,
                           StockRepository stockRepository){
         this.restClient = restClient;
         this.apiKey = apiKey;
         this.baseUrl = baseUrl;
         this.stockRepository = stockRepository;
   }
   

   @Cacheable(value = "stockPrices", key = "#symbol", unless = "#result == null || #result.compareTo(T(java.math.BigDecimal).ZERO) == 0")
   public BigDecimal getStockPrice(String symbol){
         try{
            logger.info("Attempting to fetch live price for {}", symbol);
            AlphaVantageResponse response = restClient.get()
                    .uri(baseUrl + "?function=GLOBAL_QUOTE&symbol={symbol}&apikey={apiKey}", symbol, apiKey)
                    .retrieve()
                    .body(AlphaVantageResponse.class);

            if(response != null & response.globalQuote() != null){
                String priceStr = response.globalQuote().price();
                return new BigDecimal(priceStr);
            } 
            
      

            throw new RuntimeException("Empty response from Alpha Vantage");
         }catch(Exception e){
            logger.error("API Error for {}: {}. Falling back to database price.", symbol, e.getMessage());

            // FALLBACK: Get the last price stored in our DB
            return stockRepository.findBySymbol(symbol)
                    .map(Stock::getCurrentPrice)
                    .orElse(BigDecimal.ZERO);
         }
   }
}
