package org.lins.mmmjjkx.mixtools.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MixNumberUtil {
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
}
