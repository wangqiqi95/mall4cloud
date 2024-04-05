package com.mall4j.cloud.coupon.feign;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.coupon.dto.CouponSyncDto;
import com.mall4j.cloud.api.coupon.dto.PayCouponDTO;
import com.mall4j.cloud.api.coupon.dto.QueryCrmIdsDTO;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.*;
import com.mall4j.cloud.api.crm.dto.QueryHasCouponUsersRequest;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.order.vo.CouponOrderVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.BatchReceiveCouponDTO;
import com.mall4j.cloud.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.coupon.dto.ReceiveCouponDTO;
import com.mall4j.cloud.coupon.model.TCoupon;
import com.mall4j.cloud.coupon.service.BuyCouponActivityService;
import com.mall4j.cloud.coupon.service.TCouponFeignService;
import com.mall4j.cloud.coupon.service.TCouponService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author shijing
 * @date 2022/1/18
 */
@RestController
@Slf4j
public class TCouponFeignController implements TCouponFeignClient {
    @Resource
    private TCouponService tCouponService;

    @Resource
    private TCouponFeignService tCouponFeignService;
    @Resource
    private TCouponUserService tCouponUserService;
    @Resource
    private BuyCouponActivityService buyCouponActivityService;

    @Override
    public ServerResponseEntity<CouponDetailVO> getCouponDetail(Long couponIds) {
        ServerResponseEntity<CouponDetailDTO> detail = tCouponService.detail(couponIds);
        CouponDetailVO couponDetailVO = BeanUtil.copyProperties(detail.getData(), CouponDetailVO.class);
        return ServerResponseEntity.success(couponDetailVO);
    }

    @Override
    public ServerResponseEntity<CouponDetailVO> getCouponDetailByCouponCode(String couponCode) {
        com.mall4j.cloud.coupon.model.TCouponUser tCouponUser = tCouponUserService.selectByCouponCode(couponCode);
        if(tCouponUser==null){
            Assert.faild("优惠券不存在，请检查数据后重试。");
        }
        return getCouponDetail(tCouponUser.getCouponId());
    }

    @Override
    public ServerResponseEntity<List<CouponSyncDto>> save(List<com.mall4j.cloud.api.coupon.dto.CouponDetailDTO> param) {
        List<CouponSyncDto> save = tCouponFeignService.save(param);
        return ServerResponseEntity.success(save);
    }

    @Override
    public ServerResponseEntity<List<CouponSyncDto>> update(List<com.mall4j.cloud.api.coupon.dto.CouponDetailDTO> param) {
        List<CouponSyncDto> update = tCouponFeignService.update(param);
        return ServerResponseEntity.success(update);
    }

    @Override
    public ServerResponseEntity<List<CouponListVO>> selectCouponByIds(List<Long> couponIds) {
        ServerResponseEntity<List<TCoupon>> list = tCouponService.selectCouponByIds(couponIds);
        List<CouponListVO> result = Convert.toList(CouponListVO.class, list.getData());
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> receive(com.mall4j.cloud.api.coupon.dto.ReceiveCouponDTO param) {
        ReceiveCouponDTO receiveCouponDTO = BeanUtil.copyProperties(param, ReceiveCouponDTO.class);
        return tCouponUserService.receive(receiveCouponDTO);
    }

    @Override
    public ServerResponseEntity<Void> batchReceive(com.mall4j.cloud.api.coupon.dto.BatchReceiveCouponDTO param) {
        BatchReceiveCouponDTO batchReceiveCouponDTO = BeanUtil.copyProperties(param, BatchReceiveCouponDTO.class);
        return tCouponUserService.batchReceive(batchReceiveCouponDTO);
    }

    @Override
    public ServerResponseEntity<List<TCouponUser>> userCouponList(TCouponUser param) {
        com.mall4j.cloud.coupon.model.TCouponUser tCouponUser = BeanUtil.copyProperties(param, com.mall4j.cloud.coupon.model.TCouponUser.class);
        log.info("查询优惠券参数:{}", JSONObject.toJSONString(tCouponUser));
        ServerResponseEntity<List<com.mall4j.cloud.coupon.model.TCouponUser>> list = tCouponUserService.couponUserList(tCouponUser);
        List<TCouponUser> tCouponUsers = Convert.toList(TCouponUser.class, list.getData());
        return ServerResponseEntity.success(tCouponUsers);
    }

    @Override
    public ServerResponseEntity<Integer> countCanUseCoupon(Long userId) {
        return tCouponUserService.countCanUseCoupon(userId);
    }

    @Override
    public ServerResponseEntity<Void> payCoupon(PayCouponDTO param) {
        com.mall4j.cloud.coupon.dto.PayCouponDTO payCouponDTO = BeanUtil.copyProperties(param, com.mall4j.cloud.coupon.dto.PayCouponDTO.class);
        return buyCouponActivityService.payCoupon(payCouponDTO);
    }

    @Override
    public ServerResponseEntity<List<TCouponUser>> selectOrderNo(Date startTime, Date endTime) {
        ServerResponseEntity<List<com.mall4j.cloud.coupon.model.TCouponUser>> list = tCouponUserService.selectOrderNo(startTime, endTime);
        List<TCouponUser> tCouponUsers = Convert.toList(TCouponUser.class, list.getData());
        return ServerResponseEntity.success(tCouponUsers);
    }

    @Override
    public ServerResponseEntity<Boolean> isUseEnterpriseCoupon(Long orderNo) {
        Boolean result = tCouponUserService.isUseEnterpriseCoupon(orderNo);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<CouponUserCountDataVO> countCouponUserByUserId(Long userId) {
        CouponUserCountDataVO couponUserCountDataVO = tCouponUserService.countCouponUserByUserId(userId);
        return ServerResponseEntity.success(couponUserCountDataVO);
    }

    @Override
    public ServerResponseEntity<List<TCouponUserOrderVo>> selectByOrderIds(List<Long> orderIds) {
        return ServerResponseEntity.success(tCouponUserService.selectByOrderIds(orderIds));
    }

    @Override
    public ServerResponseEntity<Void> syncCRMCoupon(CouponOrderVO couponOrderVO) {
        tCouponUserService.syncCRMCoupon(couponOrderVO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<String>> queryCrmIds(QueryCrmIdsDTO queryCrmIdsDTO) {
        return ServerResponseEntity.success(tCouponService.queryCrmIds(queryCrmIdsDTO));
    }

    @Override
    public ServerResponseEntity<List<Long>> getCouponUserIds(QueryHasCouponUsersRequest queryHasCouponUsersRequest) {
        return ServerResponseEntity.success(tCouponUserService.getCouponUserIds(queryHasCouponUsersRequest));
    }
    
    @Override
    public ServerResponseEntity<List<PaperCouponOrderVO>> listPaperCouponOrder(Date startTime, Date endTime) {
        return ServerResponseEntity.success(tCouponUserService.listPaperCouponOrder(startTime ,endTime));
    }
}
