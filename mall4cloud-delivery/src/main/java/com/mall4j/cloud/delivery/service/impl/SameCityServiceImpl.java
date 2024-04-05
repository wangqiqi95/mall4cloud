package com.mall4j.cloud.delivery.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.delivery.bo.BmapPointBO;
import com.mall4j.cloud.api.delivery.constant.DistanceUtil;
import com.mall4j.cloud.api.delivery.constant.GraphUtils;
import com.mall4j.cloud.api.delivery.constant.SameCityChargeType;
import com.mall4j.cloud.common.cache.constant.DeliveryCacheNames;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.order.vo.UserAddrVO;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.delivery.model.SameCity;
import com.mall4j.cloud.delivery.mapper.SameCityMapper;
import com.mall4j.cloud.delivery.service.SameCityService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 同城配送信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@Service
public class SameCityServiceImpl implements SameCityService {

    @Autowired
    private SameCityMapper sameCityMapper;

    @Override
    public void save(SameCity sameCity) {
        sameCityMapper.save(sameCity);
    }

    @Override
    public void updateByShopId(SameCity sameCity) {
        sameCityMapper.updateByShopId(sameCity);
    }

    @Override
    public long calculateTransFee(List<ShopCartItemVO> shopCartItems, UserAddrVO userAddr) {
        long transFee = 0L;
        if(CollectionUtil.isEmpty(shopCartItems) || Objects.isNull(userAddr)){
            return transFee;
        }
        Long shopId = shopCartItems.get(0).getShopId();
        SameCity sameCity = getSameCityByShopId(shopId);
        // 如果商家没有配好同城配送或者没有启用的模板，直接返回 -2
        if(Objects.isNull(sameCity) || Objects.equals(sameCity.getStatus(),0)){
            return -2;
        }
        //计算当前的坐标点
        BmapPointBO bmapPoint = new BmapPointBO(userAddr.getLng(),userAddr.getLat());

        List<BmapPointBO> bmapPoints = Json.parseArray(sameCity.getPositionInfo(), BmapPointBO[].class);

        boolean point = GraphUtils.isPointInPolygon(bmapPoint, bmapPoints);
        //如果不在配送范围，直接返回 -1
        if(!point){
            return -1;
        }
        // 计算总重、总价
        long productTotalAmount = 0;
        BigDecimal productTotalWeight = new BigDecimal(0);
        for (ShopCartItemVO shopCartItem : shopCartItems) {
            productTotalAmount += shopCartItem.getTotalAmount();
            productTotalWeight = shopCartItem.getWeight().add(productTotalWeight);
        }
        //1.先判断起送费有没有够,没够直接返回 -3
        if(sameCity.getStartFee() > productTotalAmount){
            return -3;
        }

        //2.判断收费类型 2.1 固定费用先加上
        transFee = sameCity.getDeliveryFee();
        if(Objects.equals(sameCity.getChargeType(), SameCityChargeType.DISTANCE.value())){
            // 2.2按距离收费,sql查询出来的单位是m
            double distance = DistanceUtil.getDistance(userAddr.getLat(), userAddr.getLng(),sameCity.getLat(),sameCity.getLng());
            distance = Arith.div(distance,1000,1);

            if(distance > sameCity.getDefaultDistance()){
                // 减去免费配送距离
                double prodContinuousPiece = Arith.sub(distance,sameCity.getDefaultDistance());
                // 超出距离的倍数，向上取整
                Integer mulNumber = (int) Math.ceil(Arith.div(prodContinuousPiece, sameCity.getOverDistance()));
                // 超出距离的运费
                long continuousFee = mulNumber * sameCity.getOverDistanceFee();
                transFee += continuousFee;
            }
        }
        //3.按重量计算运费
        double weight = productTotalWeight.doubleValue();
        if (weight > sameCity.getFreeWeight() && Objects.nonNull(sameCity.getOverWeight()) && sameCity.getOverWeight() > 0.0) {
            // 续重重量
            double prodContinuousWeight = Arith.sub(weight,sameCity.getFreeWeight());
            // 续重重量的倍数，向上取整
            int mulNumber = (int) Math.ceil(Arith.div(prodContinuousWeight, sameCity.getOverWeight()));
            // 续重数量运费
            long continuousFee = mulNumber * sameCity.getOverDistanceFee();
            transFee += continuousFee;
        }
        return transFee;
    }

    @Override
    @Cacheable(cacheNames = DeliveryCacheNames.SAME_CITY_BY_ID_PREFIX, key = "#shopId")
    public SameCity getSameCityByShopId(Long shopId) {
        return sameCityMapper.getByShopId(shopId);
    }

    @Override
    @CacheEvict(cacheNames = DeliveryCacheNames.SAME_CITY_BY_ID_PREFIX, key = "#shopId")
    public void removeSameCityCacheByShopId(Long shopId) {

    }




}
