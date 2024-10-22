package com.hungdoan.aquariux.data_access.spec;

import com.hungdoan.aquariux.model.Asset;

public interface AssetRepository {

    Asset getAssetBalance(String userId, String cryptoType);

    void updateAssetBalance(Asset asset);
}
