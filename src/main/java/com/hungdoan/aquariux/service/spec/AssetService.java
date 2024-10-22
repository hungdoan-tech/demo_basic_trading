package com.hungdoan.aquariux.service.spec;

import com.hungdoan.aquariux.model.Asset;

import java.util.Collection;

public interface AssetService {

    Collection<Asset> getAssets(String userId);
}
