package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.group.model.QuestionnaireUserGiftLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Entity com.mall4j.cloud.group.model.QuestionnaireUserGiftLog
 */
public interface QuestionnaireUserGiftLogMapper extends BaseMapper<QuestionnaireUserGiftLog> {

    /**
     * 保存一条
     * @param questionnaireUserGiftLog bo
     */
    default void insertOne(QuestionnaireUserGiftLog questionnaireUserGiftLog){
        this.insert(questionnaireUserGiftLog);
    }
}




