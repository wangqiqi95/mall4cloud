package com.mall4j.cloud.api.biz.dto.channels.request.sharer;

import lombok.Data;

/**
 * @Description 分享员绑定入参
 * @Author axin
 * @Date 2023-02-13 11:33
 **/
@Data
public class SharerBindReq {
    /**
     * 邀请的用户微信号
     */
    private String username;
}
