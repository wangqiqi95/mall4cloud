package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessageType;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptTmessageTypeMapper;
import com.mall4j.cloud.biz.service.WeixinMaSubscriptTmessageTypeService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信小程序订阅模版消息类型
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 17:03:44
 */
@Service
public class WeixinMaSubscriptTmessageTypeServiceImpl implements WeixinMaSubscriptTmessageTypeService {

    @Autowired
    private WeixinMaSubscriptTmessageTypeMapper weixinMaSubscriptTmessageTypeMapper;

    @Override
    public PageVO<WeixinMaSubscriptTmessageType> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinMaSubscriptTmessageTypeMapper.list());
    }

    @Override
    public WeixinMaSubscriptTmessageType getById(Long id) {
        return weixinMaSubscriptTmessageTypeMapper.getById(id);
    }

    @Override
    public void save(WeixinMaSubscriptTmessageType weixinMaSubscriptTmessageType) {
        weixinMaSubscriptTmessageTypeMapper.save(weixinMaSubscriptTmessageType);
    }

    @Override
    public void update(WeixinMaSubscriptTmessageType weixinMaSubscriptTmessageType) {
        weixinMaSubscriptTmessageTypeMapper.update(weixinMaSubscriptTmessageType);
    }

    @Override
    public void deleteById(Long id) {
        weixinMaSubscriptTmessageTypeMapper.deleteById(id);
    }
}
