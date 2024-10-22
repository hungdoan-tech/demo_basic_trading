package com.hungdoan.aquariux.data_access;

import com.hungdoan.aquariux.common.id_generator.IdGenerator;
import com.hungdoan.aquariux.dto.AggregatedPrice;
import com.hungdoan.aquariux.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class H2PriceRepositoryTest {

    private JdbcTemplate jdbcTemplate;
    private IdGenerator idGenerator;
    private H2PriceRepository priceRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        idGenerator = Mockito.mock(IdGenerator.class);
        priceRepository = new H2PriceRepository(jdbcTemplate, idGenerator);
    }

    @Test
    void testSaveAggregatedPrice() {
        Map<String, AggregatedPrice> cryptoPairToAggregatedPrice = new HashMap<>();
        AggregatedPrice aggregatedPrice = new AggregatedPrice("BTCUSDT", 50000.0, 51000.0);
        cryptoPairToAggregatedPrice.put("BTCUSDT", aggregatedPrice);

        when(idGenerator.getId()).thenReturn("price1");

        List<Price> prices = priceRepository.saveAggregatedPrice(cryptoPairToAggregatedPrice);

        assertEquals(1, prices.size());
        assertEquals("BTCUSDT", prices.get(0).getCryptoPair());
        assertEquals(50000.0, prices.get(0).getBidPrice());
        assertEquals(51000.0, prices.get(0).getAskPrice());
    }

    @Test
    void testFindPriceFound() {
        String cryptoPair = "BTCUSDT";
        Price expectedPrice = new Price("price1", cryptoPair, 50000.0, 51000.0, ZonedDateTime.now());

        when(jdbcTemplate.queryForObject(anyString(), any(), (RowMapper<Object>) any()))
                .thenReturn(expectedPrice);

        Optional<Price> result = priceRepository.findPrice(cryptoPair);

        assertTrue(result.isPresent());
        assertEquals(expectedPrice, result.get());
    }

    @Test
    void testFindPriceNotFound() {
        String cryptoPair = "BTCUSDT";

        when(jdbcTemplate.queryForObject(anyString(), any(), (RowMapper<Object>) any()))
                .thenThrow(new RuntimeException("No data found"));

        Optional<Price> result = priceRepository.findPrice(cryptoPair);

        assertFalse(result.isPresent());
    }
}
