package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinTmessageSendlog;
import com.mall4j.cloud.biz.mapper.WeixinTmessageSendlogMapper;
import com.mall4j.cloud.biz.service.WeixinTmessageSendlogService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信模板消息推送日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 17:13:37
 */
@Service
public class WeixinTmessageSendlogServiceImpl implements WeixinTmessageSendlogService {

    @Autowired
    private WeixinTmessageSendlogMapper weixinTmessageSendlogMapper;

    @Override
    public PageVO<WeixinTmessageSendlog> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinTmessageSendlogMapper.list());
    }

    @Override
    public WeixinTmessageSendlog getById(String id) {
        return weixinTmessageSendlogMapper.getById(id);
    }

    @Override
    public void save(WeixinTmessageSendlog weixinTmessageSendlog) {
        weixinTmessageSendlogMapper.save(weixinTmessageSendlog);
    }

    @Override
    public void update(WeixinTmessageSendlog weixinTmessageSendlog) {
        weixinTmessageSendlogMapper.update(weixinTmessageSendlog);
    }

    @Override
    public void deleteById(String id) {
        weixinTmessageSendlogMapper.deleteById(id);
    }

    @Override
    public void updateWxStatus(WeixinTmessageSendlog weixinTmessageSendlog) {
        weixinTmessageSendlogMapper.updateWxStatus(weixinTmessageSendlog);
    }
}
