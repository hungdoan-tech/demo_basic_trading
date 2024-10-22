package com.hungdoan.aquariux.dto.api.trade;

import com.hungdoan.aquariux.common.validation.CryptoPairValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TradeRequest {

    @NotBlank(message = "Crypto pair is required")
    @CryptoPairValid
    private final String cryptoPair;

    @NotBlank(message = "Trade type is required")
    @Pattern(regexp = "BUY|SELL", message = "Trade type must be either 'BUY' or 'SELL'")
    private final String tradeType;

    @Min(value = 0, message = "Trade amount must be greater than 0")
    private final Double tradeAmount;


    public TradeRequest(String cryptoPair, String tradeType, Double tradeAmount) {
        this.cryptoPair = cryptoPair;
        this.tradeType = tradeType;
        this.tradeAmount = tradeAmount;
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
