package com.mall4j.cloud.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 */
public class Arith {
    /**
     * 默认除法运算精度
     */
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 这个类不能实例化
     */
    private Arith() {
    }

    /**
     * 提供精确的加法运算。.
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        // 必须转换成String
        String s1 = Double.toString(v1);
        String s2 = Double.toString(v2);
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。.
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        // 必须转换成String
        String s1 = Double.toString(v1);
        String s2 = Double.toString(v2);
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。.
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        // 必须转换成String
        String s1 = Double.toString(v1);
        String s2 = Double.toString(v2);
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。.
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        if (v2 == 0){
            return 0.0;
        }
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。.
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (v2 == 0){
            return 0.0;
        }
        // 必须转换成String
        String s1 = Double.toString(v1);
        String s2 = Double.toString(v2);
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。.
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        String s1 = Double.toString(v);
        BigDecimal b = new BigDecimal(s1);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * @param bigDecimal
     * @param bigDecimal2
     * @param bigDecimal3
     * @return
     */
    public static double add(BigDecimal bigDecimal, BigDecimal bigDecimal2, BigDecimal bigDecimal3) {
        return bigDecimal.add(bigDecimal2).add(bigDecimal3).doubleValue();
    }

    /**
     * @param preDepositPrice
     * @param finalPrice
     * @return
     */
    public static double add(BigDecimal preDepositPrice, BigDecimal finalPrice) {
        return preDepositPrice.add(finalPrice).doubleValue();
    }

    /**
     * 两个浮点数比较是否相等
     * @param v1
     * @param v2
     * @return
     */
    public static boolean isEquals(double v1, double v2) {
        // 必须转换成String
        String s1 = Double.toString(v1);
        String s2 = Double.toString(v2);
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return Objects.equals(b1,b2);
    }

    public static BigDecimal setScale(double v1, int scale) {
        // 必须转换成String
        return new BigDecimal(v1).setScale(scale,BigDecimal  .ROUND_HALF_UP);
    }

    public static Double ceil(double v1) {
        // 必须转换成String
        return Math.ceil(v1);
    }
}
