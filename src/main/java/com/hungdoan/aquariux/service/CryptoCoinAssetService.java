package com.hungdoan.aquariux.service;

import com.hungdoan.aquariux.data_access.spec.AssetRepository;
import com.hungdoan.aquariux.model.Asset;
import com.hungdoan.aquariux.service.spec.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import static com.hungdoan.aquariux.common.validation.CryptoPairValidator.VALID_CRYPTO_QUOTES;

@Service
public class CryptoCoinAssetService implements AssetService {

    private AssetRepository assetRepository;

    @Autowired
    public CryptoCoinAssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public Collection<Asset> getAssets(String userId) {
        Map<String, Asset> assets = assetRepository.getAssets(userId, new LinkedList<>(VALID_CRYPTO_QUOTES));
        return assets.values();
    }
}
