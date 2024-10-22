package com.hungdoan.aquariux.dto.api.trade;

public class TradeResponse {

    private final String tradeId;

    private final String message;

    public TradeResponse(String tradeId, String message) {
        this.tradeId = tradeId;
        this.message = message;
    }

    public String getTradeId() {
        return tradeId;
    }

    public String getMessage() {
        return message;
    }
}
