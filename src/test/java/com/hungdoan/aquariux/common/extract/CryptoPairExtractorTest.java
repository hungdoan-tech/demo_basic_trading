package com.hungdoan.aquariux.common.extract;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class CryptoPairExtractorTest {

    private CryptoPairExtractor cryptoPairExtractor;

    @BeforeEach
    void setUp() {
        cryptoPairExtractor = new CryptoPairExtractor();
    }

    @Test
    void extractCurrencies_ValidETHUSDT_ReturnsCorrectBaseAndQuote() {
        String cryptoPair = "ETHUSDT";
        String[] result = cryptoPairExtractor.extractCurrencies(cryptoPair);
        assertArrayEquals(new String[]{"ETH", "USDT"}, result);
    }

    @Test
    void extractCurrencies_ValidBTCUSDT_ReturnsCorrectBaseAndQuote() {
        String cryptoPair = "BTCUSDT";
        String[] result = cryptoPairExtractor.extractCurrencies(cryptoPair);
        assertArrayEquals(new String[]{"BTC", "USDT"}, result);
    }

    @Test
    void extractCurrencies_InvalidPair_ReturnsEmptyArray() {
        String cryptoPair = "BTCUSD"; // Invalid since USD is not a known quote
        String[] result = cryptoPairExtractor.extractCurrencies(cryptoPair);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    void extractCurrencies_UnknownPair_LogsErrorAndReturnsEmptyArray() {
        // Assuming you have a logging mechanism and can verify it
        String cryptoPair = "XYZUSDT"; // Invalid since XYZ is not a known base
        String[] result = cryptoPairExtractor.extractCurrencies(cryptoPair);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    void extractCurrencies_EmptyPair_ReturnsEmptyArray() {
        String cryptoPair = ""; // Empty string case
        String[] result = cryptoPairExtractor.extractCurrencies(cryptoPair);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    void extractCurrencies_NullPair_ReturnsEmptyArray() {
        String[] result = cryptoPairExtractor.extractCurrencies(null);
        assertArrayEquals(new String[]{}, result);
    }
}