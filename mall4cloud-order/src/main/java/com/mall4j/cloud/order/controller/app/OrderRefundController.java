package com.mall4j.cloud.order.controller.app;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetaftersaleorderResponse;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.EcaftersaleAddResponse;
import com.mall4j.cloud.api.biz.feign.ChannlesFeignClient;
import com.mall4j.cloud.api.biz.feign.LiveStoreClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.common.cache.constant.OrderCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.order.constant.OrderSource;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.order.constant.RefundApplyType;
import com.mall4j.cloud.order.constant.RefundStatusEnum;
import com.mall4j.cloud.order.constant.RefundType;
import com.mall4j.cloud.order.constant.ReturnProcessStatusEnum;
import com.mall4j.cloud.order.dto.app.OrderRefundDTO;
import com.mall4j.cloud.order.dto.app.OrderRefundDeliveryDTO;
import com.mall4j.cloud.order.dto.app.OrderRefundPageDTO;
import com.mall4j.cloud.order.dto.platform.request.ComplaintOrderDetailRequest;
import com.mall4j.cloud.order.dto.platform.response.ComplaintDetailSKXResponse;
import com.mall4j.cloud.order.mapper.OrderRefundMapper;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.model.OrderRefund;
import com.mall4j.cloud.order.model.OrderRefundAddr;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.service.OrderRefundAddrService;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.service.impl.ComplaintOrderService;
import com.mall4j.cloud.order.vo.OrderRefundAddrVO;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单退款记录信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
@Slf4j
@RestController("appOrderRefundController")
@RequestMapping("/order_refund")
@Api(tags = "app-订单退款记录信息")
public class OrderRefundController {

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderRefundAddrService orderRefundAddrService;

    @Autowired
    LiveStoreClient liveStoreClient;
    @Autowired
    ComplaintOrderService complaintOrderService;

    @Autowired
    OrderRefundMapper orderRefundMapper;
    @Autowired
    ChannlesFeignClient channlesFeignClient;

