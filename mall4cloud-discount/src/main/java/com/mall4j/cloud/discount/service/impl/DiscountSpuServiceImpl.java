package com.mall4j.cloud.discount.service.impl;

import com.mall4j.cloud.discount.mapper.DiscountSpuMapper;
import com.mall4j.cloud.discount.service.DiscountSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 满减满折商品关联表
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
@Service
public class DiscountSpuServiceImpl implements DiscountSpuService {
    @Autowired
    private DiscountSpuMapper discountSpuMapper;


    @Override
    public List<Long> listSpuIdByDiscountId(Long discountId) {
        return discountSpuMapper.listSpuIdByDiscountId(discountId);
    }
}
