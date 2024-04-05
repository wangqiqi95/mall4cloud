package com.mall4j.cloud.user.service.impl;

import com.mall4j.cloud.common.cache.constant.UserCacheNames;
import com.mall4j.cloud.user.model.UserConsignee;
import com.mall4j.cloud.user.mapper.UserConsigneeMapper;
import com.mall4j.cloud.user.service.UserConsigneeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户提货人信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-15 17:18:56
 */
@Service
public class UserConsigneeServiceImpl implements UserConsigneeService {

    @Autowired
    private UserConsigneeMapper userConsigneeMapper;

    @Override
    @Cacheable(cacheNames = UserCacheNames.USER_DEFAULT_CONSIGNEE, key = "#userId")
    public UserConsignee getByUserId(Long userId) {
        return userConsigneeMapper.getByUserId(userId);
    }

    @Override
    @CacheEvict(cacheNames = UserCacheNames.USER_DEFAULT_CONSIGNEE, key = "#userConsignee.userId")
    public void save(UserConsignee userConsignee) {
        userConsigneeMapper.save(userConsignee);
    }

    @Override
    @CacheEvict(cacheNames = UserCacheNames.USER_DEFAULT_CONSIGNEE, key = "#userConsignee.userId")
    public void update(UserConsignee userConsignee) {
        userConsigneeMapper.update(userConsignee);
    }
}
