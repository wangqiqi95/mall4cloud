package com.mall4j.cloud.api.biz.dto.channels.response.sharer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

/**
 * @Description 获取绑定的分享员
 * @Author axin
 * @Date 2023-02-14 11:52
 **/
@Data
public class SearchSharerResp extends BaseResponse {
    /**
     * 分享员openid
     */
    private String openid;

    /**
     * 分享员unionid
     */
    private String unionid;

    /**
     * 分享员昵称
     */
    private String nickname;

    /**
     * 绑定时间
     */
    @JsonProperty("bind_time")
    private Long bindTime;

    /**
     * 分享员类型
     * 0	普通分享员
     * 1	企业分享员
     */
    @JsonProperty("sharer_type")
    private Integer sharerType;
}
