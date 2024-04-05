package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.coupon.dto.TCouponActivityCentreAddDTO;
import com.mall4j.cloud.api.coupon.dto.TCouponActivityCentreParamDTO;
import com.mall4j.cloud.api.coupon.vo.CouponListVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.model.TCouponActivityCentre;
import com.mall4j.cloud.coupon.mapper.TCouponActivityCentreMapper;
import com.mall4j.cloud.coupon.service.TCouponActivityCentreService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 优惠券关联活动
 */
@Service
public class TCouponActivityCentreServiceImpl extends ServiceImpl<TCouponActivityCentreMapper, TCouponActivityCentre> implements TCouponActivityCentreService {

    @Autowired
    private TCouponActivityCentreMapper tCouponActivityCentreMapper;

    @Override
    public PageVO<CouponListVO> page(PageDTO pageDTO, TCouponActivityCentreParamDTO paramDTO) {
        return PageUtil.doPage(pageDTO, () -> tCouponActivityCentreMapper.couponList(paramDTO));
    }

    @Override
    public List<CouponListVO> couponACList(TCouponActivityCentreParamDTO paramDTO) {
        return tCouponActivityCentreMapper.couponList(paramDTO);
    }

    @Override
    public void saveTo(TCouponActivityCentreAddDTO reqDTO) {
        if(Objects.nonNull(reqDTO.getActivityId()) && CollectionUtil.isNotEmpty(reqDTO.getCouponIds())){
            List<TCouponActivityCentre> discountCoupons=new ArrayList<>();
            reqDTO.getCouponIds().stream().forEach(item->{
                TCouponActivityCentre timeLimitedDiscountCoupon=new TCouponActivityCentre();
                timeLimitedDiscountCoupon.setActivityId(reqDTO.getActivityId());
                timeLimitedDiscountCoupon.setCouponId(item);
                timeLimitedDiscountCoupon.setActivitySource(reqDTO.getActivitySource());
                timeLimitedDiscountCoupon.setCreateBy(AuthUserContext.get().getUsername());
                timeLimitedDiscountCoupon.setCreateTime(new Date());
                timeLimitedDiscountCoupon.setDelFlag(0);
                discountCoupons.add(timeLimitedDiscountCoupon);
            });
            this.saveBatch(discountCoupons);
        }
    }

    @Override
    public void updateTo(TCouponActivityCentreAddDTO reqDTO) {
        if(Objects.nonNull(reqDTO.getActivityId()) && CollectionUtil.isNotEmpty(reqDTO.getCouponIds())){
            deleteById(reqDTO.getActivityId());

            List<TCouponActivityCentre> discountCoupons=new ArrayList<>();
            reqDTO.getCouponIds().stream().forEach(item->{
                TCouponActivityCentre timeLimitedDiscountCoupon=new TCouponActivityCentre();
                timeLimitedDiscountCoupon.setActivityId(reqDTO.getActivityId());
                timeLimitedDiscountCoupon.setCouponId(item);
                timeLimitedDiscountCoupon.setActivitySource(reqDTO.getActivitySource());
                timeLimitedDiscountCoupon.setCreateBy(AuthUserContext.get().getUsername());
                timeLimitedDiscountCoupon.setCreateTime(new Date());
                timeLimitedDiscountCoupon.setDelFlag(0);
                discountCoupons.add(timeLimitedDiscountCoupon);
            });
            this.saveBatch(discountCoupons);
        }
    }

    @Override
    public void deleteById(Long activityId) {
        this.update(new LambdaUpdateWrapper<TCouponActivityCentre>()
                .set(TCouponActivityCentre::getDelFlag,1)
                .set(TCouponActivityCentre::getUpdateBy, AuthUserContext.get().getUsername())
                .set(TCouponActivityCentre::getUpdateTime,new Date())
                .eq(TCouponActivityCentre::getActivityId,activityId));
    }

}
