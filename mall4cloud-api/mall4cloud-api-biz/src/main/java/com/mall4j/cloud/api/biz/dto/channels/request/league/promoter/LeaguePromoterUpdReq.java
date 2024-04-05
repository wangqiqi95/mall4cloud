package com.mall4j.cloud.api.biz.dto.channels.request.league.promoter;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description 编辑达人
 * @Author axin
 * @Date 2023-02-13 16:00
 **/
@Data
public class LeaguePromoterUpdReq {
    /**
     * 视频号ID
     */
    @JsonProperty("finder_id")
    private String finderId;

    /**
     * 操作类型
     * 1 取消邀请
     * 2 结束合作
     */
    @JsonProperty("type")
    private Integer type;
}
