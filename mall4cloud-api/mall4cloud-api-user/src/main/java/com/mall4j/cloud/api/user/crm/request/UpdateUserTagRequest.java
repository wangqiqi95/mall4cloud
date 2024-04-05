package com.mall4j.cloud.api.user.crm.request;

import com.mall4j.cloud.api.user.crm.response.UserTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserTagRequest {
    //微信unionId mobile。Email三者填其一
    @ApiModelProperty(value = "unionId",required = false)
    private String unionId;
    private String mobile;
    private String email;

    //需要操作的标签
    @ApiModelProperty(value = "标签内容",required = false)
    private List<UserTag> operateTags;

    /**
     * BuildTagFromEnum
     */
    @ApiModelProperty(value = "打标签动作来源",required = false)
    private Integer buildTagFrom;

}
