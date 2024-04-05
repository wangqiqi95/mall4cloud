package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.mapper.cp.ShopWelcomeConfigMapper;
import com.mall4j.cloud.biz.model.cp.ShopWelcomeConfig;
import com.mall4j.cloud.biz.service.cp.StoreWelcomeConfigService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;

import com.mall4j.cloud.common.database.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 欢迎语门店关联表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@RequiredArgsConstructor
@Service
public class StoreWelcomeConfigServiceImpl implements StoreWelcomeConfigService {

    private final  ShopWelcomeConfigMapper shopWelcomeConfigMapper;

    @Override
    public void save(ShopWelcomeConfig shopWelcomeConfig) {
        shopWelcomeConfigMapper.save(shopWelcomeConfig);
    }

    @Override
    public void deleteByWelId(Long welId) {
        shopWelcomeConfigMapper.deleteByWelId(welId);
    }

    @Override
    public List<ShopWelcomeConfig> listByWelId(Long welId) {
        return shopWelcomeConfigMapper.listByWelId( welId);
    }
}