    @PostMapping("/apply")
    @ApiOperation(value = "申请退款", notes = "申请退款")
    public ServerResponseEntity<Long> apply(@Valid @RequestBody OrderRefundDTO orderRefundParam) {
        Long userId = AuthUserContext.get().getUserId();
        // redis锁控制并发请求，一笔订单单次只允许一个请求申请售后
        String redisKey = OrderCacheNames.ORDER_REFUND_LOCK_KEY + orderRefundParam.getOrderId();
        if(RedisUtil.hasKey(redisKey)){
            log.info("redis锁控制并发，当前会员发起重复的订单售后请求被拦截。参数 --> userId:{} param:{}",userId,JSONObject.toJSONString(orderRefundParam));
            Assert.faild("您的操作太频繁，请稍后再试！");
        }
        //设置锁超时时间60秒
        RedisUtil.set(redisKey,"1",60);

        // 生成退款单信息
        OrderRefund newOrderRefund = new OrderRefund();
        try {
            // 获取订单信息
            Order order = orderService.getOrderByOrderIdAndUserId(orderRefundParam.getOrderId(), userId);
            //视频号4.0订单不允许在小程序端发起售后。
            if(order.getOrderSource()== OrderSource.CHANNELS.value()){
                Assert.faild("视频号小店订单不允许在小程序端发起售后。");
            }

            //视频号3.0订单不允许整单退款
            if(StrUtil.isNotEmpty(order.getTraceId())){
                if(orderRefundParam.getRefundType() ==1 ){
                    Assert.faild("视频号订单不允许整单售后，请分别发起售后。");
                }

//            OrderItemVO orderItemVO = orderItemService.getByOrderItemId(orderRefundParam.getOrderItemId());
//            if(orderRefundParam.getRefundCount()<orderItemVO.getCount()){
//                Assert.faild("该订单是视频号订单，申请退款不允许修改退货数量。");
//            }
                /**
                 * 视频号订单发起的退单记录。
                 * 视频号规则只允许一个订单项申请一次。如果想重新发起，只允许去视频号后台更新该退单的售后信息。
                 * 更新后，会重置该退单的状态为买家申请。
                 */
                Integer orderRefundCount = orderRefundService.getCountByOrderItemId(orderRefundParam.getOrderItemId());
                if(orderRefundCount !=null && orderRefundCount>0){
                    Assert.faild("该订单是视频号订单，请去视频号更新退款信息或者重新发起售后！");
                }

            }

            // 商品是否已经发货
            boolean isDelivered = Objects.equals(order.getStatus(), OrderStatus.CONSIGNMENT.value()) || Objects.equals(order.getStatus(), OrderStatus.SUCCESS.value());

            ServerResponseEntity<Long> checkOrderStatusResponse = checkOrderStatus(orderRefundParam, order, isDelivered);
            if (!checkOrderStatusResponse.isSuccess()) {
                return checkOrderStatusResponse;
            }

            // 获取所有正在进行中的退款订单
            List<OrderRefund> orderRefunds = orderRefundService.getProcessOrderRefundByOrderId(order.getOrderId());

            for (OrderRefund orderRefund : orderRefunds) {
                if (Objects.equals(RefundType.ALL.value(), orderRefund.getRefundType())) {
                    return ServerResponseEntity.showFailMsg("该订单正在进行整单退款，无法进行新的退款操作");
                }
            }

            //单个商品退款，判断是否有剩余商品可退
            if (Objects.equals(RefundType.SINGLE.value(), orderRefundParam.getRefundType())) {
                OrderItemVO orderItemVO = orderItemService.getByOrderItemId(orderRefundParam.getOrderItemId());
                //计算当前正在进行退款订单的退货数量
                int allRefundCount = orderRefunds.stream().filter(s -> Objects.equals(s.getOrderItemId(), orderRefundParam.getOrderItemId()))
                        .collect(Collectors.summingInt(OrderRefund::getRefundCount));
                if((allRefundCount + orderRefundParam.getRefundCount()) > orderItemVO.getCount()){
                    return ServerResponseEntity.showFailMsg("该商品正在进行退款中，无法进行新的退款操作");
                }
            }


            // 如果存在分销订单，则计算分销总金额
            List<OrderItem> orderItemList = orderItemService.listOrderItemsByOrderId(order.getOrderId());
            // 获取所有的订单项总数
            int orderItemCount = orderItemList.size();
            // 判断退款单类型（1:整单退款,2:单个物品退款）
            if (orderRefundParam.getRefundType().equals(RefundType.ALL.value())) {
                // 全部物品退款
                // 计算该订单项的分销金额
                newOrderRefund.setDistributionTotalAmount(orderService.sumTotalDistributionAmountByOrderItem(orderItemList));
                // 计算平台退款金额（退款时将这部分钱退回给平台，所以商家要扣除从平台这里获取的金额）
                newOrderRefund.setPlatformRefundAmount(order.getPlatformAmount());
                newOrderRefund.setPlatformRefundCommission(order.getPlatformCommission());
            } else {
                ServerResponseEntity<Long> doItemRefundResponse = doItemRefund(orderRefundParam, order, isDelivered, newOrderRefund, orderRefunds, orderItemList, orderItemCount);
                if (!doItemRefundResponse.isSuccess()){
                    return doItemRefundResponse;
                }
            }

            newOrderRefund.setShopId(order.getStoreId());
            newOrderRefund.setUserId(order.getUserId());
            newOrderRefund.setOrderId(order.getOrderId());
            newOrderRefund.setRefundType(orderRefundParam.getRefundType());

            newOrderRefund.setRefundAmount(orderRefundParam.getRefundAmount());
            newOrderRefund.setRefundFreightAmount(orderRefundParam.getRefundFreightAmount());
            if (Objects.equals(RefundType.ALL.value(), orderRefundParam.getRefundType())) {
                newOrderRefund.setOrderItemId(0L);
                newOrderRefund.setRefundCount(order.getAllCount());
            } else {
                newOrderRefund.setOrderItemId(orderRefundParam.getOrderItemId());
                newOrderRefund.setRefundCount(orderRefundParam.getRefundCount());
            }
            newOrderRefund.setApplyType(orderRefundParam.getApplyType());
            newOrderRefund.setApplySource(0);
            if (Objects.equals(orderRefundParam.getApplyType(), RefundApplyType.REFUND_AND_RETURNS.value())) {
                newOrderRefund.setIsReceived(1);
            } else {
                newOrderRefund.setIsReceived(orderRefundParam.getReceived());
            }
            newOrderRefund.setBuyerReason(orderRefundParam.getBuyerReason());
            newOrderRefund.setBuyerDesc(orderRefundParam.getBuyerDesc());
            newOrderRefund.setBuyerMobile(orderRefundParam.getBuyerMobile());
            newOrderRefund.setImgUrls(orderRefundParam.getImgUrls());
            newOrderRefund.setReturnMoneySts(ReturnProcessStatusEnum.APPLY.value());
            newOrderRefund.setUpdateTime(new Date());
            orderRefundService.applyRefund(newOrderRefund);

            //视频号订单推送信息到微信视频号测
            if(StrUtil.isNotEmpty(order.getTraceId())){
                ServerResponseEntity<EcaftersaleAddResponse> ecaftersaleAddResponse = liveStoreClient.ecaftersaleAdd(newOrderRefund.getRefundId());
                if(ecaftersaleAddResponse==null || ecaftersaleAddResponse.isFail()
                        || ecaftersaleAddResponse.getData()==null || ecaftersaleAddResponse.getData().getErrcode()>0 ){
                    Assert.faild("该视频号订单发起售后同步到微信测失败，请联系客服。");
                }else{
                    Long aftersaleId =ecaftersaleAddResponse.getData().getAftersaleId();
                    orderRefundService.syncAftersaleId(newOrderRefund.getRefundId(),aftersaleId);
                }
            }
        }finally {
            //申请退款结束，手动删除锁
            RedisUtil.del(redisKey);
            log.info("发起售后锁删除成功。--> {}",redisKey);
        }

        return ServerResponseEntity.success(newOrderRefund.getRefundId());
    }


