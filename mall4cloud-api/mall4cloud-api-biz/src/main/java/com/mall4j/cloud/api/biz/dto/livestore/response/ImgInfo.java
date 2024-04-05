
package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImgInfo {

    @JsonProperty("media_id")
    private String mediaId;
    @JsonProperty("temp_img_url")
    private String tempImgUrl;

}
