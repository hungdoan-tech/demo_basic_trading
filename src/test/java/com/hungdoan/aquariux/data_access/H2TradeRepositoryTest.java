package com.hungdoan.aquariux.data_access;

import com.hungdoan.aquariux.data_access.spec.TradeRepository;
import com.hungdoan.aquariux.dto.api.page.PageRequest;
import com.hungdoan.aquariux.model.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class H2TradeRepositoryTest {

    private JdbcTemplate jdbcTemplate;
    private TradeRepository tradeRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        tradeRepository = new H2TradeRepository(jdbcTemplate);
    }

    @Test
    void testGetAllTrades() {
        // Arrange
        String userId = "user1";
        PageRequest pageRequest = new PageRequest("asc", "trade_timestamp", 10, null);
        Trade trade = new Trade("1", userId, "ETH/USD", "buy", 1.0, 3000.0, ZonedDateTime.now());

        when(jdbcTemplate.query(anyString(), (Object[]) any(), any(RowMapper.class))).thenReturn(List.of(trade));

        // Act
        List<Trade> trades = tradeRepository.getAllTrades(userId, pageRequest);

        // Assert
        assertNotNull(trades);
        assertEquals(1, trades.size());
        assertEquals(trade.getId(), trades.get(0).getId());

        // Verify the query method was called
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).query(sqlCaptor.capture(), (Object[]) any(), any(RowMapper.class));

        String expectedSql = "SELECT * FROM Trade WHERE user_id = ? ORDER BY trade_timestamp asc, id asc LIMIT ?";
        assertTrue(sqlCaptor.getValue().contains(expectedSql));
    }

    @Test
    void testCountTrades() {
        // Arrange
        String userId = "user1";
        when(jdbcTemplate.queryForObject(anyString(), any(), eq(Integer.class))).thenReturn(5);

        // Act
        int count = tradeRepository.countTrades(userId);

        // Assert
        assertEquals(5, count);

        // Verify that the count query was called
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).queryForObject(sqlCaptor.capture(), any(), eq(Integer.class));
        assertEquals("SELECT COUNT(*) FROM Trade WHERE user_id = ?", sqlCaptor.getValue());
    }
}
