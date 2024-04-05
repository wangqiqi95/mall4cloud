package com.mall4j.cloud.coupon.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.coupon.model.CouponGiveLog;
import com.mall4j.cloud.coupon.mapper.CouponGiveLogMapper;
import com.mall4j.cloud.coupon.service.CouponGiveLogService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 优惠券赠送记录
 *
 * @author FrozenWatermelon
 * @date 2021-04-28 16:25:05
 */
@Service
public class CouponGiveLogServiceImpl implements CouponGiveLogService {

    @Autowired
    private CouponGiveLogMapper couponGiveLogMapper;

    @Override
    public PageVO<CouponGiveLog> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> couponGiveLogMapper.list());
    }

    @Override
    public CouponGiveLog getByBizType(Long bizType) {
        return couponGiveLogMapper.getByBizType(bizType);
    }

    @Override
    public void save(CouponGiveLog couponGiveLog) {
        couponGiveLogMapper.save(couponGiveLog);
    }

    @Override
    public void update(CouponGiveLog couponGiveLog) {
        couponGiveLogMapper.update(couponGiveLog);
    }

    @Override
    public void deleteById(Long bizType) {
        couponGiveLogMapper.deleteById(bizType);
    }
}
