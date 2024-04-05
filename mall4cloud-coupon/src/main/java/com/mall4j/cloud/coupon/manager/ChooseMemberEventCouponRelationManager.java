package com.mall4j.cloud.coupon.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.coupon.mapper.ChooseMemberEventCouponRelationMapper;
import com.mall4j.cloud.coupon.model.ChooseMemberEventCouponRelation;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventCouponRelationVO;
import com.mall4j.cloud.coupon.vo.MemberEventCouponVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChooseMemberEventCouponRelationManager {

    @Autowired
    private ChooseMemberEventCouponRelationMapper chooseMemberEventCouponRelationMapper;

    /**
     * 批量保存高价值会员活动和优惠券关系
     * */
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(Long eventId, List<Long> couponIdList){
        List<ChooseMemberEventCouponRelation> chooseMemberEventCouponRelationList = couponIdList.stream().map(couponId -> {
            ChooseMemberEventCouponRelation eventCouponRelation = new ChooseMemberEventCouponRelation();
            eventCouponRelation.setCouponId(couponId);
            eventCouponRelation.setEventId(eventId);
            return eventCouponRelation;
        }).collect(Collectors.toList());

        chooseMemberEventCouponRelationMapper.insertBatch(chooseMemberEventCouponRelationList);
    }

    /**
     * 查询高价值会员活动和优惠券关系列表
     * */
    public List<ChooseMemberEventCouponRelationVO> getList(Long eventId,Long couponId){

        LambdaQueryWrapper<ChooseMemberEventCouponRelation> queryWrapper = new LambdaQueryWrapper<>();

        if (Objects.nonNull(eventId)){
            queryWrapper.eq(ChooseMemberEventCouponRelation::getEventId, eventId);
        }

        if (Objects.nonNull(couponId)){
            queryWrapper.eq(ChooseMemberEventCouponRelation::getCouponId, couponId);
        }

        List<ChooseMemberEventCouponRelation> chooseMemberEventCouponRelationList = chooseMemberEventCouponRelationMapper.selectList(
                queryWrapper
        );

        List<ChooseMemberEventCouponRelationVO> memberEventCouponRelationVOS = chooseMemberEventCouponRelationList.stream()
                .map(relation -> {
                    ChooseMemberEventCouponRelationVO couponRelationVO = new ChooseMemberEventCouponRelationVO();
                    BeanUtils.copyProperties(relation, couponRelationVO);
                    return couponRelationVO;
                }).collect(Collectors.toList());
        return memberEventCouponRelationVOS;
    }

    /**
     * 通过活动ID查询关系列表
     * @param eventId
     * */
    public List<ChooseMemberEventCouponRelationVO> getListBYEventId(Long eventId){
        return this.getList(eventId, null);
    }

    /**
     * 通过优惠券ID查询关系列表
     * @param couponId
     * */
    public List<ChooseMemberEventCouponRelationVO> getListBYCouponId(Long couponId){
        return this.getList(null, couponId);
    }

    /**
     * 根据eventId删除所有与活动绑定的优惠券
     * @param eventId
     * */
    @Transactional(rollbackFor = Exception.class)
    public void removeRelationByEventId(Long eventId){
        chooseMemberEventCouponRelationMapper.delete(
                new LambdaQueryWrapper<ChooseMemberEventCouponRelation>()
                        .eq(ChooseMemberEventCouponRelation::getEventId, eventId)
        );
    }

    /**
     * 根据eventId查询所有与活动绑定的优惠券
     * @param eventId
     * */
    public List<MemberEventCouponVO> getTheCouponListByEventId(Long eventId){
        List<MemberEventCouponVO> couponList = chooseMemberEventCouponRelationMapper.getTheCouponListByEventId(eventId);
        return couponList;
    }
}
