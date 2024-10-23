package com.hungdoan.aquariux.model;

import com.hungdoan.aquariux.common.validation.Sortable;

import java.time.ZonedDateTime;

public class Trade {

    @Sortable
    private final String id;
    
    private final String userId;

    @Sortable
    private final String cryptoPair;

    @Sortable
    private final String tradeType;

    @Sortable
    private final Double tradeAmount;

    @Sortable
    private final Double tradePrice;

    @Sortable
    private final ZonedDateTime tradeTimestamp;

    public Trade(String id, String userId, String cryptoPair, String tradeType, Double tradeAmount, Double tradePrice, ZonedDateTime tradeTimestamp) {
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

    public Double getTradePrice() {
        return tradePrice;
    }

    public ZonedDateTime getTradeTimestamp() {
        return tradeTimestamp;
    }
}