package com.mall4j.cloud.group.dto.questionnaire;

import lombok.Data;

/**
 * @author baiqingtao
 * @date 2023/5/19
 */
@Data
public class QuestionnaireShopCountDTO {

    /**
     * 活动Id
     */
    private Long activityId;

    /**
     * 店铺Id `,`号 分隔
     */
    private String shopIds = "";

    /**
     * 门店数量
     */
    private Long count = 0L;
}
