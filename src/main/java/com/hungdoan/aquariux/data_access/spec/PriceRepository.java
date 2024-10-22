package com.hungdoan.aquariux.data_access.spec;

import com.hungdoan.aquariux.dto.AggregatedPrice;
import com.hungdoan.aquariux.model.Price;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository {

    List<Price> saveAggregatedPrices(Collection<AggregatedPrice> cryptoPairToAggregatedPrice);

    Optional<Price> findPrice(String cryptoPair);
}
