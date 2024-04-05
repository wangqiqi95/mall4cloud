package com.mall4j.cloud.user.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserTagOperationBO {


    @ApiModelProperty(value = "操作类型，1新增，2删除")
    private Integer operationState;

    @ApiModelProperty(value = "操作人")
    private Long doUser;

    @ApiModelProperty(value = "被操作人会员卡号")
    private String beVipCode;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "标签组ID")
    private Long groupId;

}
