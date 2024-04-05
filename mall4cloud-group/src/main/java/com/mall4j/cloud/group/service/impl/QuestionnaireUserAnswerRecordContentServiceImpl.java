package com.mall4j.cloud.group.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireUserAnswerRecordContent;
import com.mall4j.cloud.group.mapper.QuestionnaireUserAnswerRecordContentMapper;
import com.mall4j.cloud.group.service.QuestionnaireUserAnswerRecordContentService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 问卷 会员答题记录内容
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Service
public class QuestionnaireUserAnswerRecordContentServiceImpl implements QuestionnaireUserAnswerRecordContentService {

    @Autowired
    private QuestionnaireUserAnswerRecordContentMapper questionnaireUserAnswerRecordContentMapper;

    @Override
    public PageVO<QuestionnaireUserAnswerRecordContent> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> questionnaireUserAnswerRecordContentMapper.list());
    }

    @Override
    public QuestionnaireUserAnswerRecordContent getById(Long id) {
        return questionnaireUserAnswerRecordContentMapper.getById(id);
    }

    @Override
    public void save(QuestionnaireUserAnswerRecordContent questionnaireUserAnswerRecordContent) {
        questionnaireUserAnswerRecordContentMapper.save(questionnaireUserAnswerRecordContent);
    }

    @Override
    public void update(QuestionnaireUserAnswerRecordContent questionnaireUserAnswerRecordContent) {
        questionnaireUserAnswerRecordContentMapper.update(questionnaireUserAnswerRecordContent);
    }

    @Override
    public void deleteById(Long id) {
        questionnaireUserAnswerRecordContentMapper.deleteById(id);
    }
}
