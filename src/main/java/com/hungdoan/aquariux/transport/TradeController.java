package com.hungdoan.aquariux.transport;

import com.hungdoan.aquariux.common.extract.FieldsExtractor;
import com.hungdoan.aquariux.dto.api.page.Page;
import com.hungdoan.aquariux.dto.api.page.PageRequest;
import com.hungdoan.aquariux.dto.api.trade.TradeRequest;
import com.hungdoan.aquariux.dto.api.trade.TradeResponse;
import com.hungdoan.aquariux.dto.api.trade_history.TradeHistoryResponse;
import com.hungdoan.aquariux.model.Trade;
import com.hungdoan.aquariux.service.spec.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/trades")
public class TradeController {

    private final TradeService tradeService;

    private final FieldsExtractor fieldsExtractor;

    @Autowired
    public TradeController(TradeService tradeService, FieldsExtractor fieldsExtractor) {
        this.tradeService = tradeService;
        this.fieldsExtractor = fieldsExtractor;
    }


    @PostMapping
    public ResponseEntity<TradeResponse> executeTrade(@RequestBody TradeRequest tradeRequest) {
        String tradeId = tradeService.executeTrade(tradeRequest.getUserId(), tradeRequest.getCryptoPair(),
                tradeRequest.getTradeType(), tradeRequest.getTradeAmount());
        return ResponseEntity.ok(new TradeResponse(tradeId, "Trade executed successfully"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<TradeHistoryResponse>> getTradeHistory(@PathVariable(value = "userId") String userId,
                                                                      @RequestParam(value = "_order", required = false) Optional<String> order,
                                                                      @RequestParam(value = "_sort", required = false) Optional<String> sort,
                                                                      @RequestParam(value = "limit", required = false) Optional<Integer> pageSize,
                                                                      @RequestParam(value = "lastId", required = false) Optional<String> lastId) {

        String actualSortField = sort.orElse("id");
        if (!fieldsExtractor.checkValidField(actualSortField, Trade.class)) {
            ResponseEntity.badRequest().body(String.format("The _sort %s is not match with the system", sort));
        }

        PageRequest pageRequest = new PageRequest(
                order.orElse("asc").equalsIgnoreCase("desc") ? "desc" : "asc",
                actualSortField,
                pageSize.orElse(12),
                lastId.orElse(null)
        );

        Page<TradeHistoryResponse> tradeHistoriesResponse = tradeService.fetchHistory(userId, pageRequest);
        return ResponseEntity.ok(tradeHistoriesResponse);
    }
}