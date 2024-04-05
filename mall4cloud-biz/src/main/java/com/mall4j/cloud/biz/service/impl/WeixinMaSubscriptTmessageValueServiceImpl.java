package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessageValue;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptTmessageValueMapper;
import com.mall4j.cloud.biz.service.WeixinMaSubscriptTmessageValueService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信小程序订阅模版消息类型 可选值列表
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 17:03:45
 */
@Service
public class WeixinMaSubscriptTmessageValueServiceImpl implements WeixinMaSubscriptTmessageValueService {

    @Autowired
    private WeixinMaSubscriptTmessageValueMapper weixinMaSubscriptTmessageValueMapper;

    @Override
    public PageVO<WeixinMaSubscriptTmessageValue> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinMaSubscriptTmessageValueMapper.list());
    }

    @Override
    public WeixinMaSubscriptTmessageValue getById(Long id) {
        return weixinMaSubscriptTmessageValueMapper.getById(id);
    }

    @Override
    public void save(WeixinMaSubscriptTmessageValue weixinMaSubscriptTmessageValue) {
        weixinMaSubscriptTmessageValueMapper.save(weixinMaSubscriptTmessageValue);
    }

    @Override
    public void update(WeixinMaSubscriptTmessageValue weixinMaSubscriptTmessageValue) {
        weixinMaSubscriptTmessageValueMapper.update(weixinMaSubscriptTmessageValue);
    }

    @Override
    public void deleteById(Long id) {
        weixinMaSubscriptTmessageValueMapper.deleteById(id);
    }
}
