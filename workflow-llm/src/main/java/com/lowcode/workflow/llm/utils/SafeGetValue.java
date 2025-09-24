package com.lowcode.workflow.llm.utils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SafeGetValue {

    public static Optional<Double> extractDouble(String key, Map<String, Object> params) {
        Object val = params.get(key);
        if (val instanceof Number) {
            return Optional.of(((Number) val).doubleValue());
        }
        return java.util.Optional.empty();
    }

    public static Optional<Integer> extractInteger(String key, Map<String, Object> params) {
        Object val = params.get(key);
        if (val instanceof Number) {
            return Optional.of(((Number) val).intValue());
        }
        return Optional.empty();
    }

    public static Optional<Long> extractLong(String key, Map<String, Object> params) {
        Object val = params.get(key);
        if (val instanceof Number) {
            return Optional.of(((Number) val).longValue());
        }
        return Optional.empty();
    }

    public static Optional<Float> extractFloat(String key, Map<String, Object> params) {
        Object val = params.get(key);
        if (val instanceof Number) {
            return Optional.of(((Number) val).floatValue());
        }
        return Optional.empty();
    }

    public static Optional<Boolean> extractBoolean(String key, Map<String, Object> params) {
        Object val = params.get(key);
        if (val instanceof Boolean) {
            return Optional.of((Boolean) val);
        }
        return Optional.empty();
    }

    public static Optional<Duration> extractDuration(String key, Map<String, Object> params) {
        Optional<Integer> seconds = extractInteger(key, params);
        return seconds.map(Duration::ofSeconds);
    }

    public static Optional<List<String>> extractStringList(String key, Map<String, Object> params) {
        Object val = params.get(key);
        if (val instanceof List) {
            List<?> list = (List<?>) val;
            if (list.stream().allMatch(item -> item instanceof String)) {
                return Optional.of(list.stream().map(String::valueOf).collect(Collectors.toList()));
            }
        }
        return Optional.empty();
    }

    public static Optional<String> extractString(String key, Map<String, Object> params) {
        Object value = params.get(key);
        return value instanceof String ? Optional.of((String) value) : Optional.empty();
    }
}
