package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.mapper.cp.ConfigMapper;
import com.mall4j.cloud.biz.model.cp.Config;
import com.mall4j.cloud.biz.service.cp.ConfigService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * 企业微信配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@RequiredArgsConstructor
@Service
public class ConfigServiceImpl implements ConfigService {

    private final  ConfigMapper configMapper;

    @Override
    public Config getByCpId(String cpId) {
        return configMapper.getByCpId(cpId);
    }

    @Override
    public void save(Config config) {
//        configMapper.deleteAll();
        configMapper.save(config);
    }
    @Override
    public void update(Config config) {
        configMapper.update(config);
    }

    @Override
    public void deleteById(Long cpId) {
        configMapper.deleteById(cpId);
    }

    @Override
    public Config getConfig() {
        List<Config> configList = configMapper.list();
        if(!CollectionUtils.isEmpty(configList)){
            return configList.get(0);
        }
        return null;
    }
}
