package com.hungdoan.aquariux.common.extract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.hungdoan.aquariux.service.CryptoCoinAssetService.KNOWN_QUOTES;

@Component
public class CryptoPairExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoPairExtractor.class);

    /**
     * Extracts the base and quote currencies from a crypto pair string.
     *
     * @param cryptoPair the pair string (e.g., "ETHUSDT", "BTCUSD")
     * @return a String array with [baseCurrency, quoteCurrency]
     * @throws IllegalArgumentException if the pair cannot be parsed
     */
    public String[] extractCurrencies(String cryptoPair) {

        if (cryptoPair == null || cryptoPair.isBlank()) {
            LOG.error("Empty crypto pair");
            return new String[]{};
        }

        for (String quoteCurrency : KNOWN_QUOTES) {

            if (cryptoPair.endsWith(quoteCurrency)) {

                String baseCurrency = cryptoPair.substring(0, cryptoPair.length() - quoteCurrency.length());

                if (!KNOWN_QUOTES.contains(baseCurrency)) {
                    LOG.error("Unknown crypto pair format: " + cryptoPair);
                    return new String[]{};
                }

                return new String[]{baseCurrency, quoteCurrency};
            }
        }

        LOG.error("Unknown crypto pair format: " + cryptoPair);
        return new String[]{};
    }
}
