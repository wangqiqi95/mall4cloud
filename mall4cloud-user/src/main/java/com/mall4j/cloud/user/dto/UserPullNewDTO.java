package com.mall4j.cloud.user.dto;

import lombok.Data;

/**
 * @Description 用户拉新
 * @Author axin
 * @Date 2022-10-12 15:12
 **/
@Data
public class UserPullNewDTO {
    /**
     * 邀请人用户id
     */
    private Long inviterUserId;

    /**
     * 被邀请人用户id
     */
    private Long inviteeUserId;

    /**
     * 拉新类型 1 积分清零活动
     */
    private Integer type;


}
