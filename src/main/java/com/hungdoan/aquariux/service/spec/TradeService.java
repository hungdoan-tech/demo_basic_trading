package com.hungdoan.aquariux.service.spec;

import com.hungdoan.aquariux.dto.api.page.Page;
import com.hungdoan.aquariux.dto.api.page.PageRequest;
import com.hungdoan.aquariux.dto.api.trade_history.TradeHistoryResponse;

public interface TradeService {
    String executeTrade(String userId, String cryptoPair, String tradeType, Double tradeAmount);

    Page<TradeHistoryResponse> fetchHistory(String userId, PageRequest pageRequest);
}
