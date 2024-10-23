package com.hungdoan.aquariux.service;

import com.hungdoan.aquariux.common.extract.CryptoPairExtractor;
import com.hungdoan.aquariux.common.id_generator.IdGenerator;
import com.hungdoan.aquariux.data_access.spec.AssetRepository;
import com.hungdoan.aquariux.data_access.spec.TradeRepository;
import com.hungdoan.aquariux.exception.InvalidTrade;
import com.hungdoan.aquariux.exception.OptimisticLockingFailureException;
import com.hungdoan.aquariux.model.Asset;
import com.hungdoan.aquariux.model.Price;
import com.hungdoan.aquariux.model.Trade;
import com.hungdoan.aquariux.service.spec.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CryptoPairTradeServiceTest {

    private CryptoPairTradeService tradeService;
    private PriceService priceService;
    private CryptoPairExtractor cryptoPairExtractor;
    private IdGenerator idGenerator;
    private AssetRepository assetRepository;
    private TradeRepository tradeRepository;

    @BeforeEach
    void setUp() {
        cryptoPairExtractor = spy(CryptoPairExtractor.class);
        priceService = mock(PriceService.class);
        idGenerator = mock(IdGenerator.class);
        assetRepository = mock(AssetRepository.class);
        tradeRepository = mock(TradeRepository.class);
        ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
        tradeService = new CryptoPairTradeService(priceService, cryptoPairExtractor, idGenerator, assetRepository, tradeRepository, applicationEventPublisher);
    }

    @Test
    void executeTrade_ShouldThrowInvalidTrade_WhenPriceNotFound() {
        // Arrange
        when(priceService.getPrice("BTCUSDT")).thenReturn(Optional.empty());

        // Act & Assert
        InvalidTrade exception = assertThrows(InvalidTrade.class, () -> {
            tradeService.executeTrade("user1", "BTCUSDT", "BUY", 1.0);
        });
        assertEquals("Could not get the price of the crypto pair BTCUSDT", exception.getMessage());
    }

    @Test
    void executeTrade_ShouldThrowInvalidTrade_WhenInvalidTradeType() {
        // Arrange
        Price price = new Price("price1", "BTCUSDT", 100.0, 98.0, ZonedDateTime.now());
        when(priceService.getPrice("BTCUSDT")).thenReturn(Optional.of(price));

        // Act & Assert
        InvalidTrade exception = assertThrows(InvalidTrade.class, () -> {
            tradeService.executeTrade("user1", "BTCUSDT", "INVALID_TYPE", 1.0);
        });
        assertEquals("Invalid trade type, it must be [SELL, BUY] instead of INVALID_TYPE", exception.getMessage());
    }

    @Test
    void executeTrade_ShouldThrowInvalidTrade_WhenInvalidCryptoPair() {
        // Arrange
        Price price = new Price("price1", "BTCUSDT", 100.0, 98.0, ZonedDateTime.now());
        when(priceService.getPrice("BTCUSDT")).thenReturn(Optional.of(price));
        when(cryptoPairExtractor.extractCurrencies("BTCUSDT")).thenReturn(new String[]{"BTC", "USDT"});

        // Act & Assert
        InvalidTrade exception = assertThrows(InvalidTrade.class, () -> {
            tradeService.executeTrade("user1", "INVALID_PAIR", "BUY", 1.0);
        });
        assertEquals("Invalid crypto pair, the system does not support this pair INVALID_PAIR", exception.getMessage());
    }

    @Test
    void buy_ShouldThrowInvalidTrade_WhenInsufficientQuoteCoinBalance() {
        // Arrange
        Price price = new Price("price1", "BTCUSDT", 100.0, 98.0, ZonedDateTime.now());
        when(priceService.getPrice("BTCUSDT")).thenReturn(Optional.of(price));
        when(cryptoPairExtractor.extractCurrencies("BTCUSDT")).thenReturn(new String[]{"BTC", "USDT"});
        when(assetRepository.getAssets("user1", Arrays.asList("USDT", "BTC"))).thenReturn(createUserAssetsForBuy(50.0)); // Not enough for total cost

        // Act & Assert
        InvalidTrade exception = assertThrows(InvalidTrade.class, () -> {
            tradeService.executeTrade("user1", "BTCUSDT", "BUY", 1.0);
        });
        assertEquals("Insufficient USDT balance, the remaining 50.00000 < total cost 98.00000", exception.getMessage());
    }

    @Test
    void sell_ShouldThrowInvalidTrade_WhenInsufficientBaseCoinBalance() {
        // Arrange
        Price price = new Price("price1", "BTCUSDT", 100.0, 98.0, ZonedDateTime.now());
        when(priceService.getPrice("BTCUSDT")).thenReturn(Optional.of(price));
        when(cryptoPairExtractor.extractCurrencies("BTCUSDT")).thenReturn(new String[]{"BTC", "USDT"});
        when(assetRepository.getAssets("user1", Arrays.asList("USDT", "BTC"))).thenReturn(createUserAssetsForSell(0.5)); // Not enough base coin

        // Act & Assert
        InvalidTrade exception = assertThrows(InvalidTrade.class, () -> {
            tradeService.executeTrade("user1", "BTCUSDT", "SELL", 1.0);
        });
        assertEquals("Insufficient BTC balance, the remaining 0.50000 < trade amount 1.00000", exception.getMessage());
    }

    @Test
    void executeTrade_ShouldPerformBuySuccessfully() {
        // Arrange
        Price price = new Price("price1", "BTCUSDT", 100.0, 98.0, ZonedDateTime.now());
        when(priceService.getPrice("BTCUSDT")).thenReturn(Optional.of(price));
        when(cryptoPairExtractor.extractCurrencies("BTCUSDT")).thenReturn(new String[]{"BTC", "USDT"});
        when(assetRepository.getAssets("user1", Arrays.asList("USDT", "BTC"))).thenReturn(createUserAssetsForBuy(150.0)); // Enough for trade
        when(idGenerator.getId()).thenReturn("trade1");

        // Act
        tradeService.executeTrade("user1", "BTCUSDT", "BUY", 1.0);

        // Assert
        ArgumentCaptor<Trade> tradeCaptor = ArgumentCaptor.forClass(Trade.class);
        verify(tradeRepository).save(tradeCaptor.capture());
        Trade trade = tradeCaptor.getValue();
        assertEquals("trade1", trade.getId());
        assertEquals("user1", trade.getUserId());
        assertEquals("BTCUSDT", trade.getCryptoPair());
        assertEquals("BUY", trade.getTradeType());
        assertEquals(1.0, trade.getTradeAmount());
        assertEquals(98.0, trade.getTradePrice() * trade.getTradeAmount());
    }

    @Test
    void executeTrade_ShouldPerformSellSuccessfully() {
        // Arrange
        Price price = new Price("price1", "BTCUSDT", 100.0, 98.0, ZonedDateTime.now());
        when(priceService.getPrice("BTCUSDT")).thenReturn(Optional.of(price));
        when(cryptoPairExtractor.extractCurrencies("BTCUSDT")).thenReturn(new String[]{"BTC", "USDT"});
        when(assetRepository.getAssets("user1", Arrays.asList("USDT", "BTC"))).thenReturn(createUserAssetsForSell(2.0)); // Enough for trade
        when(idGenerator.getId()).thenReturn("trade2");

        // Act
        tradeService.executeTrade("user1", "BTCUSDT", "SELL", 1.0);

        // Assert
        ArgumentCaptor<Trade> tradeCaptor = ArgumentCaptor.forClass(Trade.class);
        verify(tradeRepository).save(tradeCaptor.capture());
        Trade trade = tradeCaptor.getValue();
        assertEquals("trade2", trade.getId());
        assertEquals("user1", trade.getUserId());
        assertEquals("BTCUSDT", trade.getCryptoPair());
        assertEquals("SELL", trade.getTradeType());
        assertEquals(1.0, trade.getTradeAmount());
        assertEquals(100.0, trade.getTradePrice() * trade.getTradeAmount()); // 1 * bid price
    }

    // Helper methods to create mock assets
    private Map<String, Asset> createUserAssetsForBuy(double usdBalance) {
        Map<String, Asset> assets = new HashMap<>();
        assets.put("USDT", new Asset("user1", "USDT", usdBalance, 1));
        assets.put("BTC", new Asset("user1", "BTC", 0.0, 1));
        return assets;
    }

    private Map<String, Asset> createUserAssetsForSell(double btcBalance) {
        Map<String, Asset> assets = new HashMap<>();
        assets.put("BTC", new Asset("user1", "BTC", btcBalance, 1));
        assets.put("USDT", new Asset("user1", "USDT", 0.0, 1));
        return assets;
    }

    @Test
    void optimisticLocking_ShouldRetry_WhenOptimisticLockingFailureOccurs() throws OptimisticLockingFailureException {
        // Arrange
        Price price = new Price("price1", "BTCUSDT", 100.0, 98.0, ZonedDateTime.now());
        when(priceService.getPrice("BTCUSDT")).thenReturn(Optional.of(price));
        when(cryptoPairExtractor.extractCurrencies("BTCUSDT")).thenReturn(new String[]{"BTC", "USDT"});
        when(assetRepository.getAssets("user1", Arrays.asList("USDT", "BTC")))
                .thenReturn(createUserAssetsForBuy(600));
        when(idGenerator.getId()).thenReturn("trade3");

        // Use doThrow for void methods
        doThrow(new OptimisticLockingFailureException("Optimistic locking failed"))
                .when(assetRepository).updateAssets(any());

        // Act & Assert
        InvalidTrade exception = assertThrows(InvalidTrade.class, () -> {
            tradeService.executeTrade("user1", "BTCUSDT", "BUY", 1.0);
        });
        assertEquals("Failed to complete the trade after multiple attempts", exception.getMessage());
    }
}

