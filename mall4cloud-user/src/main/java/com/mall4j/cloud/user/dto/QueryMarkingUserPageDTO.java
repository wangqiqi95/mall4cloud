package com.mall4j.cloud.user.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryMarkingUserPageDTO extends PageDTO {

    @ApiModelProperty("标签ID")
    @NotNull(message = "标签ID为必传项")
    private Long tagId;

    @ApiModelProperty("用户电话号码")
    private String vipPhone;

    @ApiModelProperty("用户卡号")
    private String vipCode;

}
