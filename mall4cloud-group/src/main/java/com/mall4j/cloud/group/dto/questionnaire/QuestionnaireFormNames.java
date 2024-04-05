package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuestionnaireFormNames {
    /**
     * 序号
     */
    @ApiModelProperty("题目序号")
    private Integer seq;

    /**
     * 题目标题
     */
    @ApiModelProperty("题目标题")
    private String name;

    /**
     * 是否必填
     */
    @ApiModelProperty("是否必填")
    private Integer required;

    /**
     * 单选或多选(1单选2多选)
     */
    @ApiModelProperty("单选或多选")
    private Integer radioOrCheckbox;

    /**
     * 值
     */
    @ApiModelProperty(value = "value", hidden = true)
    private String value;
}
