package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReceivePrizeDTO {

    @ApiModelProperty("问卷提交记录id")
    private Long id;

    @ApiModelProperty("活动ID")
    private Long activityId;

    @ApiModelProperty(hidden = true)
    private Long userId;

    @ApiModelProperty(hidden = true)
    private Long storeId;
}
