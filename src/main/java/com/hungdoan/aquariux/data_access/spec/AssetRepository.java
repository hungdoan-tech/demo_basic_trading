package com.hungdoan.aquariux.data_access.spec;

import com.hungdoan.aquariux.exception.OptimisticLockingFailureException;
import com.hungdoan.aquariux.model.Asset;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AssetRepository {

    Asset getAsset(String userId, String cryptoType);

    Map<String, Asset> getAssets(String userId, List<String> coins);

    @Transactional(rollbackFor = OptimisticLockingFailureException.class)
    void updateAssets(Collection<Asset> assets) throws OptimisticLockingFailureException;
}
