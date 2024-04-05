package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.coupon.constant.SuitableProdType;
import com.mall4j.cloud.coupon.dto.CouponDTO;
import com.mall4j.cloud.coupon.model.CouponSpu;
import com.mall4j.cloud.coupon.mapper.CouponSpuMapper;
import com.mall4j.cloud.coupon.service.CouponSpuService;
import com.mall4j.cloud.coupon.vo.CouponVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 优惠券商品关联信息
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
@Service
public class CouponSpuServiceImpl implements CouponSpuService {

    @Autowired
    private CouponSpuMapper couponSpuMapper;

    @Override
    public void save(Long couponId, List<Long> spuIds) {
        List<CouponSpu> couponSpus = new ArrayList<>();
        for (Long spuId : spuIds) {
            CouponSpu couponSpu = new CouponSpu();
            couponSpu.setCouponId(couponId);
            couponSpu.setSpuId(spuId);
            couponSpus.add(couponSpu);
        }
        couponSpuMapper.saveBatch(couponSpus);
    }

    @Override
    public void update(CouponDTO couponDTO, CouponVO couponDb) {
        List<Long> spuIds = new ArrayList<>(couponDTO.getSpuIds());
        // 适用商品类型发生改变
        if (!Objects.equals(couponDTO.getSuitableProdType(), couponDb.getSuitableProdType())) {
            // 指定商品更新为所有商品--删除优惠券商品关联信息
            if (Objects.equals(couponDTO.getSuitableProdType(), SuitableProdType.ALL_SPU.value())) {
                couponSpuMapper.deleteByCouponIdAndSpuIds(couponDTO.getCouponId(), null);
            }
            // 所有商品更新为指定商品--插入优惠券商品关联信息
            else {
                save(couponDTO.getCouponId(), spuIds);
            }
            return;
        }
        // 适用商品类型没有改变, 且类型为所有商品
        if (Objects.equals(couponDTO.getSuitableProdType(), SuitableProdType.ALL_SPU.value())) {
            return;
        }
        // 适用商品类型没有改变, 且类型为指定商品

        spuIds.removeAll(couponDb.getSpuIds());
        if (CollUtil.isNotEmpty(spuIds)) {
            save(couponDTO.getCouponId(), spuIds);
        }
        List<Long> deleteList = new ArrayList<>(couponDb.getSpuIds());
        deleteList.removeAll(couponDTO.getSpuIds());
        if (CollUtil.isNotEmpty(deleteList)) {
            couponSpuMapper.deleteByCouponIdAndSpuIds(couponDb.getCouponId(), deleteList);
        }
    }

    @Override
    public void deleteBySpuIds(List<Long> spuIds) {
        couponSpuMapper.deleteBySpuIds(spuIds);
    }
}
