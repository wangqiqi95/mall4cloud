package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 任务分享方式枚举
 */
@AllArgsConstructor
@Getter
public enum TaskShareTypeEnum {
    WORK_WECHAT_PRIVATE_CHAT(1, "企微单聊"),
    WORK_WECHAT_BULK_SENDING(2, "企微群发"),
    WORK_WECHAT_POST_MOMENT(3, "发朋友圈"),
    WORK_WECHAT_CUSTOMER_BASE(4, "群发客户群"),
    ;
    private final Integer value;
    private final String desc;

    public static TaskShareTypeEnum getEnum(Integer value) {
        return Arrays.stream(TaskShareTypeEnum.values()).filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }
}
