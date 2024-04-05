package com.mall4j.cloud.api.biz.dto.channels.request.sharer;

import lombok.Data;

/**
 * @Description 获取绑定的分享员
 * @Author axin
 * @Date 2023-02-14 11:50
 **/
@Data
public class SearchSharerReq {
    /**
     * 和微信号二选一
     */
    private String openid;

    /**
     * 和openid二选一
     */
    private String username;
}
