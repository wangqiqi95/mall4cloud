package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuestionnairePageDTO {
    @ApiModelProperty("问卷名称")
    private String name;
    @ApiModelProperty("活动状态0未启用，2未开始，3已开始，4，已结束")
    private Integer status;

}
