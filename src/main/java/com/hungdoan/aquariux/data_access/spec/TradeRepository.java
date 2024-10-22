package com.hungdoan.aquariux.data_access.spec;

import com.hungdoan.aquariux.model.Trade;

import java.util.List;

public interface TradeRepository {

    Trade save(Trade trade);

    List<Trade> getAllTrades(String userId);
}
