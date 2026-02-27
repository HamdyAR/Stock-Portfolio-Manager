package com.cbfacademy.stockportfoliomanager.client;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AlphaVantageClient {
   private final RestClient restClient;
   private final String apiKey;
   private final String baseUrl;


   public AlphaVantageClient(RestClient restClient, 
                            @Value("${alphavantage.api.key}") String apiKey, 
                            @Value("${alphavantage.base.url}") String baseUrl){
         this.restClient = restClient;
         this.apiKey = apiKey;
         this.baseUrl = baseUrl;
   }

   public BigDecimal getStockPrice(String symbol){
         try{
            AlphaVantageResponse response = restClient.get()
                    .uri(baseUrl + "/query?function=GLOBAL_QUOTE&symbol={symbol}&apikey={apiKey}", symbol, apiKey)
                    .retrieve()
                    .body(AlphaVantageResponse.class);

            if(response != null & response.globalQuote() != null){
                String priceStr = response.globalQuote().price();
                return new BigDecimal(priceStr);
            }       
         }catch(Exception e){
            return BigDecimal.ZERO;
         }

         return BigDecimal.ZERO;
   }
}
