package com.mall4j.cloud.api.biz.dto.channels.response.sharer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * @Description 解绑分享员
 * @Author axin
 * @Date 2023-02-14 14:59
 **/
@Data
public class SharerUnbindResp extends BaseResponse {

    /**
     * 成功列表
     */
    @JsonProperty("success_openid")
    private List<String> successOpenid;

    /**
     * 失败列表，可重试
     */
    @JsonProperty("fail_openid")
    private List<String> failOpenid;

    /**
     * 拒绝列表，不可重试（openid错误，未到解绑时间等）
     */
    @JsonProperty( "refuse_openid")
    private List<String> refuseOpenid;
}
