package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubmitDTO {

    @ApiModelProperty("问卷id")
    private Long activityId;

    @ApiModelProperty(hidden = true)
    private Long userId;

    @ApiModelProperty(hidden = true)
    private Long storeId;

    @ApiModelProperty("问卷答案")
    private List<QuestionnaireUserAnswer> answers;

}
