package com.mall4j.cloud.user.vo;

import lombok.Data;

@Data
public class UsableGroupPushVipVO {

    private Long vipUserId;
    private Integer friendState;
    private Long staffId;
    private String staffCpUserId;
    private String vipCpUserId;
    private Integer sendStatus;
    private Long groupPushTaskId;
    private Long groupPushSonTaskId;
}
