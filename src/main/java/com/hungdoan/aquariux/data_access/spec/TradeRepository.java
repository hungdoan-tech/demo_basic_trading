package com.hungdoan.aquariux.data_access.spec;

import com.hungdoan.aquariux.dto.api.page.PageRequest;
import com.hungdoan.aquariux.model.Trade;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository {

    Trade save(Trade trade);

    List<Trade> getAllTrades(String userId, PageRequest pageRequest);

    int countTrades(String userId);
}
