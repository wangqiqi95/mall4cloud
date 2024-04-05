package com.mall4j.cloud.api.order.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.coupon.constant.MyCouponStatus;
import com.mall4j.cloud.api.coupon.dto.LockCouponDTO;
import com.mall4j.cloud.api.coupon.dto.UpdateCouponStatusDTO;
import com.mall4j.cloud.api.coupon.feign.CouponOrderFeignClient;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.product.dto.SkuStockLockDTO;
import com.mall4j.cloud.api.product.feign.SkuStockLockFeignClient;
import com.mall4j.cloud.api.user.dto.UserScoreLockDTO;
import com.mall4j.cloud.api.user.feign.UserScoreLockFeignClient;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.constant.OrderCacheNames;
import com.mall4j.cloud.common.cache.util.CacheManagerUtil;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.dto.OrderInvoiceDTO;
import com.mall4j.cloud.common.order.dto.OrderShopDTO;
import com.mall4j.cloud.common.order.dto.SubmitOrderDTO;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 提交订单适配
 *
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@Slf4j
@Component
public class SubmitOrderManager {

    @Autowired
    private CacheManagerUtil cacheManagerUtil;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private SegmentFeignClient segmentFeignClient;

    @Autowired
    private SkuStockLockFeignClient skuStockLockFeignClient;

    @Autowired
    private CouponOrderFeignClient couponOrderFeignClient;

    @Autowired
    private UserScoreLockFeignClient userScoreLockFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    TCouponFeignClient couponFeignClient;

    public ServerResponseEntity<ShopCartOrderMergerVO> checkSubmitInfo(SubmitOrderDTO submitOrderParam, Long userId) {
        ShopCartOrderMergerVO mergerOrder = cacheManagerUtil.getCache(OrderCacheNames.ORDER_CONFIRM_KEY, String.valueOf(userId));
        // 看看订单有没有过期
        if (mergerOrder == null) {
            return ServerResponseEntity.showFailMsg("订单已过期，请重新提交");
        }

        if (Objects.nonNull(mergerOrder.getUserHasScore()) && Objects.nonNull(mergerOrder.getUsableScore())) {
            if (mergerOrder.getUserHasScore() < mergerOrder.getUsableScore()) {
                throw new LuckException(ResponseEnum.NOT_SCORE);
            }
        }

        // 防止重复提交
        boolean cad = RedisUtil.cad(OrderCacheNames.ORDER_CONFIRM_UUID_KEY + CacheNames.UNION + userId, String.valueOf(userId));
        if (!cad) {
            return ServerResponseEntity.fail(ResponseEnum.REPEAT_ORDER);
        }

        // 多家店铺提交订单时，配送方式只能是快递
        if (!Objects.equals(mergerOrder.getDvyType(), DeliveryType.DELIVERY.value()) && mergerOrder.getShopCartOrders().size() > 1) {
            return ServerResponseEntity.showFailMsg("多家店铺提交订单时，配送方式只能是快递");
        }

        // 当前店铺未开启同城配送，或者超出商家配送距离或起送费不够
        if (Objects.equals(mergerOrder.getDvyType(), DeliveryType.SAME_CITY.value()) &&
                Objects.nonNull(mergerOrder.getShopCityStatus()) && mergerOrder.getShopCityStatus() < 1) {
            return ServerResponseEntity.showFailMsg("当前店铺未开启同城配送，或者超出商家配送距离或起送费不够");
        }

        // 请选择自提点并填写完整的自提信息
        if (Objects.equals(mergerOrder.getDvyType(), DeliveryType.STATION.value())
                && Objects.isNull(mergerOrder.getStationId()) && Objects.isNull(submitOrderParam.getOrderSelfStationDto())) {
            return ServerResponseEntity.showFailMsg("请选择自提点并填写完整的自提信息");
        }

        // 设置自提信息
        mergerOrder.setOrderSelfStation(mapperFacade.map(submitOrderParam.getOrderSelfStationDto(), OrderSelfStationVO.class));

        List<OrderShopDTO> orderShopParams = submitOrderParam.getOrderShopParam();

        List<ShopCartOrderVO> shopCartOrders = mergerOrder.getShopCartOrders();
        // 设置备注
        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
            ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_ORDER);
            if (!segmentIdResponse.isSuccess()) {
                throw new LuckException(segmentIdResponse.getMsg());
            }
            Long orderId = segmentIdResponse.getData();
            // 设置订单id
            shopCartOrder.setOrderId(orderId);
            String orderNumber = "SKX";
            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            //打乱顺序
//        time = cn.hutool.core.util.RandomUtil.randomString(time, 14);

