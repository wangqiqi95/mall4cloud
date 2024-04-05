package com.mall4j.cloud.group.service;

import com.mall4j.cloud.group.model.QuestionnaireGift;
import com.mall4j.cloud.group.model.QuestionnaireUserGiftLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 问卷调查用户奖品记录表
 */
public interface QuestionnaireUserGiftLogService extends IService<QuestionnaireUserGiftLog> {

    /**
     * 保存一条记录
     * @param userId 用户ID
     * @param questionnaireGift 奖品信息
     */
    void saveUserGiftLog(Long userId, QuestionnaireGift questionnaireGift);
}
