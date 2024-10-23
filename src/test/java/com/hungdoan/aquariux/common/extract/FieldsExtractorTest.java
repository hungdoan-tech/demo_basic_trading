package com.hungdoan.aquariux.common.extract;

import com.hungdoan.aquariux.common.validation.Sortable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldsExtractorTest {

    private FieldsExtractor fieldsExtractor;

    class SuperClass {
        @Sortable
        private String superField;
    }

    class DerivedClass extends SuperClass {
        @Sortable
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
        assertTrue(fieldsExtractor.checkValidSortableField("derivedField", DerivedClass.class));
    }

    @Test
    void testFieldExistsInSuperClass() {
        assertTrue(fieldsExtractor.checkValidSortableField("superField", DerivedClass.class));
    }

    @Test
    void testFieldDoesNotExist() {
        assertFalse(fieldsExtractor.checkValidSortableField("nonExistentField", DerivedClass.class));
    }

    @Test
    void testFieldExistsNoInheritance() {
        assertFalse(fieldsExtractor.checkValidSortableField("someField", NoFieldClass.class));
    }

    @Test
    void testFieldCaseInsensitive() {
        assertTrue(fieldsExtractor.checkValidSortableField("SUPERFIELD", DerivedClass.class));
        assertTrue(fieldsExtractor.checkValidSortableField("derivedfield", DerivedClass.class));
    }

    @Test
    void testFieldCaching() {
        assertTrue(fieldsExtractor.checkValidSortableField("derivedField", DerivedClass.class));
        assertTrue(fieldsExtractor.checkValidSortableField("derivedField", DerivedClass.class));
    }
}
