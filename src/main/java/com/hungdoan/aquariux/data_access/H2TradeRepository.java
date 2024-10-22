package com.hungdoan.aquariux.data_access;

import com.hungdoan.aquariux.data_access.spec.TradeRepository;
import com.hungdoan.aquariux.dto.api.page.PageRequest;
import com.hungdoan.aquariux.model.Trade;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

@Repository
public class H2TradeRepository implements TradeRepository {

    private final JdbcTemplate jdbcTemplate;

    public H2TradeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Trade save(Trade trade) {
        String sql = "INSERT INTO Trade (id, user_id, crypto_pair, trade_type, trade_amount, trade_price) VALUES (?, ?, ?, ?, ?, ?)";

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
    public List<Trade> getAllTrades(String userId, PageRequest pageRequest) {
        String baseQuery = "SELECT * FROM Trade ";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        queryBuilder.append("WHERE user_id = ? ");

        String operator = pageRequest.getOrder().equalsIgnoreCase("asc") ? ">" : "<";
        if (pageRequest.getLastId() != null) {
            queryBuilder.append("AND id ").append(operator).append(" ? ");
        }

        queryBuilder.append("ORDER BY ")
                .append(pageRequest.getSort())
                .append(" ")
                .append(pageRequest.getOrder());

        if (!pageRequest.getSort().equals("id")) {
            queryBuilder.append(", id ").append(pageRequest.getOrder());
        }

        queryBuilder.append(" LIMIT ?");

        String query = queryBuilder.toString();

        List<Object> params = new LinkedList<>();
        params.add(userId);

        if (pageRequest.getLastId() != null) {
            params.add(pageRequest.getLastId());
        }

        params.add(pageRequest.getPageSize());

        return jdbcTemplate.query(query, params.toArray(), (rs, rowNum) -> new Trade(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getString("crypto_pair"),
                rs.getString("trade_type"),
                rs.getDouble("trade_amount"),
                rs.getDouble("trade_price"),
                rs.getTimestamp("trade_timestamp").toInstant().atZone(ZoneId.systemDefault())
        ));
    }

    @Override
    public int countTrades(String userId) {
        String sql = "SELECT COUNT(*) FROM Trade WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
    }
}
