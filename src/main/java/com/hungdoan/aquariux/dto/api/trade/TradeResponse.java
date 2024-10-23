package com.hungdoan.aquariux.dto.api.trade;

import java.time.ZonedDateTime;

public class TradeResponse {

    private String tradeId;

    private String userId;

    private String cryptoPair;

    private String tradeType;

    private Double tradeAmount;

    private Double tradePrice;

    private ZonedDateTime tradeTimestamp;

    private String message;

    public TradeResponse() {

    }

    public TradeResponse(String tradeId, String userId, String cryptoPair, String tradeType, Double tradeAmount, Double tradePrice, ZonedDateTime tradeTimestamp, String message) {
        this.tradeId = tradeId;
        this.userId = userId;
        this.cryptoPair = cryptoPair;
        this.tradeType = tradeType;
        this.tradeAmount = tradeAmount;
        this.tradePrice = tradePrice;
        this.tradeTimestamp = tradeTimestamp;
        this.message = message;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
