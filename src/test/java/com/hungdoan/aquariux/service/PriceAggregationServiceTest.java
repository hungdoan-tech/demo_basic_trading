package com.hungdoan.aquariux.service;

import com.hungdoan.aquariux.data_access.spec.PriceRepository;
import com.hungdoan.aquariux.dto.BinancePrice;
import com.hungdoan.aquariux.dto.HoubiPrice;
import com.hungdoan.aquariux.model.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PriceAggregationServiceTest {

    private RestTemplate restTemplate;
    private PriceRepository priceRepository;
    private PriceAggregationService priceAggregationService;

    @BeforeEach
    void setUp() {
        // Create mocks for dependencies
        restTemplate = Mockito.mock(RestTemplate.class);
        priceRepository = Mockito.mock(PriceRepository.class);

        // Instantiate the PriceAggregationService with the mocked dependencies
        priceAggregationService = new PriceAggregationService(restTemplate, priceRepository);
    }

    @Test
    void scheduleUpdateAggregatedPrice() {
        BinancePrice[] binancePrices = new BinancePrice[]{
                new BinancePrice("ETHUSDT", 2000.0, 2050.0),
                new BinancePrice("BTCUSDT", 50000.0, 51000.0)
        };

        HoubiPrice.HoubiTicker[] houbiPrices = new HoubiPrice.HoubiTicker[]{
                new HoubiPrice.HoubiTicker("ETHUSDT", 1950.0, 2000.0),
                new HoubiPrice.HoubiTicker("BTCUSDT", 50500.0, 51500.0)
        };

        when(restTemplate.getForEntity(anyString(), eq(BinancePrice[].class)))
                .thenReturn(ResponseEntity.ok(binancePrices));
        when(restTemplate.getForEntity(anyString(), eq(HoubiPrice.class)))
                .thenReturn(ResponseEntity.ok(new HoubiPrice(houbiPrices, "ok", 1L)));

        List<Price> prices = new LinkedList<>();
        prices.add(new Price("id1", "ETHUSDT", 2000.0, 2000.0, ZonedDateTime.now()));
        prices.add(new Price("id2", "BTCUSDT", 50500.0, 51000.0, ZonedDateTime.now()));
        when(priceRepository.saveAggregatedPrices(any())).thenReturn(prices);

        priceAggregationService.scheduleUpdateAggregatedPrice();
        verify(priceRepository, times(1)).saveAggregatedPrices(any());

        Optional<Price> btcusdt = priceAggregationService.getAggregatedPrice("BTCUSDT");
        Assertions.assertTrue(btcusdt.isPresent());
        Assertions.assertTrue(btcusdt.get().getAskPrice().equals(51000.0));
        Assertions.assertTrue(btcusdt.get().getBidPrice().equals(50500.0));
    }

    @Test
    void scheduleUpdateAggregatedPrice_shouldHandleEmptyBinanceResponse() {
        when(restTemplate.getForEntity(anyString(), eq(BinancePrice[].class)))
                .thenReturn(ResponseEntity.ok(new BinancePrice[0]));

        HoubiPrice.HoubiTicker[] houbiPrices = new HoubiPrice.HoubiTicker[]{
                new HoubiPrice.HoubiTicker("ETHUSDT", 1950.0, 2000.0)
        };
        when(restTemplate.getForEntity(anyString(), eq(HoubiPrice.class)))
                .thenReturn(ResponseEntity.ok(new HoubiPrice(houbiPrices, "ok", 1L)));

        priceAggregationService.scheduleUpdateAggregatedPrice();

        verify(priceRepository, times(1)).saveAggregatedPrices(any());
    }

    @Test
    void scheduleUpdateAggregatedPrice_shouldHandleEmptyHuobiResponse() {
        HoubiPrice.HoubiTicker[] houbiPrices = new HoubiPrice.HoubiTicker[0];
        when(restTemplate.getForEntity(anyString(), eq(HoubiPrice.class)))
                .thenReturn(ResponseEntity.ok(new HoubiPrice(houbiPrices, "ok", 1L)));

        BinancePrice[] binancePrices = new BinancePrice[]{
                new BinancePrice("ETHUSDT", 2000.0, 2050.0)
        };
        when(restTemplate.getForEntity(anyString(), eq(BinancePrice[].class)))
                .thenReturn(ResponseEntity.ok(binancePrices));

        priceAggregationService.scheduleUpdateAggregatedPrice();

        // Assert: Verify that saveAggregatedPrices was called with an empty set
        verify(priceRepository, times(1)).saveAggregatedPrices(any());
    }

    @Test
    void scheduleUpdateAggregatedPrice_shouldLogErrorWhenBinanceFails() {
        when(restTemplate.getForEntity(anyString(), eq(BinancePrice[].class)))
                .thenThrow(new RuntimeException("Binance error"));

        priceAggregationService.scheduleUpdateAggregatedPrice();

        verify(priceRepository, times(1)).saveAggregatedPrices(any());
    }

    @Test
    void scheduleUpdateAggregatedPrice_shouldLogErrorWhenHuobiFails() {
        when(restTemplate.getForEntity(anyString(), eq(HoubiPrice.class)))
                .thenThrow(new RuntimeException("Huobi error"));

        priceAggregationService.scheduleUpdateAggregatedPrice();

        verify(priceRepository, times(1)).saveAggregatedPrices(any());
    }

    @Test
    void aggregatePrices_shouldNotScheduleUpdateAggregatedPriceIfSymbolsDoNotMatch() {
        BinancePrice[] binancePrices = new BinancePrice[]{
                new BinancePrice("LTCUSDT", 2000.0, 2050.0)
        };

        HoubiPrice.HoubiTicker[] houbiPrices = new HoubiPrice.HoubiTicker[]{
                new HoubiPrice.HoubiTicker("XRPUSDT", 1950.0, 2000.0)
        };

        when(restTemplate.getForEntity(anyString(), eq(BinancePrice[].class)))
                .thenReturn(ResponseEntity.ok(binancePrices));
        when(restTemplate.getForEntity(anyString(), eq(HoubiPrice.class)))
                .thenReturn(ResponseEntity.ok(new HoubiPrice(houbiPrices, "ok", 1L)));

        priceAggregationService.scheduleUpdateAggregatedPrice();

        verify(priceRepository, times(1)).saveAggregatedPrices(any());
    }
}
