package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("标签查询参数")
public class TzTagQueryParamDTO {

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty("员工集合")
    private List<Long> staffIds;

    @ApiModelProperty("门店集合")
    private List<Long> storeIds;

    @ApiModelProperty("状态：0禁用 1启用")
    private Integer status;

}
