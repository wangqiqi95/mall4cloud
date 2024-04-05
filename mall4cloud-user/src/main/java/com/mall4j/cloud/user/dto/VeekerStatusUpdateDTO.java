package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class VeekerStatusUpdateDTO {

    @NotNull(message = "微客ID集合不能为空")
    @ApiModelProperty("微客ID集合")
    private List<Long> idList;
    @NotNull(message = "微客状态不能为空")
    @ApiModelProperty("微客状态 0-禁用 1-启用 2-拉黑")
    private Integer status;

}
