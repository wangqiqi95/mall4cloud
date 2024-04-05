package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class TaskSendDetailVO  {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("推送id")
    private Long pushId;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("员工企业微信id")
    private String userId;

    @ApiModelProperty("所属店id")
    private Long storeId;

    @ApiModelProperty("所属店名称")
    private String storeName;

    @ApiModelProperty("是否转发 0 未转发  1 已转发")
    private Integer isRelay;

    @ApiModelProperty("完成时间")
    private Date completeTime;

    @ApiModelProperty("是否完成发送 0 待发送 1 已发送 2 发送失败")
    private Integer sendStatus;

    @ApiModelProperty("创建时间")
    private Date createTime;
}