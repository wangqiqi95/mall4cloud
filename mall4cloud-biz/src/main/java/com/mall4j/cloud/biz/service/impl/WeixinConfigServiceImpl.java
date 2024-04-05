package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinConfig;
import com.mall4j.cloud.biz.mapper.WeixinConfigMapper;
import com.mall4j.cloud.biz.service.WeixinConfigService;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 微信配置信息表
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 15:58:07
 */
@Service
public class WeixinConfigServiceImpl implements WeixinConfigService {

    @Autowired
    private WeixinConfigMapper weixinConfigMapper;

    @Override
    public PageVO<WeixinConfig> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinConfigMapper.list());
    }

    @Override
    public WeixinConfig getById(Long id) {
        return weixinConfigMapper.getById(id);
    }

    @Override
    public void save(WeixinConfig weixinConfig) {
        weixinConfigMapper.save(weixinConfig);
    }

    @Override
    public void update(WeixinConfig weixinConfig) {
        weixinConfigMapper.update(weixinConfig);
    }

    @Override
    public void deleteById(Long id) {
        weixinConfigMapper.deleteById(id);
    }

    @Override
    public WeixinConfig getWeixinConfigByKey(String appid, String paramKey) {
        return weixinConfigMapper.getWeixinConfigByKey(appid,paramKey);
    }

    @Override
    public void saveOrUpdateWeixinConfig(String appId, String paramKey,String openState) {
        WeixinConfig weixinConfig=this.getWeixinConfigByKey(appId, paramKey);
        if(weixinConfig==null){
            weixinConfig=new WeixinConfig();
            weixinConfig.setId(RandomUtil.getUniqueNum());
            weixinConfig.setAppId(appId);
            weixinConfig.setParamKey(paramKey);
            weixinConfig.setParamValue(openState);
            weixinConfig.setCreateBy(String.valueOf(AuthUserContext.get().getUserId()));
            weixinConfig.setCreateTime(new Date());
            this.save(weixinConfig);
        }else{
            weixinConfig.setParamValue(openState);
            weixinConfig.setUpdateTime(new Date());
            weixinConfig.setUpdateBy(String.valueOf(AuthUserContext.get().getUserId()));
            this.update(weixinConfig);
        }
    }
}
