package com.mall4j.cloud.group.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireGift;
import com.mall4j.cloud.group.mapper.QuestionnaireGiftMapper;
import com.mall4j.cloud.group.service.QuestionnaireGiftService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 问卷奖品清单 实物奖品维护
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Service
public class QuestionnaireGiftServiceImpl implements QuestionnaireGiftService {

    @Autowired
    private QuestionnaireGiftMapper questionnaireGiftMapper;

    @Override
    public PageVO<QuestionnaireGift> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> questionnaireGiftMapper.list());
    }

    @Override
    public QuestionnaireGift getById(Long id) {
        return questionnaireGiftMapper.getById(id);
    }

    @Override
    public void save(QuestionnaireGift questionnaireGift) {
        questionnaireGiftMapper.save(questionnaireGift);
    }

    @Override
    public void update(QuestionnaireGift questionnaireGift) {
        questionnaireGiftMapper.update(questionnaireGift);
    }

    @Override
    public void deleteById(Long id) {
        questionnaireGiftMapper.deleteById(id);
    }
}
