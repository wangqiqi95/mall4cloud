package com.mall4j.cloud.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ChooseMemberEventExchangeRecordPageDTO{

    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNo = 1;
    @ApiModelProperty(value = "每页长度",required = true)
    private Integer pageSize = 10;
    @ApiModelProperty(value = "活动ID")
    private Long eventId;
    @ApiModelProperty(value = "会员信息")
    private String userInfo;
    @ApiModelProperty(value = "导入状态 0-未确认,1-已确认")
    private Integer exportStatus;
    @ApiModelProperty(value = "发货状态")
    private Integer deliveryStatus;
    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    private Long downLoadHisId;
}
