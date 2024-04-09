package com.mall4j.cloud.biz.vo.cp.taskInfo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务客户表
 */
@Data
public class TaskClientInfoVO {
    @ApiModelProperty("客户id)")
    private String clientId;
    @ApiModelProperty("客户昵称")
    private String clientNickname;
    @ApiModelProperty("客户电话")
    private String clientPhone;
    @ApiModelProperty("客户备注")
    private String clientRemark;
}

