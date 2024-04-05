package com.mall4j.cloud.order.controller.app;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.biz.feign.LiveStoreClient;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.OrderSelectDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmOrderFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.vo.OrderSelectVo;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.vo.OrderAddrVO;
import com.mall4j.cloud.api.order.vo.OrderShopVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.constant.OrderSource;
import com.mall4j.cloud.common.order.vo.EsOrderItemVO;
import com.mall4j.cloud.common.order.vo.EsOrderVO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.order.config.OrderCancelConfigProperties;
import com.mall4j.cloud.order.constant.RefundStatusEnum;
import com.mall4j.cloud.order.constant.RefundType;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderAddr;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.service.OrderAddrService;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.OrderCountVO;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import com.mysql.cj.log.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 我的订单
 *
 * @author FrozenWatermelon
 */
@Slf4j
@RestController
@RequestMapping("/my_order")
@Api(tags = "app-我的订单接口")
public class MyOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private OrderAddrService orderAddrService;
    @Autowired
    private OnsMQTransactionTemplate orderReceiptTemplate;
    @Autowired
    private CrmOrderFeignClient crmOrderFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    LiveStoreClient liveStoreClient;
    @Autowired
    private OrderCancelConfigProperties orderCancelConfigProperties;

    /**
     * 订单详情信息接口
     */
    @GetMapping("/order_detail")
    @ApiOperation(value = "订单详情信息", notes = "根据订单号获取订单详情信息")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true, dataType = "Long")
    public ServerResponseEntity<OrderShopVO> orderDetail(@RequestParam(value = "orderId") Long orderId) {

        Long userId = AuthUserContext.get().getUserId();
        OrderShopVO orderShopDto = new OrderShopVO();
        Order order = orderService.getOrderByOrderIdAndUserId(orderId, userId);
        if (Objects.equals(order.getShopId(), Constant.PLATFORM_SHOP_ID)) {
            order.setShopName(Constant.PLATFORM_SHOP_NAME);
        }
        OrderAddr orderAddr = orderAddrService.getByOrderAddrId(order.getOrderAddrId());

        List<OrderItemVO> orderItems = orderItemService.listOrderItemAndLangByOrderId(orderId);

        setOrderShopDto(orderShopDto, order, orderAddr, orderItems);

        List<OrderRefundVO> orderRefunds = orderRefundService.getProcessingOrderRefundByOrderId(order.getOrderId());
        long alreadyRefundAmount = 0L;
        for (OrderRefundVO orderRefund : orderRefunds) {
            alreadyRefundAmount = alreadyRefundAmount + orderRefund.getRefundAmount();
            // 整单退款
            if (Objects.equals(RefundType.ALL.value(),orderRefund.getRefundType())) {
                orderShopDto.setCanRefund(false);
                // 统一的退款单号
                for (OrderItemVO orderItemDto : orderItems) {
                    orderItemDto.setFinalRefundId(orderRefund.getRefundId());
                }
                break;
            }
            // 单项退款，每个单号都不一样
            for (OrderItemVO orderItemDto : orderItems) {
                if (Objects.equals(orderItemDto.getOrderItemId(), orderRefund.getOrderItemId())) {
                    orderItemDto.setFinalRefundId(orderRefund.getRefundId());
                }
            }

        }
        orderShopDto.setCanRefundAmount(order.getActualTotal()-alreadyRefundAmount);
        if (order.getRefundStatus() != null && !Objects.equals(order.getRefundStatus(), RefundStatusEnum.DISAGREE.value())) {
            orderShopDto.setFinalRefundId(orderItems.get(0).getFinalRefundId());
        }

        return ServerResponseEntity.success(orderShopDto);
    }

    /**
     * 插入数据
     * @param orderShopDto 插入对象
     * @param order 订单信息
     * @param orderAddr 订单地址
     * @param orderItems 订单项列表
     */
    private void setOrderShopDto(OrderShopVO orderShopDto, Order order, OrderAddr orderAddr, List<OrderItemVO> orderItems) {
        // 处理下发货完成时能否查看物流
        int updateOrViewDeliveryInfo = 0;
        if(!Objects.equals(order.getDeliveryType(), DeliveryType.DELIVERY.value())){
            orderShopDto.setUpdateOrViewDeliveryInfo(updateOrViewDeliveryInfo);
        }
        for (OrderItemVO orderItem : orderItems) {
            if(Objects.nonNull(orderItem.getDeliveryType()) && Objects.equals(orderItem.getDeliveryType(),DeliveryType.DELIVERY.value())){
                updateOrViewDeliveryInfo = 1;
                break;
            }
        }
        orderShopDto.setUpdateOrViewDeliveryInfo(updateOrViewDeliveryInfo);
        orderShopDto.setTraceId(order.getTraceId());
        orderShopDto.setOrderScore(order.getOrderScore());
        orderShopDto.setShopId(order.getShopId());
        orderShopDto.setDeliveryType(order.getDeliveryType());
        orderShopDto.setShopName(order.getShopName());
        orderShopDto.setActualTotal(order.getActualTotal());
        orderShopDto.setOrderAddr(mapperFacade.map(orderAddr, OrderAddrVO.class));
        orderShopDto.setPayType(order.getPayType());
        orderShopDto.setTransfee(order.getFreightAmount());
        orderShopDto.setReduceAmount(Math.abs(order.getReduceAmount()));
        orderShopDto.setCreateTime(order.getCreateTime());
        orderShopDto.setRemarks(order.getRemarks());
        orderShopDto.setOrderType(order.getOrderType());
        orderShopDto.setStatus(order.getStatus());
        // 返回满减优惠金额，优惠券优惠金额和店铺优惠总额
        orderShopDto.setDiscountMoney(order.getDiscountAmount());
        orderShopDto.setShopCouponMoney(order.getShopCouponAmount());
        orderShopDto.setShopAmount(order.getReduceAmount() - order.getPlatformAmount());
        //返回平台优惠券，平台等级，平台积分优惠金额和平台免运费金额
        orderShopDto.setPlatformCouponAmount(order.getPlatformCouponAmount());
        orderShopDto.setMemberAmount(order.getMemberAmount());
        orderShopDto.setScoreAmount(order.getScoreAmount());
        orderShopDto.setPlatformFreeFreightAmount(order.getPlatformFreeFreightAmount());
        orderShopDto.setShopChangeFreeAmount(order.getShopChangeFreeAmount());
        //订单过期时间
        //视频号4.0的订单过期时间跟小程序不一致,动态配置一下
        Date endTime;
        if(OrderSource.CHANNELS.value().equals(order.getOrderSource())){
            endTime = DateUtil.offsetMillisecond(order.getCreateTime(), orderCancelConfigProperties.getChannelsTime());
        }else {
            endTime = DateUtil.offsetMillisecond(order.getCreateTime(), orderCancelConfigProperties.fetchDelayTime());
        }
        //Date endTime = DateUtil.offsetMillisecond(order.getCreateTime(), RocketMqConstant.CANCEL_TIME_INTERVAL);
        orderShopDto.setEndTime(endTime);
        // 付款时间
        orderShopDto.setPayTime(order.getPayTime());
        // 发货时间
        orderShopDto.setDeliveryTime(order.getDeliveryTime());
        // 完成时间
        orderShopDto.setFinallyTime(order.getFinallyTime());
        // 取消时间
        orderShopDto.setCancelTime(order.getCancelTime());
        // 更新时间
        orderShopDto.setUpdateTime(order.getUpdateTime());
        //订单发票id
        orderShopDto.setOrderInvoiceId(order.getOrderInvoiceId());

        // 可以退款的状态，并在退款时间内
        if (order.getStatus() > OrderStatus.UNPAY.value() && order.getStatus() < OrderStatus.CLOSE.value() && orderRefundService.checkRefundDate(order)) {
            orderShopDto.setCanRefund(true);
            orderShopDto.setCanAllRefund(true);
            for (OrderItemVO orderItem : orderItems) {
                // 有正在退款中的订单项
                if (orderItem.getRefundStatus() != null && !Objects.equals(orderItem.getRefundStatus(), RefundStatusEnum.DISAGREE.value())) {
                    // 无法整单退款
                    orderShopDto.setCanAllRefund(false);
                }
            }
            // 正在进行整单退款
            if (order.getRefundStatus() != null && !Objects.equals(order.getRefundStatus(), RefundStatusEnum.DISAGREE.value())) {
                // 所有订单项都没办法退款
                orderShopDto.setCanAllRefund(false);
            }
        }
        orderShopDto.setOrderNumber(order.getOrderNumber());
        orderShopDto.setOrderItems(orderItems);
        orderShopDto.setTotal(order.getTotal());
        orderShopDto.setTotalNum(order.getAllCount());
        //订单来源
        orderShopDto.setOrderSource(order.getOrderSource());
    }

    @GetMapping("/order_count")
    @ApiOperation(value = "计算各个订单数量", notes = "根据订单状态计算各个订单数量")
    public ServerResponseEntity<OrderCountVO> orderCount() {
        Long userId = AuthUserContext.get().getUserId();
        OrderCountVO orderCount = orderService.countNumberOfStatus(userId);
        return ServerResponseEntity.success(orderCount);
    }

    /**
     * 分页获取
     */
    @GetMapping("/search_order")
    @ApiOperation(value = "订单列表信息查询", notes = "根据订单编号或者订单中商品名称搜索")
    public ServerResponseEntity<PageVO<EsOrderVO>> searchOrder(OrderSearchDTO orderSearchDTO) {
        Long userId = AuthUserContext.get().getUserId();
        orderSearchDTO.setUserId(userId);
        orderSearchDTO.setDeleteStatus(0);
        //设置门店storeid为空，查询本人订单时查询全部订单。不用storeId做条件。
        orderSearchDTO.setStoreId(null);
        PageVO<EsOrderVO> esOrderPageVO = orderService.orderPage(orderSearchDTO);
        // 处理下发货完成时能否查看物流
        for (EsOrderVO esOrderVO : esOrderPageVO.getList()) {
            if (Objects.equals(esOrderVO.getShopId(), Constant.PLATFORM_SHOP_ID)) {
                esOrderVO.setShopName(Constant.PLATFORM_SHOP_NAME);
            }
            int updateOrViewDeliveryInfo = 0;
            if(!Objects.equals(esOrderVO.getDeliveryType(), DeliveryType.DELIVERY.value()) && !Objects.equals(esOrderVO.getDeliveryType(), DeliveryType.LOGISTICS.value())){
                esOrderVO.setUpdateOrViewDeliveryInfo(updateOrViewDeliveryInfo);
                continue;
            }
            for (EsOrderItemVO orderItem : esOrderVO.getOrderItems()) {
                if(Objects.nonNull(orderItem.getDeliveryType()) &&
                        (Objects.equals(orderItem.getDeliveryType(),DeliveryType.DELIVERY.value()) ||
                                Objects.equals(orderItem.getDeliveryType(),DeliveryType.LOGISTICS.value()))){
                    updateOrViewDeliveryInfo = 1;
                    break;
                }
            }
            esOrderVO.setUpdateOrViewDeliveryInfo(updateOrViewDeliveryInfo);
        }
        return ServerResponseEntity.success(esOrderPageVO);
    }

    // todo 定时任务将订单项设为不可评论的状态
    /**
     * 订单评价列表接口
     */
    @GetMapping("/search_order_item")
    @ApiOperation(value = "订单评价列表接口", notes = "根据订单评价状态获取订单列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commStatus", value = "订单状态 0:待评价 1已评价", dataTypeClass = Integer.class)
    })
    public ServerResponseEntity<PageVO<EsOrderVO>> searchOrderItem(@RequestParam(value = "commStatus") Integer commStatus,PageDTO page) {

        OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
        Long userId = AuthUserContext.get().getUserId();
        orderSearchDTO.setUserId(userId);
        orderSearchDTO.setPageNum(page.getPageNum());
        orderSearchDTO.setPageSize(page.getPageSize());
        orderSearchDTO.setIsComm(commStatus);
        return ServerResponseEntity.success(orderService.orderPage(orderSearchDTO));
    }

    /**
     * 取消订单
     */
    @PutMapping("/cancel/{orderId}")
    @ApiOperation(value = "根据订单号取消订单", notes = "根据订单号取消订单")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true, dataType = "String")
    public ServerResponseEntity<String> cancel(@PathVariable("orderId") Long orderId) {
        Long userId = AuthUserContext.get().getUserId();
        Order order = orderService.getOrderByOrderIdAndUserId(orderId, userId);
        if (!Objects.equals(order.getStatus(), OrderStatus.UNPAY.value()) && !Objects.equals(order.getStatus(), OrderStatus.CLOSE.value())) {
            // 订单已支付，无法取消订单
            return ServerResponseEntity.showFailMsg("订单已支付，无法取消订单");
        }
        /**
         * 如果为视频号订单，需要调用视频号取消接口。
         */
        if(StrUtil.isNotEmpty(order.getTraceId())){
            log.info("调用视频号订单取消接口,orderId:{}",orderId);
            liveStoreClient.orderClose(orderId);
        }
        // 如果订单未支付的话，将订单设为取消状态
        orderService.cancelOrderAndGetCancelOrderIds(Collections.singletonList(order.getOrderId()));
        return ServerResponseEntity.success();
    }


    /**
     * 确认收货
     */
    @PutMapping("/receipt/{orderId}")
    @ApiOperation(value = "根据订单号确认收货", notes = "根据订单号确认收货")
    public ServerResponseEntity<String> receipt(@PathVariable("orderId") Long orderId) {
        Long userId = AuthUserContext.get().getUserId();
        Order order = orderService.getOrderByOrderIdAndUserId(orderId, userId);

        if (!Objects.equals(order.getStatus(), OrderStatus.CONSIGNMENT.value())) {
            if (Objects.equals(order.getStatus(), OrderStatus.SUCCESS.value())) {
                return ServerResponseEntity.showFailMsg("订单已收货，请刷新页面");
            } else {
                return ServerResponseEntity.showFailMsg("订单状态异常，请刷新后重试");
            }
        }
        if (Objects.equals(order.getRefundStatus(), RefundStatusEnum.APPLY.value())) {
            // 订单退款中，无法确认收货
            return ServerResponseEntity.showFailMsg("订单退款中，无法确认收货");
        }
        List<OrderItem> orderItems = orderItemService.listOrderItemsByOrderId(orderId);
        order.setOrderItems(orderItems);


        //注意：订单的确认收货是需要通知很多个服务的。
        // 在发送普通消息的时候，可能消息已经落地发送了，这边抛了异常，让数据回滚，变为未确认收货的状态，此时用户再点击退货，接着再次点击确认收货，
        // 而接收方确实是收到该消息，订单状态从 确认收货变成了“确认收货”，这两个确认收货的状态有本质的区别，所以应该让订单确确实实变为确认收货状态，再向各个服务推送消息
        // 因此用事务消息会比较好

        // 开启事务消息，通知订单自己，开始往各个服务发送通知了
        SendResult sendResult = orderReceiptTemplate.sendMessageInTransaction(orderId, null);

        if (sendResult == null || sendResult.getMessageId() == null) {
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }

        return ServerResponseEntity.success();
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{orderId}")
    @ApiOperation(value = "根据订单号删除订单", notes = "根据订单号删除订单")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true, dataType = "String")
    public ServerResponseEntity<String> delete(@PathVariable("orderId") Long orderId) {
        Long userId = AuthUserContext.get().getUserId();

        Order order = orderService.getOrderByOrderIdAndUserId(orderId, userId);

        if (!Objects.equals(order.getStatus(), OrderStatus.SUCCESS.value()) && !Objects.equals(order.getStatus(), OrderStatus.CLOSE.value()) ) {
            // 订单未完成或未关闭，无法删除订单
            return ServerResponseEntity.showFailMsg("订单未完成或未关闭，无法删除订单");
        }

        // 删除订单
        orderService.deleteOrder(order.getOrderId());

        return ServerResponseEntity.success();
    }

    @GetMapping("/get_order_item/{orderItemId}")
    @ApiOperation(value = "根据订单项id获取订单项详情", notes = "根据订单项id获取订单项详情")
    @ApiImplicitParam(name = "orderItemId", value = "订单项号", required = true)
    public ServerResponseEntity<OrderItemVO> getOrderItemById(@PathVariable("orderItemId") Long orderItemId) {
        Long userId = AuthUserContext.get().getUserId();
        OrderItemVO orderItemVO = orderItemService.getByOrderItemId(orderItemId);
        if (userId == null || orderItemVO == null || !userId.equals(orderItemVO.getUserId())) {
            return ServerResponseEntity.showFailMsg("订单项错误");
        }
        return ServerResponseEntity.success(orderItemVO);
    }


    @GetMapping("/crmOrderPage")
    @ApiOperation(value = "crm-订单列表", notes = "crm-订单列表")
    public ServerResponseEntity<CrmPageResult<List<OrderSelectVo>>> crmOrderPage(@Valid PageDTO pageDTO, @RequestParam(required = false) String mobile) {
        ServerResponseEntity<UserApiVO> userRep = userFeignClient.getInsiderUserData(AuthUserContext.get().getUserId());
        if (userRep.isSuccess()) {
            OrderSelectDto orderSelectDto = new OrderSelectDto();
            orderSelectDto.setPage_index(pageDTO.getPageNum());
            orderSelectDto.setPage_size(pageDTO.getPageSize());
            if (StringUtils.isNotEmpty(mobile)) {
                orderSelectDto.setMobile(mobile);
            } else {
                orderSelectDto.setMobile(userRep.getData().getPhone());
            }
            return crmOrderFeignClient.orderSelect(orderSelectDto);
        }
        CrmPageResult<List<OrderSelectVo>> crmPageResult = new CrmPageResult<>();
        crmPageResult.setTotal_count(0);
        return ServerResponseEntity.success(crmPageResult);
    }
}
