package com.hungdoan.aquariux.service.spec;

import com.hungdoan.aquariux.model.Price;

import java.util.Optional;

public interface PriceService {
    Optional<Price> getPrice(String cryptoPair);
}
