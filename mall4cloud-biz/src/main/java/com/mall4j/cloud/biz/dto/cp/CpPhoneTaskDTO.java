package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 引流手机号任务DTO
 *
 * @author gmq
 * @date 2023-10-30 17:13:30
 */
@Data
public class CpPhoneTaskDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty(value = "任务名称",required = true)
    private String taskName;

    @ApiModelProperty(value = "执行开始时间",required = true)
    private Date startTime;

    @ApiModelProperty(value = "执行结束时间",required = true)
    private Date endTime;

    @ApiModelProperty(value = "目标客户源：0全部/1按来源/2按创建时间",required = true)
    private Integer userType;

    @ApiModelProperty(value = "每人每天分配数量",required = true)
    private Integer dailyAllocationTotal;

    @ApiModelProperty("累计分配数量")
    private Integer allocationTotal;

    @ApiModelProperty(value = "引导语",required = true)
    private String slogan;

    @ApiModelProperty("开启状态：0未开启/1已开启")
    private Integer openStatus;

    @ApiModelProperty("目标手机号来源")
    private Integer importFrom;

    @ApiModelProperty("目标手机号开始时间")
    private Date phoneStartTime;

    @ApiModelProperty("目标手机号结束时间")
    private Date phoneEndTime;

    @ApiModelProperty(value = "执行员工",required = true)
    private List<CpPhoneTaskStaffDTO> staffs;

    @ApiModelProperty(value = "是否保存并且提醒员工：true提醒/false不提醒",required = true)
    private boolean saveAndSend=false;

}
