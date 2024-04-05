package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class VeekerAuditDTO {

    @NotNull(message = "微客ID集合不能为空")
    @ApiModelProperty("微客ID集合")
    private List<Long> idList;
    @NotNull(message = "审核状态不能为空")
    @ApiModelProperty("审核状态 1-已同意 2-已拒绝")
    private Integer status;

}
