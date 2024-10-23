package com.hungdoan.aquariux.transport;

import com.hungdoan.aquariux.common.extract.FieldsExtractor;
import com.hungdoan.aquariux.common.rate_limit.RateLimited;
import com.hungdoan.aquariux.dto.CustomUserDetails;
import com.hungdoan.aquariux.dto.api.page.Page;
import com.hungdoan.aquariux.dto.api.page.PageRequest;
import com.hungdoan.aquariux.dto.api.trade.TradeRequest;
import com.hungdoan.aquariux.dto.api.trade.TradeResponse;
import com.hungdoan.aquariux.dto.api.trade_history.TradeHistoryResponse;
import com.hungdoan.aquariux.model.Trade;
import com.hungdoan.aquariux.service.spec.TradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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

    @RateLimited(requestAmount = 50, inSeconds = 60)
    @PostMapping
    public ResponseEntity<TradeResponse> executeTrade(@Valid @RequestBody TradeRequest tradeRequest) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUser().getUserId();

        Trade trade = tradeService.executeTrade(userId, tradeRequest.getCryptoPair(),
                tradeRequest.getTradeType(), tradeRequest.getTradeAmount());
        TradeResponse tradeExecutedSuccessfully = new TradeResponse(trade.getId(), trade.getUserId(), trade.getCryptoPair(),
                trade.getTradeType(), trade.getTradeAmount(), trade.getTradePrice(), trade.getTradeTimestamp(),
                "Trade executed successfully");
        return ResponseEntity.ok(tradeExecutedSuccessfully);
    }

    @RateLimited(requestAmount = 100, inSeconds = 60)
    @GetMapping
    public ResponseEntity<Page<TradeHistoryResponse>> getTradeHistory(@RequestParam(value = "_order", required = false) Optional<String> order,
                                                                      @RequestParam(value = "_sort", required = false) Optional<String> sort,
                                                                      @RequestParam(value = "limit", required = false) Optional<Integer> pageSize,
                                                                      @RequestParam(value = "lastId", required = false) Optional<String> lastId) {

        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUser().getUserId();

        String actualSortField = sort.orElse("id");
        if (!fieldsExtractor.checkValidSortableField(actualSortField, Trade.class)) {
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