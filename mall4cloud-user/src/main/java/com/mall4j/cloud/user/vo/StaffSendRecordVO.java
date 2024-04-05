package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StaffSendRecordVO {

    @ApiModelProperty("主任务ID")
    private Long pushTaskId;
    @ApiModelProperty("主任务名称")
    private String pushTaskName;
    @ApiModelProperty("子任务ID")
    private Long groupPushSonTaskId;
    @ApiModelProperty("子任务名称")
    private String sonTaskName;
    @ApiModelProperty("导购ID")
    private Long staffId;
    @ApiModelProperty("导购编号")
    private String staffNo;
    @ApiModelProperty("导购名称")
    private String staffNickName;
    @ApiModelProperty("服务门店ID")
    private Long staffStoreId;
    @ApiModelProperty("服务门店编码")
    private String staffStoreCode;
    @ApiModelProperty("服务门店名称")
    private String staffStoreName;
    @ApiModelProperty("会员卡号")
    private String vipCode;
    @ApiModelProperty("0未触达，其余皆为触达")
    private Integer sendStatus;
    @ApiModelProperty("触达类型 1:1v1，2：批量群发")
    private Integer sendModel;
    @ApiModelProperty("触达类型")
    private String sendModelRemark;
    @ApiModelProperty("是否好友，0未加好友，1已加好友")
    private Integer friendState;
    @ApiModelProperty("触达状态备注（导出参数，前端不需要关注")
    private String sendRemark;
    @ApiModelProperty("触达时间")
    private LocalDateTime finishTime;
    @ApiModelProperty("会员ID（导出参数，前端不需要关注）")
    private Long userId;
    @ApiModelProperty("添加备注（导出参数，前端不需要关注）")
    private String addRemark;

}
