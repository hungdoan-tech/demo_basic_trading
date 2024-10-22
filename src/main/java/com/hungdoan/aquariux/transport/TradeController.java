package com.hungdoan.aquariux.transport;

import com.hungdoan.aquariux.dto.api.trade.TradeRequest;
import com.hungdoan.aquariux.dto.api.trade.TradeResponse;
import com.hungdoan.aquariux.service.spec.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trades")
public class TradeController {

    private final TradeService tradeService;

    @Autowired
    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping
    public ResponseEntity<TradeResponse> executeTrade(@RequestBody TradeRequest tradeRequest) {
        String tradeId = tradeService.executeTrade(tradeRequest.getUserId(), tradeRequest.getCryptoPair(),
                tradeRequest.getTradeType(), tradeRequest.getTradeAmount());
        return ResponseEntity.ok(new TradeResponse(tradeId, "Trade executed successfully"));
    }
}