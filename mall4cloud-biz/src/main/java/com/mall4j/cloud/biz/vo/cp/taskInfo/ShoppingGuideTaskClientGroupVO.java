package com.mall4j.cloud.biz.vo.cp.taskInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 导购任务客户群信息
 */
@Data
public class ShoppingGuideTaskClientGroupVO {
    @ApiModelProperty("客户群id)")
    private String clientGroupId;
    @ApiModelProperty("客户群名称)")
    private String clientGroupName;
    @ApiModelProperty("状态 1完成 0未完成)")
    private Integer status;
    @ApiModelProperty("结束时间)")
    private Date endTime;
}
