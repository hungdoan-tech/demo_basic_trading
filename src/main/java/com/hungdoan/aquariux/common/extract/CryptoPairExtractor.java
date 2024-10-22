package com.hungdoan.aquariux.common.extract;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CryptoPairExtractor {

    private final Set<String> KNOWN_QUOTES = Set.of("USDT", "ETH", "BTC");

    /**
     * Extracts the base and quote currencies from a crypto pair string.
     *
     * @param cryptoPair the pair string (e.g., "ETHUSDT", "BTCUSD")
     * @return a String array with [baseCurrency, quoteCurrency]
     * @throws IllegalArgumentException if the pair cannot be parsed
     */
    public String[] extractCurrencies(String cryptoPair) {

        for (String quoteCurrency : KNOWN_QUOTES) {

            if (cryptoPair.endsWith(quoteCurrency)) {

                String baseCurrency = cryptoPair.substring(0, cryptoPair.length() - quoteCurrency.length());

                if (!KNOWN_QUOTES.contains(baseCurrency)) {
                    throw new IllegalArgumentException("Unknown crypto pair format: " + cryptoPair);
                }

                return new String[]{baseCurrency, quoteCurrency};
            }
        }

        throw new IllegalArgumentException("Unknown crypto pair format: " + cryptoPair);
    }
}
