package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PopUpAdClickDTO {

    @NotNull(message = "广告ID为必传项")
    @ApiModelProperty("广告ID集合")
    private Long adId;

    @NotNull(message = "访问门店ID为必传项")
    @ApiModelProperty("访问门店ID")
    private Long storeId;

//    @NotNull(message = "操作入口页面必传项")
    @ApiModelProperty(value = "操作入口页面")
    private String entrance;
}
