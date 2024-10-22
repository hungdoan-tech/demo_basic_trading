package com.hungdoan.aquariux.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class CryptoPairValidator implements ConstraintValidator<CryptoPairValid, String> {

    public static final Set<String> VALID_CRYPTO_PAIRS = Set.of("ETHUSDT", "BTCUSDT");

    public static final Set<String> VALID_CRYPTO_QUOTES = Set.of("USDT", "ETH", "BTC");

    @Override
    public boolean isValid(String cryptoPair, ConstraintValidatorContext context) {
        return VALID_CRYPTO_PAIRS.contains(cryptoPair);
    }
}
