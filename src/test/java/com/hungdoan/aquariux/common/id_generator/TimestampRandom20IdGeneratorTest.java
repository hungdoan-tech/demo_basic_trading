package com.hungdoan.aquariux.common.id_generator;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimestampRandom20IdGeneratorTest {

    private final TimestampRandom20IdGenerator idGenerator = new TimestampRandom20IdGenerator();

    @Test
    void testGetIdGeneratesCorrectLength() {
        String id = idGenerator.getId();
        assertEquals(20, id.length(), "ID should be 20 characters long");
    }

    @Test
    void testGetIdGeneratesCorrectFormat() {
        String id = idGenerator.getId();
        // First 10 characters are numeric
        String timestampPart = id.substring(0, 10);
        assertTrue(Pattern.matches("\\d{10}", timestampPart), "First 10 characters should be numeric");

        // 11th character should be alphabetic
        char alphaChar = id.charAt(10);
        assertTrue(Character.isAlphabetic(alphaChar), "11th character should be alphabetic");

        // Last 9 characters should be alphanumeric
        String randomPart = id.substring(11);
        assertTrue(Pattern.matches("[a-zA-Z0-9]{9}", randomPart), "Last 9 characters should be alphanumeric");
    }
}
