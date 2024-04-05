package com.mall4j.cloud.user.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StaffSonTaskSendCountVO {

    @ApiModelProperty("完成推送个数")
    private Integer sendCount;

    @ApiModelProperty("导购ID")
    private Long staffId;

}
