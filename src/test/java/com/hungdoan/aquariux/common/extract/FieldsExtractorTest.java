package com.hungdoan.aquariux.common.extract;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldsExtractorTest {

    private FieldsExtractor fieldsExtractor;

    class SuperClass {
        private String superField;
    }

    class DerivedClass extends SuperClass {
        private String derivedField;
    }

    class NoFieldClass {
    }

    @BeforeEach
    void setUp() {
        fieldsExtractor = new FieldsExtractor();
    }

    @Test
    void testFieldExistsInDerivedClass() {
        assertTrue(fieldsExtractor.checkValidField("derivedField", DerivedClass.class));
    }

    @Test
    void testFieldExistsInSuperClass() {
        assertTrue(fieldsExtractor.checkValidField("superField", DerivedClass.class));
    }

    @Test
    void testFieldDoesNotExist() {
        assertFalse(fieldsExtractor.checkValidField("nonExistentField", DerivedClass.class));
    }

    @Test
    void testFieldExistsNoInheritance() {
        assertFalse(fieldsExtractor.checkValidField("someField", NoFieldClass.class));
    }

    @Test
    void testFieldCaseInsensitive() {
        assertTrue(fieldsExtractor.checkValidField("SUPERFIELD", DerivedClass.class));
        assertTrue(fieldsExtractor.checkValidField("derivedfield", DerivedClass.class));
    }

    @Test
    void testFieldCaching() {
        assertTrue(fieldsExtractor.checkValidField("derivedField", DerivedClass.class));
        assertTrue(fieldsExtractor.checkValidField("derivedField", DerivedClass.class));
    }
}
