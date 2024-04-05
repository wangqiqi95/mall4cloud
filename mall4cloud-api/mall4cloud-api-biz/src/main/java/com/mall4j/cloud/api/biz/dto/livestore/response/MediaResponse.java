
package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

@Data
public class MediaResponse extends BaseResponse {

    @JsonProperty("img_info")
    private ImgInfo imgInfo;

}
