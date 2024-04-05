package com.mall4j.cloud.group.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireUser;
import com.mall4j.cloud.group.mapper.QuestionnaireUserMapper;
import com.mall4j.cloud.group.service.QuestionnaireUserService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 问卷会员名单
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Service
public class QuestionnaireUserServiceImpl implements QuestionnaireUserService {

    @Autowired
    private QuestionnaireUserMapper questionnaireUserMapper;

    @Override
    public PageVO<QuestionnaireUser> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> questionnaireUserMapper.list());
    }

    @Override
    public QuestionnaireUser getById(Long id) {
        return questionnaireUserMapper.getById(id);
    }

    @Override
    public void save(QuestionnaireUser questionnaireUser) {
        questionnaireUserMapper.save(questionnaireUser);
    }

    @Override
    public void update(QuestionnaireUser questionnaireUser) {
        questionnaireUserMapper.update(questionnaireUser);
    }

    @Override
    public void deleteById(Long id) {
        questionnaireUserMapper.deleteById(id);
    }
}
