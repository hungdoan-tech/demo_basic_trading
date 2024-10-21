package com.hungdoan.aquariux.common.id_generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class IdGeneratorTest {

    private Environment environment;
    private IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        environment = Mockito.mock(Environment.class);
    }

    @Test
    void testConstructorWithMissingProperty() {
        when(environment.getProperty("IdGenerator")).thenReturn(null);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            idGenerator = new IdGenerator(environment);
        });

        assertEquals("Missing IdGenerator er.properties value", exception.getMessage());
    }

    @Test
    void testConstructorWithUnknownGenerator() {
        when(environment.getProperty("IdGenerator")).thenReturn("unknown");

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            idGenerator = new IdGenerator(environment);
        });

        assertEquals("Unknown IdGenerator value unknown", exception.getMessage());
    }

    @Test
    void testConstructorWithValidProperty() {
        when(environment.getProperty("IdGenerator")).thenReturn("tsrand20");

        idGenerator = new IdGenerator(environment);

        assertNotNull(idGenerator, "IdGenerator should be initialized successfully");
    }

    @Test
    void testGetId() {
        when(environment.getProperty("IdGenerator")).thenReturn("tsrand20");
        idGenerator = new IdGenerator(environment);

        String id = idGenerator.getId();
        assertNotNull(id, "Generated ID should not be null");
        assertEquals(20, id.length(), "Generated ID should be 20 characters long");
    }
}
