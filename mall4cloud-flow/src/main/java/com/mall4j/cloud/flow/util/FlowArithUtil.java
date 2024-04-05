package com.mall4j.cloud.flow.util;

import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.util.Arith;

import java.util.Objects;

/**
 * 流量统计计算工具类
 *
 * @author YXF
 */
public class FlowArithUtil {

    private static Double HUNDRED_DOUBLE = 100D;
    private static Double ZERO_DOUBLE = 0D;
    private static Integer HUNDRED = 100;

    /**
     * 获取平均数
     * @param value
     * @param oldValue
     * @return 比例
     */
    public static Double getAverage(Integer value, Integer oldValue) {
        return Arith.div(value, oldValue,2);
    }

    /**
     * 计算比例
     * @param value
     * @param oldValue
     * @return 比例
     */
    public static Double getRatio(Integer value, Integer oldValue) {
        return Arith.mul(Arith.div(value, oldValue,2),100);
    }

    /**
     * 计算比例
     * @param value
     * @param oldValue
     * @return 比例
     */
    public static Double getRatio(Long value, Integer oldValue) {
        return Arith.mul(Arith.div(value, oldValue,2),100);
    }

    /**
     * 获取增长比例
     * @param value
     * @param oldValue
     * @return
     */
    public static Double getIncreaseRatio(Long value, Long oldValue) {
        return calculateIncreaseRatio(value.doubleValue(), oldValue.doubleValue());
    }


    /**
     * 获取增长比例
     * @param value
     * @param oldValue
     * @return
     */
    public static Double getIncreaseRatio(Integer value, Integer oldValue) {
        return calculateIncreaseRatio(value.doubleValue(), oldValue.doubleValue());
    }


    /**
     * 获取增长比例
     * @param value
     * @param oldValue
     * @return
     */
    public static Double getIncreaseRatio(Double value, Double oldValue) {
        return calculateIncreaseRatio(value, oldValue);
    }

    /**
     * 计算两个值增加的比例
     * @param value
     * @param oldValue
     * @return
     */
    private static Double calculateIncreaseRatio(Double value, Double oldValue) {
        if (value == 0 && oldValue == 0) {
            return ZERO_DOUBLE;
        } else if (value == 0 && oldValue > 0) {
            return -HUNDRED_DOUBLE;
        } else if (value > 0 && oldValue == 0) {
            return ZERO_DOUBLE;
        }
        double sub = Arith.sub(value, oldValue);
        return Arith.mul(Arith.div(sub,oldValue,2), HUNDRED);
    }

    public static Long getAnalysisAmount(Long value, Integer oldValue) {
        if (value == 0 && oldValue == 0) {
            return Constant.ZERO_LONG;
        }
        Double amount = Arith.div(value.doubleValue(), oldValue.doubleValue(), 0);
        return amount.longValue();
    }
}
