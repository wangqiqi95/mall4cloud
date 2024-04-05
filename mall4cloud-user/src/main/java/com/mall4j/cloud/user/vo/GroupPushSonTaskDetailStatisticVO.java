package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class GroupPushSonTaskDetailStatisticVO {

    @ApiModelProperty("0未完成，1完成")
    private Integer finishState;
    @ApiModelProperty("完成时间")
    private Date finishTime;
    @ApiModelProperty("员工名称")
    private String staffName;
    @ApiModelProperty("触达任务完成数")
    private Integer pushIssueFinishCount;
    @ApiModelProperty("触达客户总数")
    private Integer issueCount;

}
