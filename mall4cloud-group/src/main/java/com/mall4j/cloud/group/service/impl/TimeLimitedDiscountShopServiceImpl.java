package com.mall4j.cloud.group.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.TimeLimitedDiscountShop;
import com.mall4j.cloud.group.mapper.TimeLimitedDiscountShopMapper;
import com.mall4j.cloud.group.service.TimeLimitedDiscountShopService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 限时调价活动 商铺
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:14
 */
@Service
public class TimeLimitedDiscountShopServiceImpl implements TimeLimitedDiscountShopService {

    @Autowired
    private TimeLimitedDiscountShopMapper timeLimitedDiscountShopMapper;

    @Override
    public PageVO<TimeLimitedDiscountShop> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> timeLimitedDiscountShopMapper.list());
    }

    @Override
    public TimeLimitedDiscountShop getById(Long id) {
        return timeLimitedDiscountShopMapper.getById(id);
    }

    @Override
    public void save(TimeLimitedDiscountShop timeLimitedDiscountShop) {
        timeLimitedDiscountShopMapper.save(timeLimitedDiscountShop);
    }

    @Override
    public void update(TimeLimitedDiscountShop timeLimitedDiscountShop) {
        timeLimitedDiscountShopMapper.update(timeLimitedDiscountShop);
    }

    @Override
    public void deleteById(Long id) {
        timeLimitedDiscountShopMapper.deleteById(id);
    }


    @Override
    public void bathcInsert(Integer activityId, List<Long> shopIds) {
        List<TimeLimitedDiscountShop> shops = new ArrayList<>();
        for (Long shopId : shopIds) {
            TimeLimitedDiscountShop shop = new TimeLimitedDiscountShop();
            shop.setActivityId(activityId);
            shop.setShopId(shopId);
            shops.add(shop);
        }
        timeLimitedDiscountShopMapper.insertBatch(shops);
    }

    @Override
    public void removeByActivityId(Integer activityId) {
        timeLimitedDiscountShopMapper.removeByActivityId(activityId);
    }
}
