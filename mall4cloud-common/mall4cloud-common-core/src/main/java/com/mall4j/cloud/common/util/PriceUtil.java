package com.mall4j.cloud.common.util;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.constant.Constant;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author FrozenWatermelon
 * @date 2020/11/16
 */
public class PriceUtil {

    /**
     * 元和分相差100
     */
    public static final String ONE_HUNDRED="100";

    /**
     * 元和分相差100
     * 因为分为long类型，:  Long.MAX_VALUE = 2<sup>63</sup>-1
     * <br/> 所以元的最大值（或阈值）为： MAX_AMOUNT = Long.MAX_VALUE/100
     * <br/>Long.MAX_VALUE : 9223372036854775807
     * <br/>Long.MAX_VALUE/100 : 92233720368547758
     */
    public static final long MAX_AMOUNT= 92233720368547758L;
    public static final String MAX_AMOUNT_STR= "92233720368547758";

    /**
     * 分的最大值 为 MAX_AMOUNT * 100
     */
    public static final long MAX_CENT= 9223372036854775800L;


    /**
     * 分转元
     * @param price 分
     * @return 元
     */
    public static BigDecimal toDecimalPrice(Long price) {
        if (price != null) {
            BigDecimal priceDecimal = new BigDecimal(price);
            // 除以100
            return priceDecimal.divide(new BigDecimal(ONE_HUNDRED),2, RoundingMode.HALF_EVEN);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 元转分
     * @param price 元
     * @return 分
     */
    public static Long toLongCent(BigDecimal price) {
        // 乘以100
        return price.multiply(new BigDecimal(ONE_HUNDRED)).longValue();
    }

    /**
     * 相除，用银行家舍入法
     * @param a1 除数
     * @param a2 被除数
     * @return 分
     */
    public static Long divideByBankerRounding(long a1, long a2) {
        return new BigDecimal(a1).divide(new BigDecimal(a2),2, RoundingMode.HALF_EVEN).longValue();
    }

    /**
     * 相除保留两位小数再乘以100，用银行家舍入法
     * @param a1 除数
     * @param a2 被除数
     * @param isLess 是否需要乘以100
     * @return 分
     */
    public static Long divideByBankerRoundingThan(long a1, long a2,boolean isLess) {
        if(isLess) {
            return new BigDecimal(a1).divide(new BigDecimal(a2), 2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)).longValue();
        }
        return new BigDecimal(a1).divide(new BigDecimal(a2), 2, RoundingMode.HALF_EVEN).longValue();
    }


    /**
     * 金额Long格式转换Double
     * @param num
     * @return 金额字符串
     */
    public static String conversionPrices (String num) {
        if (StrUtil.isBlank(num)) {
            return num;
        }
        BigDecimal b1 = new BigDecimal(num);
        BigDecimal b2 = new BigDecimal(Constant.PRICE_MAGNIFICATION);
        double price = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return Double.toString(price);
    }

}
