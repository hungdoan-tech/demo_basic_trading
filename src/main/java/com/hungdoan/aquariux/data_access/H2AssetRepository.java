package com.hungdoan.aquariux.data_access;

import com.hungdoan.aquariux.data_access.spec.AssetRepository;
import com.hungdoan.aquariux.model.Asset;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class H2AssetRepository implements AssetRepository {

    private final JdbcTemplate jdbcTemplate;

    public H2AssetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Asset getAssetBalance(String userId, String cryptoType) {
        String sql = "SELECT * FROM Wallet WHERE user_id = ? AND crypto_type = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Double balance = rs.getDouble("balance");
            int version = rs.getInt("version");
            return new Asset(userId, cryptoType, balance, version);
        }, userId, cryptoType);
    }

    @Override
    public void updateAssetBalance(Asset asset) {
        String sql = "UPDATE Wallet SET balance = ?, version = version + 1 WHERE user_id = ? AND crypto_type = ? AND version = ?";
        int updatedRows = jdbcTemplate.update(sql, asset.getBalance(), asset.getUserId(), asset.getCryptoType(), asset.getVersion());
        if (updatedRows == 0) {
            throw new IllegalStateException("Failed to update wallet due to optimistic locking.");
        }
    }
}
