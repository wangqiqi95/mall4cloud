package com.mall4j.cloud.api.platform.constant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StaffRoleTypeEnum {

    GUIDE(1, "导购"),
    MANAGER(2, "店长"),
    ASSISTANT(3, "店务");

    private final Integer value;
    private final String desc;

    public static String getDesc(Integer value) {
        StaffRoleTypeEnum[] staffRoleTypeEnums = values();
        for (StaffRoleTypeEnum staffRoleTypeEnum : staffRoleTypeEnums) {
            if (staffRoleTypeEnum.getValue()==value) {
                return staffRoleTypeEnum.getDesc();
            }
        }
        return null;
    }

    public static Integer getValue(String desc) {
        StaffRoleTypeEnum[] staffRoleTypeEnums = values();
        for (StaffRoleTypeEnum staffRoleTypeEnum : staffRoleTypeEnums) {
            if (staffRoleTypeEnum.getDesc().equals(desc)) {
                return staffRoleTypeEnum.getValue();
            }
        }
        return null;
    }
}
