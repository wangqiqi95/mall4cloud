package com.mall4j.cloud.api.biz.dto.channels.response.league.item;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * @Description 批量新增返回参数
 * @Author axin
 * @Date 2023-02-13 16:51
 **/
@Data
public class BatchAddResp extends BaseResponse {
    @JsonProperty("result_info_list")
    private List<ResultInfo> resultInfoList;

    @Data
    public static class ResultInfo extends BaseResponse{
        /**
         * 商品id
         */
        @JsonProperty("product_id")
        private String productId;

        /**
         * 特殊推广商品计划id
         */
        @JsonProperty("info_id")
        private String infoId;

        @JsonProperty("fail_finder_ids")
        private List<String> failFinderIds;
    }
}