            orderNumber = orderNumber + time + cn.hutool.core.util.RandomUtil.randomNumbers(6);
            shopCartOrder.setOrderNumber(orderNumber);
            for (OrderShopDTO orderShopParam : orderShopParams) {
                if (Objects.equals(shopCartOrder.getShopId(), orderShopParam.getShopId())) {
                    shopCartOrder.setRemarks(orderShopParam.getRemarks());
                }
            }
        }
        //设置发票
        List<OrderInvoiceDTO> orderInvoiceList = submitOrderParam.getOrderInvoiceList();
        if (CollectionUtil.isNotEmpty(orderInvoiceList)) {
            mergerOrder.setOrderInvoiceList(orderInvoiceList);
        }
        return ServerResponseEntity.success(mergerOrder);
    }

    public void tryLockStock(List<ShopCartOrderVO> shopCartOrders) {

        List<SkuStockLockDTO> skuStockLocks = new ArrayList<>();

        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {

            List<ShopCartItemDiscountVO> shopCartItemDiscounts = shopCartOrder.getShopCartItemDiscounts();
            for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartItemDiscounts) {
                List<ShopCartItemVO> shopCartItems = shopCartItemDiscount.getShopCartItems();
                for (ShopCartItemVO orderItem : shopCartItems) {
                    skuStockLocks.add(new SkuStockLockDTO(orderItem.getSpuId(), orderItem.getSkuId(), shopCartOrder.getOrderId(), orderItem.getCount()));
                }
            }
        }

        // 锁定库存
        ServerResponseEntity<?> lockStockResponse = skuStockLockFeignClient.lock(skuStockLocks);
        // 提示具体哪个商品库存不足
        if (Objects.equals(ResponseEnum.NOT_STOCK.value(), lockStockResponse.getCode())) {
            for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
                List<ShopCartItemDiscountVO> shopCartItemDiscounts = shopCartOrder.getShopCartItemDiscounts();
                for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartItemDiscounts) {
                    List<ShopCartItemVO> shopCartItems = shopCartItemDiscount.getShopCartItems();
                    for (ShopCartItemVO orderItem : shopCartItems) {
                        if (Objects.equals(orderItem.getSkuId().toString(), lockStockResponse.getData().toString())) {
                            String skuName = Objects.isNull(orderItem.getSkuName()) ? "" : orderItem.getSkuName();
                            throw new LuckException(orderItem.getSpuName() + " " + skuName + " 库存不足");
                        }
                    }
                }
            }
        }
        if (!lockStockResponse.isSuccess()) {
            throw new LuckException(lockStockResponse);
        }
    }

    public void tryLockCoupon(ShopCartOrderMergerVO mergerOrder) {
        List<ShopCartOrderVO> shopCartOrders = mergerOrder.getShopCartOrders();
        List<Long> orderIds = new ArrayList<>();
        String storeCode = null;
        ServerResponseEntity<String> storeCodeByStoreId = storeFeignClient.getStoreCodeByStoreId(mergerOrder.getStoreId());

        if (storeCodeByStoreId.isSuccess()) {
            storeCode = storeCodeByStoreId.getData();
        } else {
            throw new LuckException("店铺信息查询失败");
        }
        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
            Long orderId = shopCartOrder.getOrderId();
            List<CouponOrderVO> coupons = shopCartOrder.getCoupons();
            if (CollectionUtil.isNotEmpty(coupons)) {
                for (CouponOrderVO coupon : coupons) {
                    if (Objects.equals(Boolean.TRUE, coupon.getChoose())) {
                        coupon.setOrderId(orderId);
                        coupon.setCouponUserId(mergerOrder.getUserId());
                        UpdateCouponStatusDTO updateCouponStatusDTO = new UpdateCouponStatusDTO();
                        updateCouponStatusDTO.setStatus(MyCouponStatus.FREEZE.value());
                        updateCouponStatusDTO.setId(coupon.getCouponUserId());
                        updateCouponStatusDTO.setCouponCode(coupon.getCouponCode());
                        updateCouponStatusDTO.setCouponAmount(shopCartOrder.getCouponReduce());
                        updateCouponStatusDTO.setSourceType(coupon.getSourceType());
                        updateCouponStatusDTO.setOrderNo(orderId);
                        updateCouponStatusDTO.setOrderAmount(shopCartOrder.getActualTotal());
                        updateCouponStatusDTO.setUserId(mergerOrder.getUserId());

                        //如果为crm的优惠券，判断是否在我们系统存在，不存在则添加到t_coupon_user表中
                        if(coupon.getSourceType()==2){
                            couponFeignClient.syncCRMCoupon(coupon);
                        }


                        updateCouponStatusDTO.setStoreCode(storeCode);
                        log.info("调用修改优惠券状态接口  = {}", JSONObject.toJSONString(updateCouponStatusDTO));
                        ServerResponseEntity<Void> voidServerResponseEntity = couponOrderFeignClient.updateCouponStatus(updateCouponStatusDTO);
                        if (!voidServerResponseEntity.isSuccess()) {
                            throw new LuckException(voidServerResponseEntity.getMsg());
                        }
                    }
                }
            }
            orderIds.add(orderId);
        }

        // 锁优惠券
