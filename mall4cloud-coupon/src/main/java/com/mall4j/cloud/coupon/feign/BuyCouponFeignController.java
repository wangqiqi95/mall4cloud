package com.mall4j.cloud.coupon.feign;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.coupon.dto.PayCouponDTO;
import com.mall4j.cloud.api.coupon.feign.BuyCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.BuyCouponLog;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.mapper.BuyCouponLogMapper;
import com.mall4j.cloud.coupon.service.BuyCouponActivityService;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @author shijing
 * @date 2022/1/18
 */
@RestController
public class BuyCouponFeignController implements BuyCouponFeignClient {
    @Resource
    private BuyCouponLogMapper buyCouponLogMapper;

    @Resource
    private BuyCouponActivityService buyCouponActivityService;


    @Override
    public ServerResponseEntity<BuyCouponLog> getBuyCouponDetail(Long orderNo) {
        com.mall4j.cloud.coupon.model.BuyCouponLog buyCouponLog = buyCouponLogMapper.selectOne(new LambdaQueryWrapper<com.mall4j.cloud.coupon.model.BuyCouponLog>()
                .eq(com.mall4j.cloud.coupon.model.BuyCouponLog::getOrderNo, orderNo));
        BuyCouponLog result = BeanUtil.copyProperties(buyCouponLog, BuyCouponLog.class);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> payCoupon(PayCouponDTO param) {
        com.mall4j.cloud.coupon.dto.PayCouponDTO payCouponDTO = BeanUtil.copyProperties(param, com.mall4j.cloud.coupon.dto.PayCouponDTO.class);
        return buyCouponActivityService.payCoupon(payCouponDTO);
    }
}
