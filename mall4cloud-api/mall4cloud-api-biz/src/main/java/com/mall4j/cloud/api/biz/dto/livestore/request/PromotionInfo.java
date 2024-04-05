package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PromotionInfo {

    /**
     * promoter_id
     */
    @JsonProperty("promoter_id")
    private String promoterId;
    /**
     * finder_nickname
     */
    @JsonProperty("finder_nickname")
    private String finderNickname;
    /**
     * sharer_openid
     */
    @JsonProperty("sharer_openid")
    private String sharerOpenid;
}
