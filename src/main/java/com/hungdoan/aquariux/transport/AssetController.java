package com.hungdoan.aquariux.transport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungdoan.aquariux.dto.api.asset.AssetResponse;
import com.hungdoan.aquariux.model.Asset;
import com.hungdoan.aquariux.service.spec.AssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    private static final Logger LOG = LoggerFactory.getLogger(AssetController.class);

    private AssetService priceService;

    private ObjectMapper objectMapper;

    private MessageDigest hashingFunc;

    @Autowired
    public AssetController(AssetService priceService,
                           ObjectMapper objectMapper) throws NoSuchAlgorithmException {
        this.priceService = priceService;
        this.objectMapper = objectMapper;
        this.hashingFunc = MessageDigest.getInstance("SHA-256");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AssetResponse> getAssetsBalance(@RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch,
                                                          @PathVariable("userId") String userId) throws JsonProcessingException {
        Collection<Asset> assets = priceService.getAssets(userId);
        List<AssetResponse.AssetBalance> assetBalances = new LinkedList<>();
        for (Asset asset : assets) {
            assetBalances.add(new AssetResponse.AssetBalance(asset.getCryptoType(), asset.getBalance()));
        }
        AssetResponse assetResponse = new AssetResponse(userId, assetBalances);

        String json = objectMapper.writeValueAsString(assetResponse);
        String etag = generateETag(json);
        if (etag.equals(ifNoneMatch)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        LOG.info("test ne HUNG");
        return ResponseEntity.ok()
                .eTag(etag)
                .body(assetResponse);
    }

    private String generateETag(String data) {
        byte[] hash = hashingFunc.digest(data.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}