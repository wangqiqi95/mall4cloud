package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnswerPageDTO {
    @ApiModelProperty("0未提交 1提交")
    private Integer submitted;
    @ApiModelProperty(value = "问卷id")
    private Long activityId;
    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("是否过滤无奖品数据")
    private Integer isFilterNoPrize;
}
