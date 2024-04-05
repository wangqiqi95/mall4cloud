package com.mall4j.cloud.api.biz.dto.channels.request.sharer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 解绑分享员
 * @Author axin
 * @Date 2023-02-14 15:00
 **/
@Data
public class SharerUnbindReq {

    /**
     * 要解绑的分享员openId
     */
    @JsonProperty("openid_list")
    private List<String> openidList;
}
