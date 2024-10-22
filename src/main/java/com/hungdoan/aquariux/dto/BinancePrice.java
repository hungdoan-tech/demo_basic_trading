package com.hungdoan.aquariux.dto;

public class BinancePrice {

    private String symbol;
    private Double bidPrice;
    private Double askPrice;

    public BinancePrice(String symbol, Double bidPrice, Double askPrice) {
        this.symbol = symbol;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public Double getAskPrice() {
        return askPrice;
    }
}
