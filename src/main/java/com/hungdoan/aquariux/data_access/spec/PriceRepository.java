package com.hungdoan.aquariux.data_access.spec;

import com.hungdoan.aquariux.dto.AggregatedPrice;
import com.hungdoan.aquariux.model.Price;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PriceRepository {

    List<Price> saveAggregatedPrice(Map<String, AggregatedPrice> cryptoPairToAggregatedPrice);

    Optional<Price> findPrice(String cryptoPair);
}
