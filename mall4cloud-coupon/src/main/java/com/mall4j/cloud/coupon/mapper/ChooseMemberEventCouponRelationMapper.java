package com.mall4j.cloud.coupon.mapper;

import com.mall4j.cloud.coupon.model.ChooseMemberEventCouponRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.vo.MemberEventCouponVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 制定会员活动优惠券关联表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
public interface ChooseMemberEventCouponRelationMapper extends BaseMapper<ChooseMemberEventCouponRelation> {

    void insertBatch(@Param("relationList") List<ChooseMemberEventCouponRelation> relationList);

    Integer checkCouponToShop(@Param("couponId") Long couponId);

    List<MemberEventCouponVO> getTheCouponListByEventId(@Param("eventId") Long eventId);
}
