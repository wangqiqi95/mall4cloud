package com.mall4j.cloud.biz.vo.cp.taskInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 导购任务详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskExecuteDetailInfoVO {
    @ApiModelProperty("任务详情")
    private ShoppingGuideTaskDetailVO shoppingGuideTaskDetail;
}
