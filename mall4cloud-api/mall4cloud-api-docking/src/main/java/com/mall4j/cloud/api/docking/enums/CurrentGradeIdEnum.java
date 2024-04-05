package com.mall4j.cloud.api.docking.enums;

public enum CurrentGradeIdEnum {

    /**
     * 1---新奇（60005）
     * 2---好奇（60006）
     * 3---炫奇（60007）
     * 4---珍奇（60008）
     */

    GRADE_HQ("1", "新奇会员"),
    GRADE_XINQ("2", "好奇会员"),
    GRADE_XQ("3", "炫奇会员"),
    GRADE_ZQ("4", "珍奇会员")
    ;

    private String value;

    private String desc;

    CurrentGradeIdEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(String value) {
        CurrentGradeIdEnum[] gradeIdEnums = values();
        for (CurrentGradeIdEnum gradeIdEnum : gradeIdEnums) {
            if (gradeIdEnum.getValue().equals(value)) {
                return gradeIdEnum.getDesc();
            }
        }
        return null;
    }

    public static String getValue(String desc) {
        CurrentGradeIdEnum[] gradeIdEnums = values();
        for (CurrentGradeIdEnum gradeIdEnum : gradeIdEnums) {
            if (gradeIdEnum.getDesc().equals(desc)) {
                return gradeIdEnum.getValue();
            }
        }
        return null;
    }

    public static CurrentGradeIdEnum instance(String value) {
        CurrentGradeIdEnum[] enums = values();
        for (CurrentGradeIdEnum inventoryStatus : enums) {
            if (inventoryStatus.getValue().equals(value)) {
                return inventoryStatus;
            }
        }
        return null;
    }
}
