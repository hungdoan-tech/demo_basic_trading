package com.hungdoan.aquariux.dto;

public class HoubiPrice {

    private HoubiTicker[] data;

    private String status;

    private long ts;

    public HoubiPrice(HoubiTicker[] data, String status, long ts) {
        this.data = data;
        this.status = status;
        this.ts = ts;
    }

    public HoubiTicker[] getData() {
        return data;
    }

    public static class HoubiTicker {
        private String symbol;
        private Double bid;
        private Double bidSize;
        private Double ask;
        private Double askSize;
        private Double open;
        private Double high;
        private Double low;
        private Double close;
        private Double amount;
        private Double vol;
        private Double count;

        private HoubiTicker() {
        }

        public HoubiTicker(String symbol, Double bid, Double ask) {
            this.symbol = symbol;
            this.bid = bid;
            this.ask = ask;
        }

        public HoubiTicker(String symbol, Double bid, Double bidSize, Double ask, Double askSize, Double open,
                           Double high, Double low, Double close, Double amount, Double vol, Double count) {
            this.symbol = symbol;
            this.bid = bid;
            this.bidSize = bidSize;
            this.ask = ask;
            this.askSize = askSize;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.amount = amount;
            this.vol = vol;
            this.count = count;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public Double getBid() {
            return bid;
        }

        public void setBid(Double bid) {
            this.bid = bid;
        }

        public Double getAsk() {
            return ask;
        }

        public void setAsk(Double ask) {
            this.ask = ask;
        }
    }
}
