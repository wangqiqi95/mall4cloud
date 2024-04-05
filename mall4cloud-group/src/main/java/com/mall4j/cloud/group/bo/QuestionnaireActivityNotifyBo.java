package com.mall4j.cloud.group.bo;

import lombok.Data;

@Data
public class QuestionnaireActivityNotifyBo {
    //活动id
    private Long activityId;
    //用户答题记录
    private Long userAnswerRecordId;
}
