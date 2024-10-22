package com.hungdoan.aquariux.service.spec;

public interface TradeService {
    void executeTrade(String userId, String cryptoPair, String tradeType, Double tradeAmount);
}
