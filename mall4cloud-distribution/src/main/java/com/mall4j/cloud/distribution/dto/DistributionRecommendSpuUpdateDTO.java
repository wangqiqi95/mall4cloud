package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DistributionRecommendSpuUpdateDTO {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty("主键ID")
    private List<Long> idList;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

}
