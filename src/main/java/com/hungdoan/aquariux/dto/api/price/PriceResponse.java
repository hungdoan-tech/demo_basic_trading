package com.hungdoan.aquariux.dto.api.price;

import java.time.ZonedDateTime;

public class PriceResponse {
    private String cryptoPair;
    private Double bidPrice;
    private Double askPrice;
    private ZonedDateTime timestamp;

    public PriceResponse() {

    }

    public PriceResponse(String cryptoPair, Double bidPrice, Double askPrice, ZonedDateTime timestamp) {
        this.cryptoPair = cryptoPair;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.timestamp = timestamp;
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

    public void setCryptoPair(String cryptoPair) {
        this.cryptoPair = cryptoPair;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public void setAskPrice(Double askPrice) {
        this.askPrice = askPrice;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
