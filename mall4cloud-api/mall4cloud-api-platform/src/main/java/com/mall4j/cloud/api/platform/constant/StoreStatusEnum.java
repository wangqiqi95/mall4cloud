package com.mall4j.cloud.api.platform.constant;

/**
 * 门店状态
 *
 * @author gmq
 */
public enum StoreStatusEnum {

    //门店状态: 0:关闭 1:营业 2:强制关闭 3:审核中 4:审核失败

    COLSE(0, "关闭"),

    BUSINESS(1, "营业"),

    EXIT_CLOSE(2, "强制关闭"),

    EXAMINE_ON(3, "审核中"),

    EXAMINE_OUT(4, "审核失败"),
    ;

    private final Integer value;
    private final String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    StoreStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        StoreStatusEnum[] storeStatusEnums = values();
        for (StoreStatusEnum storeStatusEnum : storeStatusEnums) {
            if (storeStatusEnum.getValue()==value) {
                return storeStatusEnum.getDesc();
            }
        }
        return null;
    }

    public static Integer getValue(String desc) {
        StoreStatusEnum[] storeStatusEnums = values();
        for (StoreStatusEnum storeStatusEnum : storeStatusEnums) {
            if (storeStatusEnum.getDesc().equals(desc)) {
                return storeStatusEnum.getValue();
            }
        }
        return null;
    }

}



