package com.hungdoan.aquariux.service.spec;

import com.hungdoan.aquariux.model.Price;

import java.util.Map;
import java.util.Optional;

public interface PriceService {
    Map<String, Price> getPrices();

    Optional<Price> getPrice(String cryptoPair);
}
