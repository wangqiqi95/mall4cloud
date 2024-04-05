package com.mall4j.cloud.biz.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class TaskManagementInfoDTO {


    @ApiModelProperty("门店id")
    private Integer storeId;

    @ApiModelProperty(value = "任务客户列表")
    private List<Integer> userIds;

}
