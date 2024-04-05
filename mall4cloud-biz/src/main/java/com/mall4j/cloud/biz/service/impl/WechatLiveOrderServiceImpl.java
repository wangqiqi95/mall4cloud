package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.mapper.WechatNotifyTaskMapper;
import com.mall4j.cloud.biz.model.WechatNotifyTaskDO;
import com.mall4j.cloud.biz.service.WechatLiveOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WechatLiveOrderServiceImpl implements WechatLiveOrderService {

    @Autowired
    private WechatNotifyTaskMapper notifyTaskMapper;

    /**
     * 记录订单变更信息
     *
     * @param orderId
     * @return
     */
    @Override
    public void recordOrderChange(String orderId) {
        WechatNotifyTaskDO entity = new WechatNotifyTaskDO();
        entity.setType("1");
        entity.setItemId(orderId);
        entity.setItemStatus("0");

        notifyTaskMapper.insert(entity);
    }

    /**
     * 记录订单售后变更信息
     *
     * @param orderId
     * @return
     */
    @Override
    public void recordAftersaleOrderChange(String orderId) {
        WechatNotifyTaskDO entity = new WechatNotifyTaskDO();
        entity.setType("2");
        entity.setItemId(orderId);
        entity.setItemStatus("0");

        notifyTaskMapper.insert(entity);
    }
}
