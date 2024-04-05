

package com.mall4j.cloud.user.constant;

/**
 * 最近几天类型
 * @author cl
 * @date 2021-05-18 17:17:54
 */
public enum RecentDaysTypeEnum {

    /**
     * 今天
     */
    TODAY(0),

    /**
     * 最近7天
     */
    RECENT_SEVEN_DAYS(7),

    /**
     * 最近15天
     */
    RECENT_FIFTEEN_DAYS(15),

    /**
     * 最近30天
     */
    RECENT_THIRTY_DAYS(30),

    /**
     * 最近45天
     */
    RECENT_FORTY_FIVE_DAYS(45),

    /**
     * 最近60天
     */
    RECENT_SIXTY_DAYS(60),

    /**
     * 最近90天
     */
    RECENT_NINETY_DAYS(90),

    /**
     * 最近180天
     */
    RECENT_ONE_HUNDRED_AND_EIGHTY_DAYS(180);

    private Integer num;

    public Integer value() {
        return num;
    }

    RecentDaysTypeEnum(Integer num) {
        this.num = num;
    }

    public static RecentDaysTypeEnum instance(Integer value) {
        RecentDaysTypeEnum[] enums = values();
        for (RecentDaysTypeEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
