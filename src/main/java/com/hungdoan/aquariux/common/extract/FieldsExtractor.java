package com.hungdoan.aquariux.common.extract;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Component
public class FieldsExtractor {

    private final Map<Class<?>, Set<String>> clazzToFields = new ConcurrentHashMap<>();

    public boolean checkValidField(String sortString, Class<?> clazz) {
        String lowerSortString = sortString.toLowerCase(Locale.ROOT);

        Set<String> fieldNames = clazzToFields.computeIfAbsent(clazz, this::extractFieldNames);

        return fieldNames.contains(lowerSortString);
    }

    private Set<String> extractFieldNames(Class<?> clazz) {

        Set<String> fieldNames = new HashSet<>();

        Stream.concat(Arrays.stream(clazz.getSuperclass().getDeclaredFields()),
                        Arrays.stream(clazz.getDeclaredFields()))
                .map(Field::getName)
                .map(fieldName -> fieldName.toLowerCase(Locale.ROOT))
                .forEach(fieldNames::add);

        return fieldNames;
    }
}
