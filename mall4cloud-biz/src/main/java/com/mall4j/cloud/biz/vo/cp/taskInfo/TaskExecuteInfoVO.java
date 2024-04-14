package com.mall4j.cloud.biz.vo.cp.taskInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TaskExecuteInfoVO {
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private Long taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    /**
     * 任务类型 1加企微好友 2好友转会员 3分享素材 4回访客户
     */
    @ApiModelProperty("任务类型 1加企微好友 2好友转会员 3分享素材 4回访客户")
    private Integer taskType;
    /**
     * 导购id
     */
    @ApiModelProperty("导购id")
    private String shoppingGuideId;
    /**
     * 状态 1完成 0未完成
     */
    @ApiModelProperty("状态 1完成 0未完成")
    private Integer status;
    /**
     * 任务时间
     */
    @ApiModelProperty("任务时间")
    private Date taskTime;
    /**
     * 需完成数量
     */
    @ApiModelProperty("需完成数量")
    private Integer clientSum;
    /**
     * 已完成数量
     */
    @ApiModelProperty("已完成数量")
    private Integer successClientSum;
}
