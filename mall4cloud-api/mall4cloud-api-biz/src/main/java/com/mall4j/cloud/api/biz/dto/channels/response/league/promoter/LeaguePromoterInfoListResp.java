package com.mall4j.cloud.api.biz.dto.channels.response.league.promoter;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * @Description 联盟达人
 * @Author axin
 * @Date 2023-02-13 15:13
 **/
@Data
public class LeaguePromoterInfoListResp extends BaseResponse {

    /**
     * 视频号ID
     */
    @JsonProperty("finder_ids")
    private List<String> finderIds;

    /**
     * 达人总数
     */
    @JsonProperty("total_num")
    private Integer totalNum;
}
