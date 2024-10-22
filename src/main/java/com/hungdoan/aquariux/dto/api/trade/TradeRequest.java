package com.hungdoan.aquariux.dto.api.trade;

public class TradeRequest {
    private final String userId;
    private final String cryptoPair;
    private final String tradeType;
    private final Double tradeAmount;

    public TradeRequest(String userId, String cryptoPair, String tradeType, Double tradeAmount) {
        this.userId = userId;
        this.cryptoPair = cryptoPair;
        this.tradeType = tradeType;
        this.tradeAmount = tradeAmount;
    }

    public String getUserId() {
        return userId;
    }

    public String getCryptoPair() {
        return cryptoPair;
    }

    public String getTradeType() {
        return tradeType;
    }

    public Double getTradeAmount() {
        return tradeAmount;
    }
}
