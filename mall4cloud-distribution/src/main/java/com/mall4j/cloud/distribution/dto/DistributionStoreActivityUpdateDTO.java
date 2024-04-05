package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DistributionStoreActivityUpdateDTO {

    @NotNull(message = "ID集合不能为空")
    @ApiModelProperty("ID集合")
    private List<Long> idList;

    @NotNull(message = "活动状态不能为空")
    @ApiModelProperty("活动状态")
    private Integer status;

}
