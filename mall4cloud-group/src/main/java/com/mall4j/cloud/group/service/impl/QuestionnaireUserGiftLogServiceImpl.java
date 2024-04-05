package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.group.model.QuestionnaireGift;
import com.mall4j.cloud.group.model.QuestionnaireUserGiftLog;
import com.mall4j.cloud.group.service.QuestionnaireUserGiftLogService;
import com.mall4j.cloud.group.mapper.QuestionnaireUserGiftLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 *
 */
@Service
public class QuestionnaireUserGiftLogServiceImpl extends ServiceImpl<QuestionnaireUserGiftLogMapper, QuestionnaireUserGiftLog>
    implements QuestionnaireUserGiftLogService{

    @Autowired
    private QuestionnaireUserGiftLogMapper questionnaireUserGiftLogMapper;

    @Override
    public void saveUserGiftLog(Long userId, QuestionnaireGift questionnaireGift) {
        if (Objects.isNull(questionnaireGift)){
            return;
        }

        String giftValueStr = StrUtil.format("giftType:[{}], giftId:[{}], giftName:[{}], stock:[{}], gameType:[{}]",
                questionnaireGift.getGiftType(),
                questionnaireGift.getGiftId(),
                questionnaireGift.getGiftName(),
                questionnaireGift.getStock(),
                questionnaireGift.getGameType());

        QuestionnaireUserGiftLog questionnaireUserGiftLog = new QuestionnaireUserGiftLog();
        questionnaireUserGiftLog.setUserId(userId);
        questionnaireUserGiftLog.setQuestionnaireGiftId(questionnaireGift.getId());
        questionnaireUserGiftLog.setQuestionnaireGiftValue(giftValueStr);

        questionnaireUserGiftLogMapper.insertOne(questionnaireUserGiftLog);
    }

}




