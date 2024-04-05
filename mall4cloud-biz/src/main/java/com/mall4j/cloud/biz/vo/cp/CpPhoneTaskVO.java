package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 引流手机号任务VO
 *
 * @author gmq
 * @date 2023-10-30 17:13:30
 */
@Data
public class CpPhoneTaskVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("执行开始时间")
    private Date startTime;

    @ApiModelProperty("执行结束时间")
    private Date endTime;

    @ApiModelProperty("目标客户源：0全部/1按来源/2按创建时间")
    private Integer userType;

    @ApiModelProperty("每人每天分配数量")
    private Integer dailyAllocationTotal;

    @ApiModelProperty("累计分配数量")
    private Integer allocationTotal;

    @ApiModelProperty("引导语")
    private String slogan;

    @ApiModelProperty("开启状态：0未开启/1已开启")
    private Integer openStatus;

    @ApiModelProperty("状态：0未开始/1已开始/2进行中/3已结束")
    private Integer status;

    @ApiModelProperty("")
    private String createBy;

    @ApiModelProperty("")
    private String updateBy;

    @ApiModelProperty("目标手机号来源")
    private Integer importFrom;

    @ApiModelProperty("目标手机号开始时间")
    private Date phoneStartTime;

    @ApiModelProperty("目标手机号结束时间")
    private Date phoneEndTime;

}
