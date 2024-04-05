
package com.mall4j.cloud.biz.dto.live;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

@Data
public class LiveMediaResponse extends BaseResponse {

    @JsonProperty("type")
    private String type;

    @JsonProperty("media_id")
    private String mediaId;

    @JsonProperty("created_at")
    private String createdAt;

}
