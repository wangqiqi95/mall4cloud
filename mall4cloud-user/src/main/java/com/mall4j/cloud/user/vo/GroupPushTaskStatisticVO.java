package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GroupPushTaskStatisticVO {

    @ApiModelProperty("子任务ID")
    private Long sonTaskId;
    @ApiModelProperty("子任务名称")
    private String sonTaskName;
    @ApiModelProperty("未完成推送数")
    private Integer notPushCount;
    @ApiModelProperty("触达任务完成数")
    private Integer pushIssueFinishCount;
    @ApiModelProperty("推送完成率")
    private BigDecimal pushRate;
   /* @ApiModelProperty("执行导购总数")
    private Integer staffCount;*/
    @ApiModelProperty("触达客户总数")
    private Integer issueCount;
    @ApiModelProperty(value = "1-正常/2-中止")
    private Integer status;

}
