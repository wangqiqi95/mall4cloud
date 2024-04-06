package com.mall4j.cloud.biz.constant.task;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 任务类型枚举
 */
@AllArgsConstructor
@Getter
public enum TaskTypeEnum {
    WORK_WECHAT_ADD_FRIEND(1, "加企微好友"),
    FRIEND_TO_VIP(2, "好友转会员"),
    SHARE_MATERIAL(3, "分享素材"),
    VISIT_CUSTOMER(4, "回访客户"),
    ;
    private final Integer value;
    private final String desc;

    public static TaskTypeEnum getTaskTypeEnum(Integer value) {
        return Arrays.stream(TaskTypeEnum.values()).filter(item -> ObjectUtil.equals(value, item.getValue())).findFirst().orElse(null);
    }

}
