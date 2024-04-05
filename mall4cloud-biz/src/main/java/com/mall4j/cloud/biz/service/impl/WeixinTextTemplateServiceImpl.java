package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinTextTemplate;
import com.mall4j.cloud.biz.mapper.WeixinTextTemplateMapper;
import com.mall4j.cloud.biz.service.WeixinTextTemplateService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信文本模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:52:50
 */
@Service
public class WeixinTextTemplateServiceImpl implements WeixinTextTemplateService {

    @Autowired
    private WeixinTextTemplateMapper weixinTextTemplateMapper;

    @Override
    public PageVO<WeixinTextTemplate> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinTextTemplateMapper.list());
    }

    @Override
    public WeixinTextTemplate getById(String id) {
        return weixinTextTemplateMapper.getById(id);
    }

    @Override
    public void save(WeixinTextTemplate weixinTextTemplate) {
        weixinTextTemplateMapper.save(weixinTextTemplate);
    }

    @Override
    public void update(WeixinTextTemplate weixinTextTemplate) {
        weixinTextTemplateMapper.update(weixinTextTemplate);
    }

    @Override
    public void deleteById(String id) {
        weixinTextTemplateMapper.deleteById(id);
    }
}
