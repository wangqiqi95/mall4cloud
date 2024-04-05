package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.NotifyTemplateShop;
import com.mall4j.cloud.biz.mapper.NotifyTemplateShopMapper;
import com.mall4j.cloud.biz.service.NotifyTemplateShopService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
@Service
public class NotifyTemplateShopServiceImpl implements NotifyTemplateShopService {

    @Autowired
    private NotifyTemplateShopMapper notifyTemplateShopMapper;

    @Override
    public PageVO<NotifyTemplateShop> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> notifyTemplateShopMapper.list());
    }

    @Override
    public NotifyTemplateShop getByNotifyShopId(Long notifyShopId) {
        return notifyTemplateShopMapper.getByNotifyShopId(notifyShopId);
    }

    @Override
    public void save(NotifyTemplateShop notifyTemplateShop) {
        notifyTemplateShopMapper.save(notifyTemplateShop);
    }

    @Override
    public void update(NotifyTemplateShop notifyTemplateShop) {
        notifyTemplateShopMapper.update(notifyTemplateShop);
    }

    @Override
    public void deleteById(Long notifyShopId) {
        notifyTemplateShopMapper.deleteById(notifyShopId);
    }
}
