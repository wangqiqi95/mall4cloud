package com.mall4j.cloud.group.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireShop;
import com.mall4j.cloud.group.mapper.QuestionnaireShopMapper;
import com.mall4j.cloud.group.service.QuestionnaireShopService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 问卷适用门店
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Service
public class QuestionnaireShopServiceImpl implements QuestionnaireShopService {

    @Autowired
    private QuestionnaireShopMapper questionnaireShopMapper;

    @Override
    public PageVO<QuestionnaireShop> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> questionnaireShopMapper.list());
    }

    @Override
    public QuestionnaireShop getById(Long id) {
        return questionnaireShopMapper.getById(id);
    }

    @Override
    public void save(QuestionnaireShop questionnaireShop) {
        questionnaireShopMapper.save(questionnaireShop);
    }

    @Override
    public void update(QuestionnaireShop questionnaireShop) {
        questionnaireShopMapper.update(questionnaireShop);
    }

    @Override
    public void deleteById(Long id) {
        questionnaireShopMapper.deleteById(id);
    }
}
