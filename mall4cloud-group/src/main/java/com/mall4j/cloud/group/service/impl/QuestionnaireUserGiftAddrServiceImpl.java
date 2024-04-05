package com.mall4j.cloud.group.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireUserGiftAddr;
import com.mall4j.cloud.group.mapper.QuestionnaireUserGiftAddrMapper;
import com.mall4j.cloud.group.service.QuestionnaireUserGiftAddrService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户奖品配送地址
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:11:00
 */
@Service
public class QuestionnaireUserGiftAddrServiceImpl implements QuestionnaireUserGiftAddrService {

    @Autowired
    private QuestionnaireUserGiftAddrMapper questionnaireUserGiftAddrMapper;

    @Override
    public PageVO<QuestionnaireUserGiftAddr> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> questionnaireUserGiftAddrMapper.list());
    }

    @Override
    public QuestionnaireUserGiftAddr getById(Long id) {
        return questionnaireUserGiftAddrMapper.getById(id);
    }

    @Override
    public void save(QuestionnaireUserGiftAddr questionnaireUserGiftAddr) {
        questionnaireUserGiftAddrMapper.save(questionnaireUserGiftAddr);
    }

    @Override
    public void update(QuestionnaireUserGiftAddr questionnaireUserGiftAddr) {
        questionnaireUserGiftAddrMapper.update(questionnaireUserGiftAddr);
    }

    @Override
    public void deleteById(Long id) {
        questionnaireUserGiftAddrMapper.deleteById(id);
    }
}
