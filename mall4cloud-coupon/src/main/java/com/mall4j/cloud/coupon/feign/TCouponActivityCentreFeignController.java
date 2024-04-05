package com.mall4j.cloud.coupon.feign;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.coupon.dto.*;
import com.mall4j.cloud.api.coupon.feign.TCouponActivityCentreFeignClient;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.*;
import com.mall4j.cloud.api.crm.dto.QueryHasCouponUsersRequest;
import com.mall4j.cloud.common.order.vo.CouponOrderVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.BatchReceiveCouponDTO;
import com.mall4j.cloud.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.coupon.dto.ReceiveCouponDTO;
import com.mall4j.cloud.coupon.model.TCoupon;
import com.mall4j.cloud.coupon.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 */
@RestController
@Slf4j
public class TCouponActivityCentreFeignController implements TCouponActivityCentreFeignClient {
    @Resource
    private TCouponActivityCentreService activityCentreService;

    @Override
    public ServerResponseEntity<Void> saveTo(TCouponActivityCentreAddDTO addDTO) {
        activityCentreService.saveTo(addDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateTo(TCouponActivityCentreAddDTO addDTO) {
        activityCentreService.updateTo(addDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<CouponListVO>> couponACList(TCouponActivityCentreParamDTO paramDTO) {
        return ServerResponseEntity.success(activityCentreService.couponACList(paramDTO));
    }
}
