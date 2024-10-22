package com.hungdoan.aquariux.dto.api.trade_history;

import java.time.ZonedDateTime;

public class TradeHistoryResponse {

    private final String id;
    private final String userId;
    private final String cryptoPair;
    private final String tradeType;
    private final Double tradeAmount;
    private final Double tradePrice;
    private final ZonedDateTime tradeTimestamp;

    public TradeHistoryResponse(String id, String userId, String cryptoPair, String tradeType, Double tradeAmount, Double tradePrice, ZonedDateTime tradeTimestamp) {
        this.id = id;
        this.userId = userId;
        this.cryptoPair = cryptoPair;
        this.tradeType = tradeType;
        this.tradeAmount = tradeAmount;
        this.tradePrice = tradePrice;
        this.tradeTimestamp = tradeTimestamp;
    }
}
