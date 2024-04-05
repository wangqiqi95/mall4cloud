package com.mall4j.cloud.biz.dto.chat;

import com.mall4j.cloud.biz.model.chat.EndStatement;
import com.mall4j.cloud.biz.model.chat.WorkDate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 会话超时规则DTO
 */
@Data
public class SessionTimeOutDTO {

    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    private String startTime;
    private String endTime;
    @ApiModelProperty(value = "适用人员")
    private String suitPeople;
    @ApiModelProperty(value = "适用场景类型1：企微单聊2：企微群聊")
    private String type;
    private String workDate;
    private String workStartDate;
    private String workEndDate;
    @ApiModelProperty(value = "超时时效")
    private String timeOut;
    @ApiModelProperty(value = "正常结束语0：关闭，1：开启")
    private Integer normalEnd;
    private String conclusion;
    @ApiModelProperty(value = "通知提醒0：关闭，1：开启")
    private Integer remind;
    @ApiModelProperty(value = "提醒类型 1：当事人 2：提醒指定员工")
    private String remindPeople;
    @ApiModelProperty(value = "提醒时间0：立即上报，1：指定时间")
    private Integer remindOpen;
    @ApiModelProperty(value = "延迟上报时间，以小时为单位")
    private String remindTime;
    @ApiModelProperty(value = "工作时间集合")
    private List<WorkDate> workDateList;
    @ApiModelProperty(value = "结束语集合")
    private List<EndStatement> statementList;
    @ApiModelProperty(value = "提醒人")
    private String staff;
    @ApiModelProperty(value = "适用人员名称")
    private String suitPeopleName;
    @ApiModelProperty(value = "提醒人名称")
    private String staffName;
}