    private ServerResponseEntity<Long> doItemRefund(OrderRefundDTO orderRefundParam, Order order, boolean isDelivered, OrderRefund newOrderRefund, List<OrderRefund> orderRefunds, List<OrderItem> orderItemList, int orderItemCount) {
        // 部分物品退款
        OrderItemVO orderItemVO = orderItemService.getByOrderItemId(orderRefundParam.getOrderItemId());
        if (orderItemVO == null) {
            // 该物品在订单中不存在
            return ServerResponseEntity.fail(ResponseEnum.ORDER_NOT_EXIST);
        }

        boolean isCanRefund = true;
        //  查看是否有支付金额和积分都为空的订单，有则抛出异常
        for (OrderItem item : orderItemList) {
            if (item.getActualTotal() <= 0 && item.getUseScore() <= 0) {
                isCanRefund = false;
                break;
            }
        }
        if (!isCanRefund) {
            // 该订单部分订单项支付金额和积分为0，无法使用部分退款
            return ServerResponseEntity.showFailMsg("该订单部分订单项支付金额和积分为0，无法使用部分退款");
        }

        // 是否为最后一项
        boolean isEndItem = Objects.equals(orderRefunds.size(), orderItemCount - 1);
        // 用户实际能退的运费 = 订单当前运费 - 平台减免运费
        long freightAmount = order.getFreightAmount()  - order.getPlatformFreeFreightAmount();
//        // 如果为最后一项并且不为确认收货或待收货状态，则进行加上运费判断是不是为部分退款
//        Long itemActualTotal = orderItemVO.getActualTotal();
//        if (isEndItem && !isDelivered) {
//            itemActualTotal = orderItemVO.getActualTotal() + freightAmount;
//        }
        // 计算该订单项的分销金额
        OrderItem tempOrderItem = mapperFacade.map(orderItemVO, OrderItem.class);
        newOrderRefund.setDistributionTotalAmount(orderService.sumTotalDistributionAmountByOrderItem(Collections.singletonList(tempOrderItem)));

        // 计算平台退款金额（退款时将这部分钱退回给平台，所以商家要扣除从平台这里获取的金额）
        // 平台退款金额 = 平台优惠金额 *（退款金额 / 实付金额）
        Long changePlatformAmount = PriceUtil.divideByBankerRounding(orderItemVO.getPlatformShareReduce() * orderRefundParam.getRefundAmount(), orderItemVO.getActualTotal());

        //此处设置实际平台抵现金额
        newOrderRefund.setPlatformRefundAmount(changePlatformAmount);

        // 平台佣金应退 = 平台佣金 *（退款金额 / 实付金额）
        Long changePlatformCommission = PriceUtil.divideByBankerRounding(orderItemVO.getPlatformCommission() * orderRefundParam.getRefundAmount(), orderItemVO.getActualTotal());
        newOrderRefund.setPlatformRefundCommission(changePlatformCommission);


        // 退款物品数量为null或者0时，则为退款全部数量
        if (orderRefundParam.getRefundCount() <= 0) {
            orderRefundParam.setRefundCount(orderItemVO.getCount());
        }

        // 判断退款数量是否溢出
        if (orderRefundParam.getRefundCount() > orderItemVO.getCount()) {
            // 退款物品数量已超出订单中的数量，不允许申请
            return ServerResponseEntity.showFailMsg("退款物品数量已超出订单中的数量，不允许申请");
        }

        // 判断退款金额是否超出订单金额3种情况
        double refundSingleAmount = Arith.div(orderRefundParam.getRefundAmount(), orderRefundParam.getRefundCount(), 3);
        double singleAmount = Arith.div(orderItemVO.getActualTotal(), orderItemVO.getCount(), 3);
        // 可以退款的实际金额应该为
        double productTotalAmount = orderItemVO.getSpuTotalAmount() + orderItemVO.getShopChangeFreeAmount();
        // 1.如果是此笔订单最后一件并且不为确认收货或待收货状态，则订单项加上运费进行判断。
        if (isEndItem && !isDelivered) {
            productTotalAmount = Arith.add(productTotalAmount, freightAmount);
            singleAmount = Arith.add(singleAmount, freightAmount);
        }
        // 2.如果不是直接跟订单项进行判断
        if (refundSingleAmount > productTotalAmount || refundSingleAmount > singleAmount) {
            // 退款金额已超出订单金额，无法申请
            return ServerResponseEntity.showFailMsg("退款金额已超出订单金额，无法申请");
        }
        // 3.当前退款金额  +  已申请退款金额 > 订单实际支付总额， 就不能退款了
        double refundAmount = orderRefundParam.getRefundAmount();
        // 已退款总金额
        double alreadyRefundTotal = orderRefunds.stream().mapToDouble(OrderRefund::getRefundAmount).sum();
        if (Arith.add(refundAmount, alreadyRefundTotal) > order.getActualTotal()) {
            // 退款金额已超出订单金额，无法申请
            return ServerResponseEntity.showFailMsg("退款金额已超出订单金额，无法申请");
        }
        // 一个订单项只能申请一次退款
        for (OrderRefund orderRefund : orderRefunds) {
            if (Objects.equals(orderRefund.getOrderId(), orderItemVO.getOrderItemId())) {
                // 退款订单项已处理，请勿重复申请
                return ServerResponseEntity.showFailMsg("退款订单项已处理，请勿重复申请");
            }
        }
        return ServerResponseEntity.success();
    }

