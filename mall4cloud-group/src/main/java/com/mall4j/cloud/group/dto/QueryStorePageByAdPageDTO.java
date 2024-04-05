package com.mall4j.cloud.group.dto;


import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryStorePageByAdPageDTO extends PageDTO {


    @NotNull(message = "弹窗广告ID为必传项")
    @ApiModelProperty(value = "弹窗广告ID")
    private Long adId;

    @ApiModelProperty(value = "店铺关键字（名称/编号）")
    private String storeKeyword;

}
