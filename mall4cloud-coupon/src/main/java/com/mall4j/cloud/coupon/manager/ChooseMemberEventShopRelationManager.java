package com.mall4j.cloud.coupon.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.coupon.dto.AddEventShopRelationDTO;
import com.mall4j.cloud.coupon.mapper.ChooseMemberEventShopRelationMapper;
import com.mall4j.cloud.coupon.model.ChooseMemberEventShopRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChooseMemberEventShopRelationManager {

    @Autowired
    private ChooseMemberEventShopRelationMapper chooseMemberEventShopRelationMapper;

    @Transactional(rollbackFor = Exception.class)
    public void addList(Long eventId, List<AddEventShopRelationDTO> shopList){

        List<ChooseMemberEventShopRelation> chooseMemberEventShopRelationList = shopList.stream().map(shop -> {
            ChooseMemberEventShopRelation eventShopRelation = new ChooseMemberEventShopRelation();
            eventShopRelation.setEventId(eventId);
            eventShopRelation.setShopId(shop.getShopId());
            eventShopRelation.setStoreCode(shop.getStoreCode());
            return eventShopRelation;
        }).collect(Collectors.toList());

        chooseMemberEventShopRelationMapper.insertBatch(chooseMemberEventShopRelationList);

    }

    public List<ChooseMemberEventShopRelation> getEventShopRelationList(Long event){
        List<ChooseMemberEventShopRelation> chooseMemberEventShopRelations = chooseMemberEventShopRelationMapper.selectList(
                new LambdaQueryWrapper<ChooseMemberEventShopRelation>()
                        .eq(ChooseMemberEventShopRelation::getEventId, event)
        );

        return chooseMemberEventShopRelations;
    }

    public List<ChooseMemberEventShopRelation> getEventShopRelationInIdList(List<Long> shopIdList){
        List<ChooseMemberEventShopRelation> chooseMemberEventShopRelations = chooseMemberEventShopRelationMapper.selectList(
                new LambdaQueryWrapper<ChooseMemberEventShopRelation>()
                        .in(ChooseMemberEventShopRelation::getShopId, shopIdList)
        );

        return chooseMemberEventShopRelations;
    }
}
