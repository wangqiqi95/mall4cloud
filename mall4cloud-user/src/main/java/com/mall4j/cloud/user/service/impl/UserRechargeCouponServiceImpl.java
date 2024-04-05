package com.mall4j.cloud.user.service.impl;
import java.util.*;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.RechargeCouponDTO;
import com.mall4j.cloud.user.model.UserRechargeCoupon;
import com.mall4j.cloud.user.mapper.UserRechargeCouponMapper;
import com.mall4j.cloud.user.service.UserRechargeCouponService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 余额优惠券关联表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
@Service
public class UserRechargeCouponServiceImpl implements UserRechargeCouponService {

    @Autowired
    private UserRechargeCouponMapper userRechargeCouponMapper;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageVO<UserRechargeCoupon> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userRechargeCouponMapper.list());
    }

    @Override
    public UserRechargeCoupon getByRechargeId(Long rechargeId) {
        return userRechargeCouponMapper.getByRechargeId(rechargeId);
    }

    @Override
    public void save(UserRechargeCoupon userRechargeCoupon) {
        userRechargeCouponMapper.save(userRechargeCoupon);
    }

    @Override
    public void update(UserRechargeCoupon userRechargeCoupon) {
        userRechargeCouponMapper.update(userRechargeCoupon);
    }

    @Override
    public void deleteById(Long rechargeId) {
        userRechargeCouponMapper.deleteById(rechargeId);
    }

    @Override
    public void insertBatch(Long rechargeId, List<RechargeCouponDTO> couponList) {
        List<UserRechargeCoupon> userRechargeCouponList = Lists.newArrayList();
        for (RechargeCouponDTO coupon : couponList){
            UserRechargeCoupon rechargeCoupon = new UserRechargeCoupon();
            rechargeCoupon.setRechargeId(rechargeId);
            rechargeCoupon.setCouponId(coupon.getCouponId());
            rechargeCoupon.setCouponNum(coupon.getCouponNum());
            userRechargeCouponList.add(rechargeCoupon);
        }
        userRechargeCouponMapper.saveBatch(userRechargeCouponList);
    }

    @Override
    public void removeByRechargeIdAndCouponId(Long rechargeId, Set<Long> couponIdList) {
        userRechargeCouponMapper.removeByRechargeIdAndCouponId(rechargeId,couponIdList);
    }

    @Override
    public void updateBatchByCoupons(Long rechargeId, List<RechargeCouponDTO> updateList) {
        List<UserRechargeCoupon> couponList = mapperFacade.mapAsList(updateList, UserRechargeCoupon.class);
        userRechargeCouponMapper.updateBatchByCoupons(rechargeId,couponList);
    }

    @Override
    public void cancelBindingCoupons(List<Long> couponIds) {
        List<Long> rechargeIds = userRechargeCouponMapper.listRechargeIdByCouponIds(couponIds);
        if (CollUtil.isEmpty(rechargeIds)) {
            return;
        }
        userRechargeCouponMapper.removeByCouponIds(couponIds);
        List<String> keys = new ArrayList<>();
        // 清除余额充值模板缓存
        for (Long rechargeId : rechargeIds) {
            keys.add(CacheNames.USER_RECHARGE_INFO + CacheNames.UNION + rechargeId);
        }
        keys.add(CacheNames.USER_RECHARGE_LIST);
        RedisUtil.deleteBatch(keys);
    }

}
