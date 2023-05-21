package org.lins.mmmjjkx.mixtools.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class OtherUtil {
    private static final NumberFormat PRETTY_FORMAT = NumberFormat.getInstance(Locale.getDefault());

    public static String formatAsPrettyCurrency(final BigDecimal value) {
        String str = PRETTY_FORMAT.format(value);
        if (str.endsWith(".00")) {
            str = str.substring(0, str.length() - 3);
        }
        return str;
    }

    public static String ToStringCurrency(final BigDecimal value) {
        String currency = formatAsPrettyCurrency(value);
        if (value.signum() < 0) {
            currency = currency.substring(1);
        }
        return currency;
    }

    public static <T> Optional<T> listGetIf(List<T> list, Predicate<? super T> filter){
        for (T obj : list) {
            if (filter.test(obj)) return Optional.of(obj);
        }
        return Optional.empty();
    }

    public static <K,T> Optional<T> mapValueGetIf(Map<K, T> map, Predicate<? super T> filter){
        for (T value : map.values()) {
            if (filter.test(value)) return Optional.of(value);
        }
        return Optional.empty();
    }
}
