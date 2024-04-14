package com.mall4j.cloud.biz.vo.cp.taskInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 导购任务详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingGuideTaskDetailVO {
    @ApiModelProperty("任务id")
    private Long taskId;
    @ApiModelProperty("任务名称")
    private String taskName;
    @ApiModelProperty("任务类型 1加企微好友 2好友转会员 3分享素材 4回访客户")
    private Integer taskType;
    @ApiModelProperty("任务开始时间")
    private Date taskStartTime;
    @ApiModelProperty("任务结束时间")
    private Date taskEndTime;
    @ApiModelProperty("任务目标。部分任务不存在比例，该字段存储")
    private String taskTarget;
    @ApiModelProperty("任务目标比例")
    private Double taskTargetScale;
    @ApiModelProperty("话术")
    private String speechSkills;
    @ApiModelProperty("素材信息")
    private List<TaskMaterialInfoVO> taskMaterialInfo;
}
