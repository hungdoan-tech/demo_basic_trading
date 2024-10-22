package com.hungdoan.aquariux.service.spec;

public interface TradeService {
    String executeTrade(String userId, String cryptoPair, String tradeType, Double tradeAmount);
}
