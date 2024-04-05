package com.mall4j.cloud.api.user.crm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QueryUserTagRequest {
    @ApiModelProperty("微信unionId mobile。Email三者填其一")
    private String unionid;
    private String mobile;
    private String email;

    @ApiModelProperty("标签id列表 必传")
    private List<String> tagIds;

}
