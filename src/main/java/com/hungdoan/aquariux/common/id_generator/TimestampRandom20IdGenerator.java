package com.hungdoan.aquariux.common.id_generator;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Random;

/**
 * Timestamp Random 20 Ids: roughly sortable (since timestamp starts the string) 20 characters long
 * first 10 char are the current timestamp in seconds from our custom epoc of Jan-1-2020 0:0:0 UTC,
 * left padded with 0 to 10 chars 11th single
 * character is one random alphabetic last 9 are random alphanumeric
 *
 * <p>This Id will work for over 200 years, it will stop working (as in be longer than 20 chars)
 * when the timestamp portion exceeds 10 characters.
 *
 * <p>Example: 0044834759MdjfVJabJe
 */
class TimestampRandom20IdGenerator extends BaseIdGenerator {

    private static final long EPOCH_SECONDS =
            OffsetDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneOffset.UTC)
                    .toInstant()
                    .getEpochSecond();

    private final Random random;

    TimestampRandom20IdGenerator() {
        super();
        this.random = newRandom();
    }

    @Override
    String getId() {
        StringBuilder bld = new StringBuilder(20);
        String timestamp = String.valueOf(Instant.now().getEpochSecond() - EPOCH_SECONDS);

        if (timestamp.length() < 8) {
            throw new IllegalStateException("Should always have timestamp at least 8 " + timestamp);
        }

        if (timestamp.length() > 10) {
            throw new IllegalStateException("Timestamp too long " + timestamp);
        }

        if (timestamp.length() == 8) {
            bld.append("00");
        } else if (timestamp.length() == 9) {
            bld.append("0");
        }

        bld.append(timestamp);
        appendRandomAlpha(random, bld);
        appendRandomAlphanumeric(random, bld, 9);

        if (bld.capacity() != 20) {
            throw new IllegalStateException("It grew! " + bld);
        }

        if (bld.length() != 20) {
            throw new IllegalStateException("It isn't 20! " + bld);
        }

        return bld.toString();
    }
}