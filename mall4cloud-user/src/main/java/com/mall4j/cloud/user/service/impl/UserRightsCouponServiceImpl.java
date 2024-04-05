package com.mall4j.cloud.user.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.mapper.UserRightsCouponMapper;
import com.mall4j.cloud.user.model.UserRightsCoupon;
import com.mall4j.cloud.user.service.UserRightsCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
@Service
public class UserRightsCouponServiceImpl implements UserRightsCouponService {

    @Autowired
    private UserRightsCouponMapper userRightsCouponMapper;

    @Override
    public PageVO<UserRightsCoupon> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userRightsCouponMapper.list());
    }

    @Override
    public UserRightsCoupon getByRightsCouponId(Long rightsCouponId) {
        return userRightsCouponMapper.getByRightsCouponId(rightsCouponId);
    }

    @Override
    public void save(UserRightsCoupon userRightsCoupon) {
        userRightsCouponMapper.save(userRightsCoupon);
    }

    @Override
    public void update(UserRightsCoupon userRightsCoupon) {
        userRightsCouponMapper.update(userRightsCoupon);
    }

    @Override
    public void deleteById(Long rightsCouponId) {
        userRightsCouponMapper.deleteById(rightsCouponId);
    }
}
