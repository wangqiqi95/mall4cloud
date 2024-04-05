package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.vo.WeixinMaSubscriptTmessageOptionalValueVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessageOptionalValue;
import com.mall4j.cloud.biz.mapper.WeixinMaSubscriptTmessageOptionalValueMapper;
import com.mall4j.cloud.biz.service.WeixinMaSubscriptTmessageOptionalValueService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 微信小程序订阅模版消息 值
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 17:03:45
 */
@Service
public class WeixinMaSubscriptTmessageOptionalValueServiceImpl implements WeixinMaSubscriptTmessageOptionalValueService {

    @Autowired
    private WeixinMaSubscriptTmessageOptionalValueMapper weixinMaSubscriptTmessageOptionalValueMapper;

    @Override
    public PageVO<WeixinMaSubscriptTmessageOptionalValue> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinMaSubscriptTmessageOptionalValueMapper.list());
    }

    @Override
    public WeixinMaSubscriptTmessageOptionalValue getById(Long id) {
        return weixinMaSubscriptTmessageOptionalValueMapper.getById(id);
    }

    @Override
    public void save(WeixinMaSubscriptTmessageOptionalValue weixinMaSubscriptTmessageOptionalValue) {
        weixinMaSubscriptTmessageOptionalValueMapper.save(weixinMaSubscriptTmessageOptionalValue);
    }

    @Override
    public void update(WeixinMaSubscriptTmessageOptionalValue weixinMaSubscriptTmessageOptionalValue) {
        weixinMaSubscriptTmessageOptionalValueMapper.update(weixinMaSubscriptTmessageOptionalValue);
    }

    @Override
    public void deleteById(Long id) {
        weixinMaSubscriptTmessageOptionalValueMapper.deleteById(id);
    }

    @Override
    public List<WeixinMaSubscriptTmessageOptionalValueVO> getByTypeId(Integer typeId) {
        return weixinMaSubscriptTmessageOptionalValueMapper.getByTypeId(typeId);
    }
}
