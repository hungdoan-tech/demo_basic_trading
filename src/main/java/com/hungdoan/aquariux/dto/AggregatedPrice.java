package com.hungdoan.aquariux.dto;

public class AggregatedPrice {

    private final String symbol;
    private final double bestBid;
    private final double bestAsk;

    public AggregatedPrice(String symbol, double bestBid, double bestAsk) {
        this.symbol = symbol;
        this.bestBid = bestBid;
        this.bestAsk = bestAsk;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getBestBid() {
        return bestBid;
    }

    public double getBestAsk() {
        return bestAsk;
    }
}
