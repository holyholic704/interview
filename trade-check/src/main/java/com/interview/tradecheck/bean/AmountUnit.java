package com.interview.tradecheck.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Function;

/**
 * @author jiage
 */
@AllArgsConstructor
@Getter
public enum AmountUnit {

    /**
     * 元
     */
    YUAN("元", v -> new BigDecimal(v).multiply(BigDecimal.valueOf(100)).longValue()),
    /**
     * 分
     */
    FEN("分", Long::parseLong);

    private final String unit;
    private final Function<String, Long> convert;

    public static AmountUnit getUnit(String unit) {
        return Arrays.stream(values()).filter(v -> v.getUnit().equals(unit)).findFirst().orElse(null);
    }
}
