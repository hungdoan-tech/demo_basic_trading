package com.hungdoan.aquariux.data_access;

import com.hungdoan.aquariux.data_access.spec.AssetRepository;
import com.hungdoan.aquariux.exception.OptimisticLockingFailureException;
import com.hungdoan.aquariux.model.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class H2AssetRepository implements AssetRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2AssetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Asset getAsset(String userId, String cryptoType) {
        String sql = "SELECT * FROM asset WHERE user_id = ? AND crypto_type = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Double balance = rs.getDouble("balance");
            int version = rs.getInt("version");
            return new Asset(userId, cryptoType, balance, version);
        }, userId, cryptoType);
    }

    @Override
    public Map<String, Asset> getAssets(String userId, List<String> coins) {
        String sql = "SELECT * FROM asset WHERE user_id = ? AND crypto_type IN (";
        
        String placeholders = String.join(", ", Collections.nCopies(coins.size(), "?"));
        sql += placeholders + ")";

        Map<String, Asset> assetMap = new HashMap<>();

        Object[] params = new Object[coins.size() + 1];
        params[0] = userId;
        System.arraycopy(coins.toArray(), 0, params, 1, coins.size());

        jdbcTemplate.query(sql, (rs, rowNum) -> {
            Asset asset = new Asset(rs.getString("user_id"), rs.getString("crypto_type"),
                    rs.getDouble("balance"), rs.getInt("version"));
            assetMap.put(asset.getCryptoType(), asset);
            return asset;
        }, params);

        return assetMap;
    }

    @Transactional(rollbackFor = OptimisticLockingFailureException.class)
    @Override
    public void updateAssets(Collection<Asset> assets) throws OptimisticLockingFailureException {
        for (Asset asset : assets) {
            String sql = "UPDATE asset SET balance = ?, version = version + 1 WHERE user_id = ? AND crypto_type = ? AND version = ?";
            int updatedRows = jdbcTemplate.update(sql, asset.getBalance(), asset.getUserId(), asset.getCryptoType(), asset.getVersion());
            if (updatedRows == 0) {
                throw new OptimisticLockingFailureException("Asset with ID " + asset.getUserId() + asset.getCryptoType()
                        + " was updated by another transaction.");
            }
        }
    }
}
