package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinNewsTemplate;
import com.mall4j.cloud.biz.mapper.WeixinNewsTemplateMapper;
import com.mall4j.cloud.biz.service.WeixinNewsTemplateService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信图文模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:22
 */
@Service
public class WeixinNewsTemplateServiceImpl implements WeixinNewsTemplateService {

    @Autowired
    private WeixinNewsTemplateMapper weixinNewsTemplateMapper;

    @Override
    public PageVO<WeixinNewsTemplate> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinNewsTemplateMapper.list());
    }

    @Override
    public WeixinNewsTemplate getById(String id) {
        return weixinNewsTemplateMapper.getById(id);
    }

    @Override
    public void save(WeixinNewsTemplate weixinNewsTemplate) {
        weixinNewsTemplateMapper.save(weixinNewsTemplate);
    }

    @Override
    public void update(WeixinNewsTemplate weixinNewsTemplate) {
        weixinNewsTemplateMapper.update(weixinNewsTemplate);
    }

    @Override
    public void deleteById(String id) {
        weixinNewsTemplateMapper.deleteById(id);
    }
}