    private ServerResponseEntity<Long> checkOrderStatus(OrderRefundDTO orderRefundParam, Order order, boolean isDelivered) {
        //待收货或者确认收货 -> 整单退款 -> 退款金额 < 订单金额 - 运费金额
        if (isDelivered && orderRefundParam.getRefundType().equals(RefundType.ALL.value())) {
            Long refundAmount = order.getActualTotal() - order.getFreightAmount() - order.getPlatformFreeFreightAmount();
            if (orderRefundParam.getRefundAmount() > refundAmount) {
                // 退款金额已超出订单金额，无法申请
                return ServerResponseEntity.showFailMsg("退款金额已超出订单金额");
            }
        }

        if (!Objects.equals(order.getIsPayed(), 1)) {
            // 当前订单还未付款，无法申请
            return ServerResponseEntity.showFailMsg("当前订单还未付款，无法申请退款");
        }

        if (Objects.equals(order.getStatus(), OrderStatus.CLOSE.value())) {
            // 当前订单已失败，不允许退款
            return ServerResponseEntity.showFailMsg("当前订单已关闭");
        }

        if (Objects.equals(order.getStatus(), OrderStatus.WAIT_GROUP.value())) {
            // 当前订单正在等待成团状态，需等待成团才能进行下一步操作
            return ServerResponseEntity.showFailMsg("当前订单正在等待成团状态，需等待成团才能进行下一步操作");
        }


        if (orderRefundParam.getRefundAmount() > order.getActualTotal()) {
            return ServerResponseEntity.showFailMsg("退款金额已超出订单金额，无法申请");
        }


        if (!orderRefundService.checkRefundDate(order)) {
            return ServerResponseEntity.showFailMsg("当前订单已超过可退款时间，无法退款");
        }
        if (Objects.equals(order.getOrderType(), OrderType.SCORE.value())) {
            //
            return ServerResponseEntity.showFailMsg("积分商品，无法退款");
        }

        boolean hasRefund =  !Objects.isNull(order.getRefundStatus()) && !Objects.equals(order.getRefundStatus(), RefundStatusEnum.DISAGREE.value());
        if (Objects.equals(orderRefundParam.getRefundType(), RefundType.ALL.value()) && hasRefund) {
            // 该订单已有商品正在退款中，不能再进行整单退款
            return ServerResponseEntity.showFailMsg("该订单已有商品正在退款中，不能再进行整单退款");
        }
        return ServerResponseEntity.success();
    }

