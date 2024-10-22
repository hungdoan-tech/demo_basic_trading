package com.hungdoan.aquariux.dto.api.trade_history;

import java.time.ZonedDateTime;

public class TradeHistoryResponse {

    private String id;
    private String userId;
    private String cryptoPair;
    private String tradeType;
    private Double tradeAmount;
    private Double tradePrice;
    private ZonedDateTime tradeTimestamp;

    public TradeHistoryResponse(String id, String userId, String cryptoPair, String tradeType, Double tradeAmount, Double tradePrice, ZonedDateTime tradeTimestamp) {
        this.id = id;
        this.userId = userId;
        this.cryptoPair = cryptoPair;
        this.tradeType = tradeType;
        this.tradeAmount = tradeAmount;
        this.tradePrice = tradePrice;
        this.tradeTimestamp = tradeTimestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCryptoPair() {
        return cryptoPair;
    }

    public void setCryptoPair(String cryptoPair) {
        this.cryptoPair = cryptoPair;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public Double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(Double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public ZonedDateTime getTradeTimestamp() {
        return tradeTimestamp;
    }

    public void setTradeTimestamp(ZonedDateTime tradeTimestamp) {
        this.tradeTimestamp = tradeTimestamp;
    }
}
