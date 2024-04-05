package com.mall4j.cloud.biz.dto.live;

import lombok.Data;

@Data
public class LiveUserRespInfo {

    /**
     * 主播微信号
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String headingimg;
    /**
     * 角色列表
     */
    private Integer[] roleList;
    /**
     * openId
     */
    private String openid;
}