    /**
     * 用户提交物流公司信息
     */
    @PostMapping("/submit_express")
    @ApiOperation(value = "提交退款订单物流填写信息", notes = "提交退款订单物流填写信息")
    public ServerResponseEntity<Void> submitExpress(@Valid @RequestBody OrderRefundDeliveryDTO orderRefundExpressParam) {
        log.info("提交退款订单物流填写信息参数:{}", JSONObject.toJSONString(orderRefundExpressParam));
        OrderRefundVO orderRefundVO = orderRefundService.getByRefundId(orderRefundExpressParam.getRefundId());
        if (Objects.isNull(orderRefundVO)) {
            // 查询不到退款信息
            return ServerResponseEntity.showFailMsg("退款订单不存在");
        }

        Long userId = AuthUserContext.get().getUserId();
        if (!Objects.equals(orderRefundVO.getUserId(), userId)) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }

        if (!Objects.equals(orderRefundVO.getApplyType(), RefundApplyType.REFUND_AND_RETURNS.value())) {
            // 当前申请类型不允许提交物流信息操作
            return ServerResponseEntity.showFailMsg("当前申请类型不允许提交物流信息操作");
        }
        if (!Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.PROCESSING.value())
                && !Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.CONSIGNMENT.value())) {
            // 当前状态不允许提交物流信息操作
            return ServerResponseEntity.showFailMsg("当前状态不允许提交物流信息操作");
        }

        // 填写物流信息 申请退货的时候已经有退货信息了
        OrderRefundAddr orderRefundAddr = orderRefundAddrService.getByRefundId(orderRefundExpressParam.getRefundId());
        orderRefundAddr.setSenderMobile(orderRefundExpressParam.getMobile());
        orderRefundAddr.setSenderRemarks(orderRefundExpressParam.getSenderRemarks());
        orderRefundAddr.setDeliveryCompanyId(orderRefundExpressParam.getDeliveryCompanyId());
        orderRefundAddr.setDeliveryName(orderRefundExpressParam.getDeliveryName());
        orderRefundAddr.setDeliveryNo(orderRefundExpressParam.getDeliveryNo());
        orderRefundAddr.setImgs(orderRefundExpressParam.getImgs());
        orderRefundAddr.setCreateTime(new Date());

        OrderRefund orderRefund = mapperFacade.map(orderRefundVO, OrderRefund.class);
        orderRefundService.submitExpress(orderRefund, orderRefundAddr);
        // 提交成功
        //通知视频号物流信息
        EsOrderBO esOrder = orderService.getEsOrder(orderRefund.getOrderId());
        if (StrUtil.isNotEmpty(esOrder.getTraceId())){
            liveStoreClient.aftersaleUploadLogistics(orderRefundExpressParam.getRefundId());
        }
        return ServerResponseEntity.success();
    }


    /**
     * 用户撤销退货退款申请
     */
    @PutMapping("/cancel")
    @ApiOperation(value = "撤销退货退款申请")
    public ServerResponseEntity<Void> cancel(@RequestBody Long refundId) {
        OrderRefundVO orderRefundVO = orderRefundService.getByRefundId(refundId);
        if (Objects.isNull(orderRefundVO)) {
            // 撤销失败 退款订单不存在
            return ServerResponseEntity.showFailMsg("退款订单不存在");
        }
        if (Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.PROCESSING.value())) {
            // 卖家正在处理退款，不能撤销退款申请
            return ServerResponseEntity.showFailMsg("卖家正在处理退款，不能撤销退款申请");
        }
        if (Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.CONSIGNMENT.value())) {
            // 买家已发货，不能撤销退款申请
            return ServerResponseEntity.showFailMsg("买家已发货，不能撤销退款申请");
        }
        if (Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.RECEIVE.value())) {
            // 卖家已收货，不能撤销退款申请
            return ServerResponseEntity.showFailMsg("卖家已收货，不能撤销退款申请");
        }
        if (Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.SUCCESS.value())) {
            return ServerResponseEntity.showFailMsg("退款已成功，不能撤销退款申请");
        }
        Long userId = AuthUserContext.get().getUserId();

        // 查看订单是否还有处于处理中的退款单，如果没有则修改订单退款状态为关闭状态
        Order order = orderService.getOrderByOrderIdAndUserId(orderRefundVO.getOrderId(), userId);

        //如果订单状态为待发货、包含运费、单个商品退款，且所有订单项都进行退款，则不能再取消退款（取消退款后再退款会导致重复退运费bug）
        if (Objects.equals(order.getStatus(),OrderStatus.PAYED.value()) && Objects.equals(orderRefundVO.getRefundType(),RefundType.SINGLE.value())
                && order.getFreightAmount() - order.getPlatformFreeFreightAmount() > 0){
            // 退款数量
            int refundCount = orderRefundService.countByReturnMoneyStsAndOrderId(1, 6, order.getOrderId());
            // 订单项数量
            int orderItemCount = orderItemService.countByOrderId(order.getOrderId());
            if (refundCount == orderItemCount){
                // 该订单所有商品都进行退款，已无法取消退款
                return ServerResponseEntity.showFailMsg("该订单所有商品都进行退款，已无法取消退款");
            }
        }

        if (Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.SUCCESS.value()) ||
                Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.FAIL.value())) {
            // 撤销失败 当前状态不允许此操作
            return ServerResponseEntity.showFailMsg("撤销失败 当前状态不允许此操作");
        }
        //如果为视频号订单，调用视频号的退款取消操作。
        if(StrUtil.isNotEmpty(order.getTraceId())){
            ServerResponseEntity<BaseResponse> baseResponseServerResponseEntity =liveStoreClient.ecaftersaleCancel(refundId);

            if(baseResponseServerResponseEntity==null || baseResponseServerResponseEntity.isFail() || baseResponseServerResponseEntity.getData()==null){
                Assert.faild("");
            }
        }

        orderRefundService.cancelRefund(orderRefundVO);



        return ServerResponseEntity.success();
    }

    /**
     * 查看退款订单详情
     */
    @GetMapping("/info")
    @ApiOperation(value = "查看退款订单详情", notes = "查看退款订单详情")
    public ServerResponseEntity<OrderRefundVO> info(Long refundId) {
        // 查询详情
        OrderRefundVO orderRefundVO = orderRefundService.getDetailByRefundId(refundId);

        if (orderRefundVO == null) {
            // 查看失败 该退款订单不存在
            return ServerResponseEntity.showFailMsg("该退款订单不存在");
        }

        if (!Objects.equals(orderRefundVO.getUserId(), AuthUserContext.get().getUserId())) {
            // 查看失败 您没有此权限
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        // 退货地址
        if (Objects.equals(orderRefundVO.getApplyType(), RefundApplyType.REFUND_AND_RETURNS.value()) && orderRefundVO.getHandelTime() !=null) {
            OrderRefundAddr orderRefundAddr = orderRefundAddrService.getByRefundId(refundId);
            orderRefundVO.setOrderRefundAddr(mapperFacade.map(orderRefundAddr, OrderRefundAddrVO.class));
        }
        EsOrderBO order =orderService.getEsOrder(orderRefundVO.getOrderId());
        orderRefundVO.setOrderSource(order.getOrderSource());
        //视频号4。0订单获取微信端售后的详情
        if(order.getOrderSource()==OrderSource.CHANNELS.value()){
            ServerResponseEntity<EcGetaftersaleorderResponse> serverResponse = channlesFeignClient.getEcaftersale(orderRefundVO.getAftersaleId());
            orderRefundVO.setEcGetaftersaleorderResponse(serverResponse.getData());
        }
        return ServerResponseEntity.success(orderRefundVO);
    }

    /**
     * 查看我的退款订单列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "我的退款订单列表", notes = "我的退款订单列表，显示数量时候")
    public ServerResponseEntity<PageVO<OrderRefundVO>> list(PageDTO page, OrderRefundPageDTO orderRefundPageDTO) {
        orderRefundPageDTO.setUserId(AuthUserContext.get().getUserId());
        PageVO<OrderRefundVO> pageList = orderRefundService.page(page, orderRefundPageDTO);
        return ServerResponseEntity.success(pageList);
    }

    @ApiOperation("纠纷单详情")
    @GetMapping("/detail")
    public ServerResponseEntity<ComplaintDetailSKXResponse> detail(ComplaintOrderDetailRequest complaintOrderDetailRequest) {
        ComplaintDetailSKXResponse response = complaintOrderService.detail(complaintOrderDetailRequest.getComplaintOrderId());
        orderRefundMapper.complaintOrderUserRead(complaintOrderDetailRequest.getComplaintOrderId());
        return ServerResponseEntity.success(response);
    }


}
