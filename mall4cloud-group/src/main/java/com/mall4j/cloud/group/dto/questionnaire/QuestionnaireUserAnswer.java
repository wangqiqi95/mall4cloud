package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuestionnaireUserAnswer {

    @ApiModelProperty("题目序号")
    private Integer seq;
    @ApiModelProperty("题目标题")
    private String name;
    @ApiModelProperty("题目答案")
    private String value;

}
