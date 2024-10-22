package com.hungdoan.aquariux.data_access;

import com.hungdoan.aquariux.data_access.spec.TradeRepository;
import com.hungdoan.aquariux.model.Trade;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.ZoneId;
import java.util.List;

public class H2TradeRepository implements TradeRepository {

    private final JdbcTemplate jdbcTemplate;

    public H2TradeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Trade save(Trade trade) {
        String sql = "INSERT INTO Trade (trade_id, user_id, crypto_pair, trade_type, trade_amount, trade_price) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, ps -> {
            ps.setString(1, trade.getId());
            ps.setString(2, trade.getUserId());
            ps.setString(3, trade.getCryptoPair());
            ps.setString(4, trade.getTradeType());
            ps.setDouble(5, trade.getTradeAmount());
            ps.setDouble(6, trade.getTradePrice());
        });

        return trade;
    }

    @Override
    public List<Trade> getAllTrades(String userId) {
        String sql = "SELECT trade_id, user_id, crypto_pair, trade_type, trade_amount, trade_price, trade_timestamp " +
                "FROM Trade WHERE user_id = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            Trade trade = new Trade(rs.getString("trade_id"),
                    rs.getString("user_id"),
                    rs.getString("crypto_pair"),
                    rs.getString("trade_type"),
                    rs.getDouble("trade_amount"),
                    rs.getDouble("trade_price"),
                    rs.getTimestamp("trade_timestamp").toInstant().atZone(ZoneId.systemDefault())
            );

            return trade;
        });
    }
}
