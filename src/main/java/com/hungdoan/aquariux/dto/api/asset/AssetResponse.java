package com.hungdoan.aquariux.dto.api.asset;

import java.util.List;

public class AssetResponse {

    private String userId;

    private List<AssetBalance> assetBalances;

    public AssetResponse(String userId, List<AssetBalance> assetBalances) {
        this.userId = userId;
        this.assetBalances = assetBalances;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<AssetBalance> getAssetBalances() {
        return assetBalances;
    }

    public void setAssetBalances(List<AssetBalance> assetBalances) {
        this.assetBalances = assetBalances;
    }

    public static class AssetBalance {
        private String cryptoType;
        private Double balance;

        public AssetBalance(String cryptoType, Double balance) {
            this.cryptoType = cryptoType;
            this.balance = balance;
        }

        public String getCryptoType() {
            return cryptoType;
        }

        public void setCryptoType(String cryptoType) {
            this.cryptoType = cryptoType;
        }

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }
    }

}
