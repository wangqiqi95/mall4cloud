package com.mall4j.cloud.biz.dto.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 超时回复DTO
 */
@Data
public class TimeOutLogDTO {

    private String id;
    @ApiModelProperty(value = "员工名称")
    private String staffName;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "对接客户")
    private String userName;
    @ApiModelProperty(value = "超时规则id")
    private Long timeOutId;
    private String status;
    @ApiModelProperty(value = "员工企微id")
    private List<String> staffQiWeiId;
    @ApiModelProperty(value = "客户企微id")
    private List<String> userIds;
}
