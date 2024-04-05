package com.mall4j.cloud.api.platform.constant;

/**
 * 门店类型
 *
 * @author gmq
 */
public enum StoreTypeEnum {

    //门店类型1-自营，2-经销，3-代销，4-电商，5-其他

    SESLF(1, "自营"),

    SELL(2, "经销"),

    OFFER_SALE(3, "代销"),

    EBUSINESS(4, "电商"),

    OTHER(5, "其他"),
    ;

    private final Integer value;
    private final String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    StoreTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        StoreTypeEnum[] storeTypeEnums = values();
        for (StoreTypeEnum storeTypeEnum : storeTypeEnums) {
            if (storeTypeEnum.getValue()==value) {
                return storeTypeEnum.getDesc();
            }
        }
        return null;
    }

    public static Integer getValue(String desc) {
        StoreTypeEnum[] storeTypeEnums = values();
        for (StoreTypeEnum storeTypeEnum : storeTypeEnums) {
            if (storeTypeEnum.getDesc().equals(desc)) {
                return storeTypeEnum.getValue();
            }
        }
        return null;
    }

}



