package com.hungdoan.aquariux.dto.api.trade;

import com.hungdoan.aquariux.common.validation.CryptoPairValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TradeRequest {

    @NotBlank(message = "Crypto pair is required")
    @CryptoPairValid
    private String cryptoPair;

    @NotBlank(message = "Trade type is required")
    @Pattern(regexp = "BUY|SELL", message = "Trade type must be either 'BUY' or 'SELL'")
    private String tradeType;

    @Min(value = 0, message = "Trade amount must be greater than 0")
    private Double tradeAmount;

    public TradeRequest() {
    }

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

    public void setCryptoPair(@NotBlank(message = "Crypto pair is required") String cryptoPair) {
        this.cryptoPair = cryptoPair;
    }

    public void setTradeType(@NotBlank(message = "Trade type is required") @Pattern(regexp = "BUY|SELL", message = "Trade type must be either 'BUY' or 'SELL'") String tradeType) {
        this.tradeType = tradeType;
    }

    public void setTradeAmount(@Min(value = 0, message = "Trade amount must be greater than 0") Double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }
}
