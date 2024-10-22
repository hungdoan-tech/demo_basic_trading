package com.hungdoan.aquariux.service;

import com.hungdoan.aquariux.dto.AggregatedPrice;
import com.hungdoan.aquariux.dto.BinancePrice;
import com.hungdoan.aquariux.dto.HoubiPrice;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class PriceAggregationService {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(PriceAggregationService.class);

    private final RestTemplate restTemplate;

    private final Set<String> cryptoPairs;

    @Autowired
    public PriceAggregationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.cryptoPairs = new HashSet<>();
        this.cryptoPairs.add("ETHUSDT");
        this.cryptoPairs.add("BTCUSDT");
    }

    @Scheduled(fixedRate = 10000)
    public void aggregatePrices() {

        BinancePrice[] binancePrices = fetchBinancePrices();

        HoubiPrice.HoubiTicker[] houbiPrices = fetchHuobiPrices();

        Map<String, AggregatedPrice> bestPrices = getBestPrices(binancePrices, houbiPrices);

        saveBestPrices(bestPrices.values());
    }

    private BinancePrice[] fetchBinancePrices() {
        try {
            String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker";
            ResponseEntity<BinancePrice[]> response = restTemplate.getForEntity(binanceUrl, BinancePrice[].class);

            if (response.getBody() == null) {
                System.out.println("Failed to fetch prices from Binance: the body is null");
                return new BinancePrice[0];
            }

            return Arrays.stream(response.getBody())
                    .filter(price -> cryptoPairs.contains(price.getSymbol().toUpperCase()))
                    .toArray(BinancePrice[]::new);
        } catch (Exception e) {
            System.out.println("Failed to fetch prices from Binance: " + e.getMessage());
            return new BinancePrice[0];
        }
    }

    private HoubiPrice.HoubiTicker[] fetchHuobiPrices() {
        try {
            String huobiUrl = "https://api.huobi.pro/market/tickers";
            ResponseEntity<HoubiPrice> response = restTemplate.getForEntity(huobiUrl, HoubiPrice.class);
            HoubiPrice.HoubiTicker[] data = response.getBody().getData();

            return Arrays.stream(data)
                    .filter(price -> cryptoPairs.contains(price.getSymbol().toUpperCase()))
                    .toArray(HoubiPrice.HoubiTicker[]::new);
        } catch (Exception e) {
            System.out.println("Failed to fetch prices from Huobi: " + e.getMessage());
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

    private void saveBestPrices(Collection<AggregatedPrice> bestPrices) {
        //TODO
    }
}
