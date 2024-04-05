package com.mall4j.cloud.api.user.crm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QueryTagCategoryPageRequest {

    @ApiModelProperty("目录id")
    private List<Integer> categoryIds;
    @ApiModelProperty("页码 从0开始  默认0")
    private Integer pageNo;
    @ApiModelProperty("页大小 默认20")
    private Integer pageSize;



}
