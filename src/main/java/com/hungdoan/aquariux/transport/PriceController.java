package com.hungdoan.aquariux.transport;

import com.hungdoan.aquariux.model.Price;
import com.hungdoan.aquariux.service.spec.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {

    private PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/{cryptoPair}")
    public ResponseEntity<Price> getLatestPrice(@PathVariable String cryptoPair) {
        Optional<Price> price = priceService.getPrice(cryptoPair);
        // 404 when empty
        return ResponseEntity.of(price);
    }
}
