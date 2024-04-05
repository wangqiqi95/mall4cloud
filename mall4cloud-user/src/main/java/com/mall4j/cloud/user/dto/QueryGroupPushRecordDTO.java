package com.mall4j.cloud.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QueryGroupPushRecordDTO {

    @NotNull(message = "开始时间为必传项")
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @NotNull(message = "结束时间为必传项")
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "销售名称")
    private String staffName;

    @ApiModelProperty(value = "任务模式：0-客户群发 1-群群发")
    private Integer taskMode;

    @ApiModelProperty(value = "0-待发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private Integer sendStatus;
}
