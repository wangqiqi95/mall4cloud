package com.mall4j.cloud.group.dto.questionnaire;

import lombok.Data;

import java.util.List;

/**
 * @date 2023/6/12
 */
@Data
public class QuestionnaireUpdateExcelDTO {
    private String redisKey;

    private Long activityId;

    private List<Long> removeUserIds;

    private Boolean isRemoveAllUser = Boolean.FALSE;
}
