package com.cdkj.baselibrary.utils;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_CEILING;

/**
 * Created by cdkj on 2017/7/19.
 */

public class BigDecimalUtils {

    public static int intValue(BigDecimal b) {
        if (b != null) {
            return b.intValue();
        }

        return 0;
    }

    public static double doubleValue(BigDecimal b) {
        if (b != null) {
            return b.doubleValue();
        }

        return 0;
    }

    //减法
    public static BigDecimal subtract(BigDecimal b, BigDecimal b1) {
        if (b != null && b1 != null) {
            return b.subtract(b1);
        }

        return BigDecimal.ZERO;
    }

    //乘法
    public static BigDecimal multiply(BigDecimal b, BigDecimal b1) {
        if (b != null && b1 != null) {
            return b.multiply(b1);
        }

        return BigDecimal.ZERO;
    }

    //加法
    public static BigDecimal add(BigDecimal b, BigDecimal b1) {
        if (b != null && b1 != null) {
            return b.add(b1);
        }

        return BigDecimal.ZERO;
    }

    public static BigDecimal div(BigDecimal b, BigDecimal b1, int sca) {

        if (b != null && b1 != null) {
            return b.divide(b1, sca, ROUND_CEILING);
        }
        return BigDecimal.ZERO;
    }

}
