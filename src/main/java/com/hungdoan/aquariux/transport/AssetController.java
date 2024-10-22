package com.hungdoan.aquariux.transport;

import com.hungdoan.aquariux.dto.api.asset.AssetResponse;
import com.hungdoan.aquariux.model.Asset;
import com.hungdoan.aquariux.service.spec.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    private AssetService priceService;

    @Autowired
    public AssetController(AssetService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AssetResponse> getAssesBalances(@PathVariable("userId") String userId) {
        Collection<Asset> assets = priceService.getAssets(userId);
        List<AssetResponse.AssetBalance> assetBalances = new LinkedList<>();
        for (Asset asset : assets) {
            assetBalances.add(new AssetResponse.AssetBalance(asset.getCryptoType(), asset.getBalance()));
        }
        return ResponseEntity.ok(new AssetResponse(userId, assetBalances));
    }
}