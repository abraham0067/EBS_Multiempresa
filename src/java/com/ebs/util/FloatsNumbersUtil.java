package com.ebs.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by eflores on 03/10/2017.
 */
public class FloatsNumbersUtil {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double round6Places(double value) {
        return FloatsNumbersUtil.round(value, 6);
    }


}
