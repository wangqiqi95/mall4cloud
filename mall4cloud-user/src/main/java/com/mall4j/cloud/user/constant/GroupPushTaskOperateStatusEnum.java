package com.mall4j.cloud.user.constant;

import lombok.Getter;

@Getter
public enum GroupPushTaskOperateStatusEnum {
    /**
     * 整合版
     * 0 创建中
     * 1 启用中
     * 5 草稿
     * 6 已完成
     * 7 禁用
     */
    CREATING(0),
    SUCCESS(1),
    DRAFT(5),
    FINISH(6),
    DISBALE(7)
    ;

    private Integer operateStatus;

    GroupPushTaskOperateStatusEnum(Integer operateStatus){
        this.operateStatus = operateStatus;
    }
}
