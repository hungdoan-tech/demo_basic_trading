package com.hungdoan.aquariux.service;

import com.hungdoan.aquariux.data_access.spec.PriceRepository;
import com.hungdoan.aquariux.dto.AggregatedPrice;
import com.hungdoan.aquariux.dto.BinancePrice;
import com.hungdoan.aquariux.dto.HoubiPrice;
import com.hungdoan.aquariux.model.Price;
import com.hungdoan.aquariux.service.spec.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PriceAggregationService implements PriceService {

    private static final Logger LOG = LoggerFactory.getLogger(PriceAggregationService.class);

    private static final long cacheTTL = 10000; // TTL in milliseconds

    private final RestTemplate restTemplate;

    private final Set<String> validCryptoPairs;

    private final PriceRepository priceRepository;

    private final ConcurrentHashMap<String, CachedPrice> priceCache;

    @Autowired
    public PriceAggregationService(RestTemplate restTemplate, PriceRepository priceRepository) {
        this.restTemplate = restTemplate;
        this.priceRepository = priceRepository;
        this.validCryptoPairs = new HashSet<>();
        this.validCryptoPairs.add("ETHUSDT");
        this.validCryptoPairs.add("BTCUSDT");
        this.priceCache = new ConcurrentHashMap<>();
    }

    @Override
    public Map<String, Price> getPrices() {
        Map<String, Price> prices = new HashMap<>();

        // Loop through all crypto pairs we are tracking
        for (String cryptoPair : validCryptoPairs) {
            // Fetch the price from the cache or DB
            Optional<Price> priceOpt = getPrice(cryptoPair);
            if (priceOpt.isPresent()) {
                prices.put(cryptoPair, priceOpt.get());
            } else {
                LOG.warn("Price for {} could not be found in cache or DB", cryptoPair);
            }
        }

        return prices;
    }

    @Override
    public Optional<Price> getPrice(String cryptoPair) {
        CachedPrice cachedPrice = priceCache.get(cryptoPair.toUpperCase());
        if (cachedPrice != null && !cachedPrice.isExpired()) {
            return Optional.of(cachedPrice.getPrice());
        }

        Optional<Price> price = priceRepository.findPrice(cryptoPair);
        if (price.isPresent()) {
            return price;
        }

        LOG.error("Price not found in cache or DB for pair: {}", cryptoPair);
        scheduleUpdateAggregatedPrice();

        return Optional.empty();
    }


    @Scheduled(fixedRate = 10000)
    public List<Price> scheduleUpdateAggregatedPrice() {

        BinancePrice[] binancePrices = fetchBinancePrices();
        BinancePrice[] filteredBinancePrices = Arrays.stream(binancePrices)
                .filter(price -> validCryptoPairs.contains(price.getSymbol().toUpperCase()))
                .toArray(BinancePrice[]::new);

        HoubiPrice.HoubiTicker[] houbiPrices = fetchHuobiPrices();
        HoubiPrice.HoubiTicker[] filteredHoubiPrice = Arrays.stream(houbiPrices)
                .filter(price -> validCryptoPairs.contains(price.getSymbol().toUpperCase()))
                .toArray(HoubiPrice.HoubiTicker[]::new);

        Map<String, AggregatedPrice> bestPrices = getBestPrices(filteredBinancePrices, filteredHoubiPrice);
        List<Price> prices = priceRepository.saveAggregatedPrices(bestPrices.values());

        prices.forEach((price) -> priceCache.put(price.getCryptoPair(), new CachedPrice(price)));
        return prices;
    }

    private BinancePrice[] fetchBinancePrices() {
        try {
            String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker";
            ResponseEntity<BinancePrice[]> response = restTemplate.getForEntity(binanceUrl, BinancePrice[].class);

            if (response.getBody() == null) {
                LOG.error("Failed to fetch prices from Binance: the body is null");
                return new BinancePrice[0];
            }

            return response.getBody();
        } catch (Exception e) {
            LOG.error("Failed to fetch prices from Binance: " + e.getMessage());
            return new BinancePrice[0];
        }
    }

    private HoubiPrice.HoubiTicker[] fetchHuobiPrices() {
        try {
            String huobiUrl = "https://api.huobi.pro/market/tickers";
            ResponseEntity<HoubiPrice> response = restTemplate.getForEntity(huobiUrl, HoubiPrice.class);
            if (response.getBody() == null) {
                LOG.error("Failed to fetch prices from Houbi: the body is null");
                return new HoubiPrice.HoubiTicker[0];
            }

            return response.getBody().getData();

        } catch (Exception e) {
            LOG.error("Failed to fetch prices from Huobi: " + e.getMessage());
            return new HoubiPrice.HoubiTicker[0];
        }
    }

    private Map<String, AggregatedPrice> getBestPrices(BinancePrice[] binancePrices, HoubiPrice.HoubiTicker[] houbiPrices) {

        Map<String, AggregatedPrice> bestPrices = new HashMap<>();

        Map<String, BinancePrice> binancePriceMap = new HashMap<>();
        for (BinancePrice binancePrice : binancePrices) {
            binancePriceMap.put(binancePrice.getSymbol().toUpperCase(), binancePrice);
        }

        for (HoubiPrice.HoubiTicker houbiPrice : houbiPrices) {

            String symbol = houbiPrice.getSymbol().toUpperCase();

            BinancePrice binancePrice = binancePriceMap.get(symbol);

            if (binancePrice == null) {
                LOG.error("Binance does not contain the corresponding crypto symbol");
                continue;
            }

            double bestBid = Math.max(binancePrice.getBidPrice(), houbiPrice.getBid());

            double bestAsk = Math.min(binancePrice.getAskPrice(), houbiPrice.getAsk());

            bestPrices.put(symbol, new AggregatedPrice(symbol, bestBid, bestAsk));
        }

        return bestPrices;
    }

    public static class CachedPrice {
        private final Price price;
        private final long timestamp;

        public CachedPrice(Price price) {
            this.price = price;
            this.timestamp = System.currentTimeMillis();
        }

        public Price getPrice() {
            return price;
        }

        public long getTimestamp() {
            return timestamp;
        }

        private boolean isExpired() {
            return System.currentTimeMillis() - getTimestamp() > cacheTTL;
        }
    }
}
