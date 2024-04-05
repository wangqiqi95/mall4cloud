package com.mall4j.cloud.api.user.crm.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QueryCategoryResponse {
    @ApiModelProperty("分组id")
    private Integer id;
    @ApiModelProperty("分组名称")
    private String name;
    @ApiModelProperty("父级Id")
    private Integer parentId;
    @ApiModelProperty("根分组id")
    private Integer rootId;
    @ApiModelProperty("是否为外部目录")
    private Boolean isExternal;
    @ApiModelProperty("外部目录id")
    private String externalCategoryId;
    @ApiModelProperty("下一级目录信息")
    private List<QueryCategoryResponse> openCategoryVos;
}
