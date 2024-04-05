package com.mall4j.cloud.api.biz.dto.channels.response;

import lombok.Data;

/**
 * 基础响应
 * @author lt
 */
@Data
public class EcBaseResponse {
    /**
     * 错误码
     */
    private Integer errcode;
    /**
     * 错误消息
     */
    private String errmsg;
}
