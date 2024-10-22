package com.hungdoan.aquariux.data_access;

import com.hungdoan.aquariux.common.id_generator.IdGenerator;
import com.hungdoan.aquariux.data_access.spec.PriceRepository;
import com.hungdoan.aquariux.dto.AggregatedPrice;
import com.hungdoan.aquariux.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class H2PriceRepository implements PriceRepository {

    private final JdbcTemplate jdbcTemplate;

    private final IdGenerator idGenerator;

    @Autowired
    public H2PriceRepository(JdbcTemplate jdbcTemplate, IdGenerator idGenerator) {
        this.jdbcTemplate = jdbcTemplate;
        this.idGenerator = idGenerator;
    }

    public List<Price> saveAggregatedPrices(Collection<AggregatedPrice> aggregatedPrices) {
        String sql = "INSERT INTO Price (price_id, crypto_pair, bid_price, ask_price, timestamp) " +
                "VALUES (?, ?, ?, ?, ?)";

        List<Price> prices = new LinkedList<>();
        for (AggregatedPrice price : aggregatedPrices) {

            String id = idGenerator.getId();
            ZonedDateTime now = ZonedDateTime.now();

            jdbcTemplate.update(sql,
                    id,
                    price.getSymbol(),
                    price.getBestBid(),
                    price.getBestAsk(),
                    now
            );

            prices.add(new Price(id, price.getSymbol(), price.getBestBid(), price.getBestAsk(), now));
        }

        return prices;
    }

    @Override
    public Optional<Price> findPrice(String cryptoPair) {
        String sql = "SELECT * FROM Price WHERE crypto_pair = ? ORDER BY timestamp DESC LIMIT 1";

        try {

            Price price = jdbcTemplate.queryForObject(sql, new Object[]{cryptoPair}, (rs, rowNum) -> {
                String id = rs.getString("price_id");
                double bidPrice = rs.getDouble("bid_price");
                double askPrice = rs.getDouble("ask_price");
                ZonedDateTime timestamp = rs.getTimestamp("timestamp").toInstant().atZone(ZoneId.systemDefault());

                return new Price(id, cryptoPair, bidPrice, askPrice, timestamp);
            });

            return Optional.of(price);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
