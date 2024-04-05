package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinOpenAccount;
import com.mall4j.cloud.biz.mapper.WeixinOpenAccountMapper;
import com.mall4j.cloud.biz.service.WeixinOpenAccountService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信第三方平台账号表
 *
 * @author FrozenWatermelon
 * @date 2021-12-29 11:05:26
 */
@Service
public class WeixinOpenAccountServiceImpl implements WeixinOpenAccountService {

    @Autowired
    private WeixinOpenAccountMapper weixinOpenAccountMapper;

    @Override
    public PageVO<WeixinOpenAccount> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinOpenAccountMapper.list());
    }

    @Override
    public WeixinOpenAccount getById(Long id) {
        return weixinOpenAccountMapper.getById(id);
    }

    @Override
    public void save(WeixinOpenAccount weixinOpenAccount) {
        weixinOpenAccountMapper.save(weixinOpenAccount);
    }

    @Override
    public void update(WeixinOpenAccount weixinOpenAccount) {
        weixinOpenAccountMapper.update(weixinOpenAccount);
    }

    @Override
    public void deleteById(Long id) {
        weixinOpenAccountMapper.deleteById(id);
    }

    @Override
    public WeixinOpenAccount queryOneByAppid(String appid) {
        return weixinOpenAccountMapper.queryOneByAppid(appid);
    }
}
