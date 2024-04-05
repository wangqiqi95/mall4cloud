package com.mall4j.cloud.api.biz.dto.channels.response.league.item;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

/**
 * @Description TODO
 * @Author axin
 * @Date 2023-02-13 17:43
 **/
@Data
public class ItemUpdResp extends BaseResponse {

    /**
     * 特殊推广商品计划id
     */
    @JsonProperty("info_id")
    private String infoId;
}
