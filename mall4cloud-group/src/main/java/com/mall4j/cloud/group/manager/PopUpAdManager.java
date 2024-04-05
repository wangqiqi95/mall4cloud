package com.mall4j.cloud.group.manager;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.CouponListVO;
import com.mall4j.cloud.api.user.feign.TagClient;
import com.mall4j.cloud.api.user.feign.UserTagRelationFeignClient;
import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.group.bo.PopUpAdParamsValidBO;
import com.mall4j.cloud.group.constant.*;
import com.mall4j.cloud.group.dto.UpdatePopUpAdPushRuleDTO;
import com.mall4j.cloud.group.mapper.PopUpAdMapper;
import com.mall4j.cloud.group.model.PopUpAd;
import com.mall4j.cloud.group.strategy.ad.attachment.enums.AttachmentHandlerEnum;
import com.mall4j.cloud.group.strategy.ad.rule.enums.PushRuleHandlerEnum;
import com.mall4j.cloud.group.vo.PageAdByUserVO;
import com.mall4j.cloud.group.vo.PopUpAdVO;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PopUpAdManager {

    @Autowired
    private PopUpAdMapper popUpAdMapper;

    @Autowired
    private UserTagRelationFeignClient userTagRelationFeignClient;

    @Autowired
    private TCouponFeignClient tCouponFeignClient;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private TagClient tagClient;

    private final static String POP_UP_AD_CACHE_KEY = "popUpAd::";

    public PopUpAdVO getById(Long adId){
        PopUpAd popUpAd = popUpAdMapper.selectById(adId);
        PopUpAdVO popUpAdVO = new PopUpAdVO();
        BeanUtils.copyProperties(popUpAd, popUpAdVO);
        return popUpAdVO;
    }

    public List<PopUpAdVO> getListByIdList(List<Long> adIdList){
        List<PopUpAd> popUpAdList = popUpAdMapper.selectList(
                new LambdaQueryWrapper<PopUpAd>()
                        .in(PopUpAd::getPopUpAdId, adIdList)
        );
        List<PopUpAdVO> popUpAdVOS = mapperFacade.mapAsList(popUpAdList, PopUpAdVO.class);

        return popUpAdVOS;
    }

    public List<PageAdByUserVO> getThePageAdByUser(Long serviceStoreId, LocalDateTime time){
        return popUpAdMapper.getThePageAdByUser(serviceStoreId, time);
    }


    public List<Long> checkTagUser(List<Long> tagIdList){
        ServerResponseEntity<List<Long>> response = userTagRelationFeignClient.checkUserTagRelationByTagIdList(tagIdList);

        if (response.isSuccess() && CollectionUtil.isNotEmpty(response.getData())){
            return response.getData();
        }

        return null;
    }

    @Async
    public void setBrowseCache(List<PopUpAd> popUpAdList, String unionId){

        popUpAdList.stream().forEach(ad -> {
            //当广告设置了时段控制的，需要缓存相关用户广告浏览记录，没有设置则表明全时段不需要控制
            String cacheKey = this.createKey(ad.getPopUpAdId())+unionId;

            Integer time;

            //校验是否为每天一次的广告频率
            if (ad.getAdFrequency().equals(PopUpAdAdFrequencyEnum.EVERY_DAY.getAdFrequency())){
                time = DateUtils.getRemainSecondsOneDay(new Date());
                RedisUtil.set( cacheKey, ad, time.longValue());
            }
            //校验是否为只打开一次的广告频率
            if (ad.getAdFrequency().equals(PopUpAdAdFrequencyEnum.ONCE.getAdFrequency())){
                time = DateUtils.getTheBetweenSeconds(LocalDateTime.now(), ad.getActivityEndTime());
                RedisUtil.set( cacheKey, ad, time.longValue());
            }


        });
    }

    public String createKey(Long adId){
        return POP_UP_AD_CACHE_KEY+adId+"::";
    }

    public void validParams(PopUpAdParamsValidBO popUpAdParamsValidBO){

        if (CollectionUtil.isNotEmpty(popUpAdParamsValidBO.getPopUpAdPushRuleList())){
            List<UpdatePopUpAdPushRuleDTO> pushRuleDTOS = popUpAdParamsValidBO.getPopUpAdPushRuleList().stream()
                    .filter(updatePopUpAdPushRuleDTO -> updatePopUpAdPushRuleDTO.getEndTime().isBefore(updatePopUpAdPushRuleDTO.getStartTime()))
                    .collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(pushRuleDTOS)){
                throw new LuckException("投放时段开始时间无法大于结束时间");
            }
        }

        if (popUpAdParamsValidBO.getPushType().equals(PopUpAdPushTypeEnum.CHOOSE_TIME.getPushType())
                && StringUtils.isEmpty(popUpAdParamsValidBO.getRuleTimeTag())
                && !popUpAdParamsValidBO.getPushRule().equals(PushRuleHandlerEnum.EVERY_DAY.getRuleType())){
            throw new LuckException("请设置相应触发日期段");
        }
        if (popUpAdParamsValidBO.getPushType().equals(PopUpAdPushTypeEnum.CHOOSE_TIME.getPushType())
                && StringUtils.isEmpty(popUpAdParamsValidBO.getPushRule())){
            throw new LuckException("请设置相应推送规则");
        }
        if (PopUpAdPushRuleTypeEnum.TIME.getRule().contains(popUpAdParamsValidBO.getPushRule())
                && CollectionUtil.isEmpty(popUpAdParamsValidBO.getPopUpAdPushRuleList())){
            throw new LuckException("请设置相应推送规则");
        }

        //校验是否为部分门店规则，是的话校验是否设置了相关指定门店
        if (!popUpAdParamsValidBO.getAttachmentType().equals(AttachmentHandlerEnum.QUESTIONNAIRE.getContentType())){
            if (popUpAdParamsValidBO.getIsAllShop().equals(PopUpAdShopEnum.NOT_ALL_SHOP.getIsAllShop())
                    && CollectionUtil.isEmpty(popUpAdParamsValidBO.getStoreIdList())){
                throw new LuckException("指定门店需要选择至少一家门店");
            }
            if (popUpAdParamsValidBO.getVisibleType().equals(PopUpAdVisibleTypeEnum.CHOOSE.getVisibleType())
                    && Objects.isNull(popUpAdParamsValidBO.getUserTagId())){
                throw new LuckException("指定人群需选择相关标签");
            }
        }


        //校验是否为部分门店规则，是的话校验是否设置了相关指定门店
        if (CollectionUtil.isEmpty(popUpAdParamsValidBO.getPopUpAdAttachmentList())){
            throw new LuckException("广告需要设置至少一个附件素材");
        }
    }


    public List<CouponListVO> getCouponList(List<Long> couponIdList){
        //通过微服务获取优惠券信息
        ServerResponseEntity<List<CouponListVO>> response = tCouponFeignClient.selectCouponByIds(couponIdList);

        List<CouponListVO> data;
        //校验微服务是否调用成功
        if (response.isSuccess() && CollectionUtil.isNotEmpty(data = response.getData())){
            return data;
        }

        return null;
    }

    public TagVO getTagById(Long tagId){
        ServerResponseEntity<TagVO> response = tagClient.getTag(tagId);

        TagVO tagVO;

        if (response.isSuccess() && Objects.nonNull(tagVO = response.getData())){
            return tagVO;
        }

        return null;
    }

}
