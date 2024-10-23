package com.hungdoan.aquariux.transport;

import com.hungdoan.aquariux.common.rate_limit.RateLimited;
import com.hungdoan.aquariux.common.validation.CryptoPairValid;
import com.hungdoan.aquariux.dto.api.price.PriceResponse;
import com.hungdoan.aquariux.model.Price;
import com.hungdoan.aquariux.service.spec.PriceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @RateLimited(requestAmount = 50, inSeconds = 5)
    @GetMapping
    public ResponseEntity<List<PriceResponse>> getLatestPrices() {
        Map<String, Price> prices = priceService.getPrices();
        List<PriceResponse> priceResponses = new LinkedList<>();
        for (Price price : prices.values()) {
            priceResponses.add(new PriceResponse(price.getCryptoPair(), price.getBidPrice(), price.getAskPrice(), price.getTimestamp()));
        }
        return ResponseEntity.ok(priceResponses);
    }

    @RateLimited(requestAmount = 50, inSeconds = 60)
    @GetMapping("/{cryptoPair}")
    public ResponseEntity<PriceResponse> getLatestPrice(@Valid @CryptoPairValid @PathVariable("cryptoPair") String cryptoPair) {
        Optional<Price> optionalPrice = priceService.getPrice(cryptoPair);
        if (optionalPrice.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Price price = optionalPrice.get();
        return ResponseEntity.ok(new PriceResponse(price.getCryptoPair(), price.getBidPrice(), price.getAskPrice(), price.getTimestamp()));
    }
}
