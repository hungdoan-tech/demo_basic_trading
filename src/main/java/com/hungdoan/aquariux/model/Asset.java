package com.hungdoan.aquariux.model;

public class Asset {
    private String userId;
    private String cryptoType;
    private Double balance;
    private int version;

    public Asset(String userId, String cryptoType, Double balance, int version) {
        this.userId = userId;
        this.cryptoType = cryptoType;
        this.balance = balance;
        this.version = version;
    }

    public String getUserId() {
        return userId;
    }

    public String getCryptoType() {
        return cryptoType;
    }

    public Double getBalance() {
        return balance;
    }

    public int getVersion() {
        return version;
    }
}
