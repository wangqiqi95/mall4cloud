package com.mall4j.cloud.group.strategy.ad.attachment;


import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.CouponListVO;
import com.mall4j.cloud.common.cache.constant.GroupCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.group.bo.AttachmentBO;
import com.mall4j.cloud.group.strategy.ad.attachment.enums.CouponFaceValueEnum;
import com.mall4j.cloud.group.vo.PopUpAdAttachmentVO;
import com.mall4j.cloud.group.vo.PopUpAdContainerVO;
import com.mall4j.cloud.group.vo.PopUpAdFormCouponVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
public class CouponAttachmentHandler implements AttachmentHandler {

    @Autowired
    private TCouponFeignClient tCouponFeignClient;

//    private static final String COUPON_ATTACHMENT = "coupon_attachment::";

//    private static final ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    public void extraction(PopUpAdContainerVO container, AttachmentBO attachmentBO) {

        //聚合所有的优惠券ID
        List<Long> couponIdList = attachmentBO.getPopUpAdAttachmentVOList().stream()
                .map(PopUpAdAttachmentVO::getBusinessId)
                .collect(Collectors.toList());

        //尝试获取锁
//        while (!reentrantLock.tryLock()){
//            //获取不到锁则查询缓存中是否能获取到本次请求想要的数据，如果没有就绪获取锁
//            if (RedisUtil.hasKey(GroupCacheNames.ATTACHMENT +attachmentBO.getPopUpAdId())){
//
//                PopUpAdFormCouponVO couponAdVO = RedisUtil.get(GroupCacheNames.ATTACHMENT+attachmentBO.getPopUpAdId());
//                container.getPopUpAdFormCouponList().add(couponAdVO);
//                return;
//            }
//        }

//        else {

        //尝试获取锁成功后，进行上锁
//        reentrantLock.lock();
        //上锁后第一时间校验缓存中是否存在想要的数据，是的话直接从缓存中获取并解锁
        if (RedisUtil.hasKey(GroupCacheNames.ATTACHMENT+attachmentBO.getPopUpAdId())){

            PopUpAdFormCouponVO couponAdVO = RedisUtil.get(GroupCacheNames.ATTACHMENT+attachmentBO.getPopUpAdId());
            container.getPopUpAdFormCouponList().add(couponAdVO);
//            reentrantLock.unlock();
            return;
        }
        if (CollectionUtil.isNotEmpty(couponIdList)){
            //通过微服务获取优惠券信息
            ServerResponseEntity<List<CouponListVO>> response = tCouponFeignClient.selectCouponByIds(couponIdList);

            List<CouponListVO> data;
            //校验微服务是否调用成功
            if (response.isSuccess() && CollectionUtil.isNotEmpty(data = response.getData())){
                //声明一个优惠券广告对象
                PopUpAdFormCouponVO couponAdVO = new PopUpAdFormCouponVO();
                //获取优惠券信息
                List<PopUpAdFormCouponVO.CouponDetail> couponList = data.stream()
                        .map(couponDataVO -> {
                            //声明优惠券信息对象
                            PopUpAdFormCouponVO.CouponDetail couponVO = new PopUpAdFormCouponVO.CouponDetail();

                            //设置优惠券ID，优惠券名称，优惠券封面图
                            couponVO.setCouponId(couponDataVO.getId());
                            couponVO.setCouponName(couponDataVO.getName());
                            couponVO.setCouponPicUrl(couponDataVO.getCoverUrl());
                            if (couponDataVO.getType().equals(CouponFaceValueEnum.FULL_DISCOUNT.getType())){
                                BigDecimal divide = couponDataVO.getReduceAmount().divide(new BigDecimal(100));
                                couponVO.setSymbol(divide.toString());
                                couponVO.setFlag(CouponFaceValueEnum.FULL_DISCOUNT.getFlag());
                            }

                            if (couponDataVO.getType().equals(CouponFaceValueEnum.REDUCED.getType())){
                                BigDecimal multiply = couponDataVO.getCouponDiscount().setScale(1);
                                couponVO.setSymbol(multiply.toString());
                                couponVO.setFlag(CouponFaceValueEnum.REDUCED.getFlag());
                            }

                            return couponVO;
                        })
                        .collect(Collectors.toList());

                couponAdVO.setAttachmentType(attachmentBO.getAttachmentType());
                couponAdVO.setPopUpAdID(attachmentBO.getPopUpAdId());
                couponAdVO.setCouponDetailList(couponList);
                couponAdVO.setAutoOffSeconds(attachmentBO.getAutoOffSeconds());

                //保存数据到缓存
                Integer expires = DateUtils.getTheBetweenSeconds(LocalDateTime.now(), attachmentBO.getActivityEndTime());
                RedisUtil.set(GroupCacheNames.ATTACHMENT+couponAdVO.getPopUpAdID(),couponAdVO,expires);

                //将优惠券广告放入广告容器
                container.getPopUpAdFormCouponList().add(couponAdVO);
            }
        }

    }


}
