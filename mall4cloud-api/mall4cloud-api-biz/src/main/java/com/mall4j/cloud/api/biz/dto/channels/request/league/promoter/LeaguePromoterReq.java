package com.mall4j.cloud.api.biz.dto.channels.request.league.promoter;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description 新增达人
 * @Author axin
 * @Date 2023-02-13 15:59
 **/
@Data
public class LeaguePromoterReq {
    /**
     * 视频号ID
     */
    @JsonProperty("finder_id")
    private String finderId;
}