//        List<LockCouponDTO> lockCouponParams = getLockCouponDto(mergerOrder, orderIds);
//        if (CollectionUtil.isNotEmpty(lockCouponParams)) {
//            //todo 优惠券锁定状态接口修改
//            UpdateCouponStatusDTO updateCouponStatusDTO = new UpdateCouponStatusDTO();
//            updateCouponStatusDTO.setStatus(MyCouponStatus.FREEZE.value());
//            updateCouponStatusDTO.setId(lockCouponParams.get(0).getCouponUserId());
//            updateCouponStatusDTO.setOrderNo(Long.valueOf(lockCouponParams.get(0).getOrderIds()));
//            ServerResponseEntity<Void> voidServerResponseEntity = couponOrderFeignClient.updateCouponStatus(updateCouponStatusDTO);
////            ServerResponseEntity<Void> lockCouponResponse = couponOrderFeignClient.lockCoupon(lockCouponParams);
//            if (!voidServerResponseEntity.isSuccess()) {
//                throw new LuckException(voidServerResponseEntity.getMsg());
//            }
//        }
    }

    /**
     * 尝试锁定优惠券
     *
     * @param mergerOrder
     * @param orderIds
     */
    private List<LockCouponDTO> getLockCouponDto(ShopCartOrderMergerVO mergerOrder, List<Long> orderIds) {
        // 锁定优惠券
        // 平台优惠券
        List<LockCouponDTO> lockCouponParams = new ArrayList<>();
        LockCouponDTO platformLockCouponParam = getLockCouponDTO(mergerOrder.getCoupons());
        if (platformLockCouponParam != null) {
            // 平台优惠券涉及多个订单，所以设置订单id为多个订单id以逗号分割
            platformLockCouponParam.setOrderIds(StrUtil.join(StrUtil.COMMA, orderIds));
            lockCouponParams.add(platformLockCouponParam);
        }
        // 店铺优惠券
        for (ShopCartOrderVO shopCartOrder : mergerOrder.getShopCartOrders()) {
            LockCouponDTO shopLockCouponParam = getLockCouponDTO(shopCartOrder.getCoupons());
            if (shopLockCouponParam != null) {
                lockCouponParams.add(shopLockCouponParam);
            }
        }

        return lockCouponParams;
    }

    private LockCouponDTO getLockCouponDTO(List<CouponOrderVO> couponOrders) {
        if (CollectionUtil.isEmpty(couponOrders)) {
            return null;
        }
        for (CouponOrderVO couponOrder : couponOrders) {
            if ((Objects.equals(Boolean.TRUE, couponOrder.getChoose())) && couponOrder.getCanUse()) {
                LockCouponDTO param = new LockCouponDTO();
                param.setOrderIds(String.valueOf(couponOrder.getOrderId()));
                param.setCouponId(couponOrder.getCouponId());
                param.setCouponUserId(couponOrder.getCouponUserId());
                param.setReduceAmount(couponOrder.getReduceAmount());
                return param;
            }
        }
        return null;
    }

    public void tryLockScore(ShopCartOrderMergerVO mergerOrder) {
        // 如果没有使用积分，就不用锁定积分啦
        if (Objects.isNull(mergerOrder.getUsableScore()) || Objects.equals(mergerOrder.getUsableScore(), 0L)) {
            return;
        }
        List<ShopCartOrderVO> shopCartOrders = mergerOrder.getShopCartOrders();
        List<UserScoreLockDTO> userScoreLocks = new ArrayList<>();
        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
            if (Objects.equals(shopCartOrder.getUseScore(), 0L)) {
                continue;
            }
            userScoreLocks.add(new UserScoreLockDTO(shopCartOrder.getOrderId(), shopCartOrder.getUseScore(), mergerOrder.getOrderType().value()));
        }

        // 锁定积分
        userScoreLockFeignClient.lock(userScoreLocks);

    }

    public ServerResponseEntity<ShopCartOrderMergerVO> checkSubmitInfoTest(SubmitOrderDTO submitOrderParam, Long userId) {
        ShopCartOrderMergerVO mergerOrder = cacheManagerUtil.getCache(OrderCacheNames.ORDER_CONFIRM_KEY, String.valueOf(userId));
        // 看看订单有没有过期
        if (mergerOrder == null) {
            return ServerResponseEntity.showFailMsg("订单已过期，请重新提交");
        }

        if (Objects.nonNull(mergerOrder.getUserHasScore()) && Objects.nonNull(mergerOrder.getUsableScore())) {
            if (mergerOrder.getUserHasScore() < mergerOrder.getUsableScore()) {
                throw new LuckException(ResponseEnum.NOT_SCORE);
            }
        }

        // 防止重复提交
//        boolean cad = RedisUtil.cad(OrderCacheNames.ORDER_CONFIRM_UUID_KEY + CacheNames.UNION + userId, String.valueOf(userId));
//        if (!cad) {
//            return ServerResponseEntity.fail(ResponseEnum.REPEAT_ORDER);
//        }

        // 多家店铺提交订单时，配送方式只能是快递
        if (!Objects.equals(mergerOrder.getDvyType(), DeliveryType.DELIVERY.value()) && mergerOrder.getShopCartOrders().size() > 1) {
            return ServerResponseEntity.showFailMsg("多家店铺提交订单时，配送方式只能是快递");
        }

        // 当前店铺未开启同城配送，或者超出商家配送距离或起送费不够
        if (Objects.equals(mergerOrder.getDvyType(), DeliveryType.SAME_CITY.value()) &&
                Objects.nonNull(mergerOrder.getShopCityStatus()) && mergerOrder.getShopCityStatus() < 1) {
            return ServerResponseEntity.showFailMsg("当前店铺未开启同城配送，或者超出商家配送距离或起送费不够");
        }

        // 请选择自提点并填写完整的自提信息
        if (Objects.equals(mergerOrder.getDvyType(), DeliveryType.STATION.value())
                && Objects.isNull(mergerOrder.getStationId()) && Objects.isNull(submitOrderParam.getOrderSelfStationDto())) {
            return ServerResponseEntity.showFailMsg("请选择自提点并填写完整的自提信息");
        }

        // 设置自提信息
        mergerOrder.setOrderSelfStation(mapperFacade.map(submitOrderParam.getOrderSelfStationDto(), OrderSelfStationVO.class));

        List<OrderShopDTO> orderShopParams = submitOrderParam.getOrderShopParam();

        List<ShopCartOrderVO> shopCartOrders = mergerOrder.getShopCartOrders();
        // 设置备注
        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
            ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_ORDER);
            if (!segmentIdResponse.isSuccess()) {
                throw new LuckException(segmentIdResponse.getMsg());
            }
            Long orderId = segmentIdResponse.getData();
            // 设置订单id
            shopCartOrder.setOrderId(orderId);
            for (OrderShopDTO orderShopParam : orderShopParams) {
                if (Objects.equals(shopCartOrder.getShopId(), orderShopParam.getShopId())) {
                    shopCartOrder.setRemarks(orderShopParam.getRemarks());
                }
            }
        }
        //设置发票
        List<OrderInvoiceDTO> orderInvoiceList = submitOrderParam.getOrderInvoiceList();
        if (CollectionUtil.isNotEmpty(orderInvoiceList)) {
            mergerOrder.setOrderInvoiceList(orderInvoiceList);
        }
        return ServerResponseEntity.success(mergerOrder);
    }
}
