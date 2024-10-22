package com.hungdoan.aquariux.model;

import java.time.ZonedDateTime;

public class Price {

    private String id;
    private String cryptoPair;
    private Double bidPrice;
    private Double askPrice;
    private ZonedDateTime timestamp;

    public Price(String id, String cryptoPair, Double bidPrice, Double askPrice, ZonedDateTime timestamp) {
        this.id = id;
        this.cryptoPair = cryptoPair.toUpperCase();
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getCryptoPair() {
        return cryptoPair;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public Double getAskPrice() {
        return askPrice;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
