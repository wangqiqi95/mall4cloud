package com.mall4j.cloud.group.vo.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 解析excel返回体VO
 * @date 2023/5/30
 */
@Data
public class QuestionnaireResolveExcelVO implements Serializable {

    @ApiModelProperty("redisKey")
    private String redisKey;

    @ApiModelProperty("生效数量")
    private Integer count = 0;
}
