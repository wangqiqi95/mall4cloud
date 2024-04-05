package com.mall4j.cloud.user.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QuerySecondGradeDTO {

    @ApiModelProperty("一级分类ID")
    @NotNull(message = "一级分类ID为必传项")
    private Long parentId;


//    @ApiModelProperty("")

}
