package com.mall4j.cloud.order.feign;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintDetailRequest;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintDetailResponse;
import com.mall4j.cloud.api.biz.feign.LiveStoreClient;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.dto.InitRefundStatusDTO;
import com.mall4j.cloud.api.order.dto.OrderRefundSaleDTO;
import com.mall4j.cloud.api.order.dto.RefundDeliveryDTO;
import com.mall4j.cloud.api.order.feign.OrderRefundFeignClient;
import com.mall4j.cloud.api.order.vo.OrderRefundAddrVO;
import com.mall4j.cloud.api.order.vo.OrderRefundProdEffectRespVO;
import com.mall4j.cloud.api.order.vo.OrderRefundSimpleVO;
import com.mall4j.cloud.api.order.vo.OrderRefundVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.order.bo.RefundNotifyBO;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.order.constant.RefundApplyType;
import com.mall4j.cloud.order.constant.RefundStatusEnum;
import com.mall4j.cloud.order.constant.RefundType;
import com.mall4j.cloud.order.constant.ReturnProcessStatusEnum;
import com.mall4j.cloud.order.dto.app.OrderRefundDTO;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.model.OrderRefund;
import com.mall4j.cloud.order.model.OrderRefundAddr;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.service.OrderRefundAddrService;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/12/25
 */
@RestController
@Slf4j
public class OrderRefundFeignController implements OrderRefundFeignClient {


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

    @Override
    public ServerResponseEntity<Integer> getRefundStatus(Long refundId) {

        return ServerResponseEntity.success(orderRefundService.getRefundStatus(refundId));
    }



    @Override
    public ServerResponseEntity<Integer> initRefundStatus(InitRefundStatusDTO initRefundStatusDTO) {
        log.info("更新售后单状态，执行参数:{}",JSONObject.toJSONString(initRefundStatusDTO));
        com.mall4j.cloud.order.vo.OrderRefundVO orderRefundVO = orderRefundService.getByAftersaleId(initRefundStatusDTO.getAftersaleId());
        if(orderRefundVO==null ||orderRefundVO.getRefundId()<=0 ){
            Assert.faild(StrUtil.format("售后单据不存在，请检查数据:{}",initRefundStatusDTO.getAftersaleId()));
        }

        /**
         *
         * 2	商家处理退款申请中
         * 4	商家拒绝退款
         * 23	商家处理退货申请中
         */
        Integer newStatus = 1;
        int result = orderRefundService.initRefundStatus(orderRefundVO.getRefundId(),newStatus,initRefundStatusDTO.getBuyerDesc(),initRefundStatusDTO.getImg_urls());

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Integer> ecInitRefundStatus(InitRefundStatusDTO initRefundStatusDTO) {
        log.info("用户视频号4.0更新售后单状态，执行参数:{}",JSONObject.toJSONString(initRefundStatusDTO));
        com.mall4j.cloud.order.vo.OrderRefundVO orderRefundVO = orderRefundService.getByAftersaleId(initRefundStatusDTO.getAftersaleId());
        if(orderRefundVO==null ||orderRefundVO.getRefundId()<=0 ){
            Assert.faild(StrUtil.format("售后单据不存在，请检查数据:{}",initRefundStatusDTO.getAftersaleId()));
        }

        Integer newStatus = 1;
        int result = orderRefundService.ecInitRefundStatus(orderRefundVO.getRefundId(),newStatus,initRefundStatusDTO.getApplyType(),initRefundStatusDTO.getRefundAmount());

        //  退单状态更新，重新推送到中台
        orderRefundService.pushRefund(orderRefundVO.getRefundId(), 1);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<List<OrderRefundProdEffectRespVO>> getProdRefundEffectByDateAndProdIds(List<Long> spuIds, Long startTime, Long endTime) {
        return ServerResponseEntity.success(orderRefundService.getProdRefundEffectByDateAndProdIds(spuIds, new Date(startTime), new Date(endTime)));
    }

    @Override
    public ServerResponseEntity<List<OrderRefundVO>> getOrderRefundByOrderIdAndRefundStatus(Long orderId, Integer returnMoneySts) {
        return ServerResponseEntity.success(orderRefundService.getOrderRefundByOrderIdAndRefundStatus(orderId, returnMoneySts));
    }

    @Override
    public ServerResponseEntity<OrderRefundVO> getOrderRefundByAftersaleId(Long aftersaleId) {
        return ServerResponseEntity.success(orderRefundService.getOrderRefundByAftersaleId(aftersaleId));
    }

    @Override
    public ServerResponseEntity<Void> aftersaleReturnOverdue(Long aftersaleId) {
        orderRefundService.aftersaleReturnOverdue(aftersaleId);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> ecAftersaleReturnOverdue(Long aftersaleId) {
        orderRefundService.ecAftersaleReturnOverdue(aftersaleId);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<OrderRefundSimpleVO>> listOrderIdsByRefundIds(List<Long> refundIds) {
        return ServerResponseEntity.success(orderRefundService.listOrderIdsByRefundIds(refundIds));
    }

    @Override
    public ServerResponseEntity<OrderRefundVO> getOrderRefundByRefundId(Long refundId) {
        return ServerResponseEntity.success(orderRefundService.getOrderRefundByRefundId(refundId));
    }

    @Override
    public ServerResponseEntity<OrderRefundVO> getOrderRefundByRefundNumber(String refundNumber) {
        return ServerResponseEntity.success(orderRefundService.getOrderRefundByRefundNumber(refundNumber));
    }

    @Override
    public ServerResponseEntity<Void> pushRefund(Long refundId, Integer returnStatus) {
        orderRefundService.pushRefund(refundId,returnStatus);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Integer> countRefundSuccessRefundCountByOrderId(Long orderId) {
        return ServerResponseEntity.success(orderRefundService.countRefundSuccessRefundCountByOrderId(orderId));
    }

    @Override
    public ServerResponseEntity<Integer> countReturnProcessingItemByOrderId(Long orderId) {
        return ServerResponseEntity.success(orderRefundService.countReturnProcessingItemByOrderId(orderId));
    }

    @Override
    public ServerResponseEntity<Long> applyRefundSale(OrderRefundSaleDTO orderRefundSaleParam) {
        log.info("开始申请售后，售后数据:{}", JSONObject.toJSONString(orderRefundSaleParam));
        OrderRefundDTO orderRefundParam = mapperFacade.map(orderRefundSaleParam, OrderRefundDTO.class);

//        orderRefundParam.setRefundFreightAmount(0L);
        // 获取订单信息
        Order order = orderService.getByOrderNumber(orderRefundSaleParam.getOrderNumber());
        orderRefundParam.setOrderId(order.getOrderId());
        orderRefundParam.setBuyerMobile(order.getMobile());
        orderRefundParam.setUserId(order.getUserId());
        //视频号订单不允许整单退款
        if (StrUtil.isNotEmpty(order.getTraceId()) && orderRefundParam.getRefundType() == 1) {
            Assert.faild("视频号订单不允许整单售后，请分别发起售后。");
        }

        // 商品是否已经发货
        // 视频号订单售后 不做判断
        boolean isDelivered = Objects.equals(order.getStatus(), OrderStatus.CONSIGNMENT.value()) || Objects.equals(order.getStatus(), OrderStatus.SUCCESS.value());

//        ServerResponseEntity<Long> checkOrderStatusResponse = checkOrderStatus(orderRefundParam, order, isDelivered);
//        if (!checkOrderStatusResponse.isSuccess()) {
//            return checkOrderStatusResponse;
//        }

        // 生成退款单信息
        OrderRefund newOrderRefund = new OrderRefund();

        // 获取所有正在进行中的退款订单
        List<OrderRefund> orderRefunds = orderRefundService.getProcessOrderRefundByOrderId(order.getOrderId());
        //视频号订单不做一个商品只可以退款一次的判断。
//        for (OrderRefund orderRefund : orderRefunds) {
//            if (Objects.equals(RefundType.ALL.value(), orderRefund.getRefundType())) {
//                return ServerResponseEntity.showFailMsg("该订单正在进行整单退款，无法进行新的退款操作");
//            }
//            if (Objects.equals(orderRefund.getOrderItemId(), orderRefundParam.getOrderItemId())) {
//                return ServerResponseEntity.showFailMsg("该商品正在进行退款中，无法进行新的退款操作");
//            }
//        }

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
            if (!doItemRefundResponse.isSuccess()) {
                return doItemRefundResponse;
            }
        }

        newOrderRefund.setAftersaleId(orderRefundSaleParam.getAftersaleId());
        newOrderRefund.setShopId(order.getStoreId());
        newOrderRefund.setUserId(order.getUserId());
        newOrderRefund.setOrderId(order.getOrderId());
        newOrderRefund.setRefundType(orderRefundParam.getRefundType());

        newOrderRefund.setRefundAmount(orderRefundParam.getRefundAmount());
        if (Objects.equals(RefundType.ALL.value(), orderRefundParam.getRefundType())) {
            newOrderRefund.setOrderItemId(0L);
            newOrderRefund.setRefundCount(order.getAllCount());
        } else {
            newOrderRefund.setOrderItemId(orderRefundParam.getOrderItemId());
            newOrderRefund.setRefundCount(orderRefundParam.getRefundCount());
        }
        newOrderRefund.setApplyType(orderRefundParam.getApplyType());
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
        /**
         * 视频号订单，初始化退费运费金额。这里视频号订单不允许修改
         */
        newOrderRefund.setRefundFreightAmount(0l);
        //视频号订单，发起退单设置为买家申请
        newOrderRefund.setApplySource(0);
        orderRefundService.applyRefund(newOrderRefund);
        return ServerResponseEntity.success();
    }

    private ServerResponseEntity<Long> checkOrderStatus(OrderRefundDTO orderRefundParam, Order order, boolean isDelivered) {
        log.info("checkOrderStatus 。params.orderRefundParam:{},order:{},isDelivered:{}",JSONObject.toJSONString(orderRefundParam),JSONObject.toJSONString(order),isDelivered);
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

        boolean hasRefund = !Objects.isNull(order.getRefundStatus()) && !Objects.equals(order.getRefundStatus(), RefundStatusEnum.DISAGREE.value());
        if (Objects.equals(orderRefundParam.getRefundType(), RefundType.ALL.value()) && hasRefund) {
            // 该订单已有商品正在退款中，不能再进行整单退款
            return ServerResponseEntity.showFailMsg("该订单已有商品正在退款中，不能再进行整单退款");
        }
        return ServerResponseEntity.success();
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
        long freightAmount = order.getFreightAmount() - order.getPlatformFreeFreightAmount();
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
            log.info("doItemRefund check ，refundSingleAmount:{},productTotalAmount:{},singleAmount:{}",refundSingleAmount,productTotalAmount,singleAmount);
            // 退款金额已超出订单金额，无法申请
//            return ServerResponseEntity.showFailMsg("退款金额已超出订单金额，无法申请");
        }
        // 3.当前退款金额  +  已申请退款金额 > 订单实际支付总额， 就不能退款了
        double refundAmount = orderRefundParam.getRefundAmount();
        // 已退款总金额
        double alreadyRefundTotal = orderRefunds.stream().mapToDouble(OrderRefund::getRefundAmount).sum();
        if (Arith.add(refundAmount, alreadyRefundTotal) > order.getActualTotal()) {
            log.info("doItemRefund check ，refundAmount:{},alreadyRefundTotal:{},order.getActualTotal:{}",refundAmount,alreadyRefundTotal,order.getActualTotal());
            // 退款金额已超出订单金额，无法申请
//            return ServerResponseEntity.showFailMsg("退款金额已超出订单金额，无法申请");
        }
        //20230322日修改：视频号订单去除当前判断
        // 一个订单项只能申请一次退款
//        for (OrderRefund orderRefund : orderRefunds) {
//            if (Objects.equals(orderRefund.getOrderId(), orderItemVO.getOrderItemId())) {
//                // 退款订单项已处理，请勿重复申请
//                return ServerResponseEntity.showFailMsg("退款订单项已处理，请勿重复申请");
//            }
//        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<OrderRefundAddrVO> getRefundAddr(Long refundId) {
        return ServerResponseEntity.success(orderRefundService.getRefundAddr(refundId));
    }

    @Override
    public ServerResponseEntity<Void> saleCancel(Long aftersaleId, String outAftersaleId) {
        log.info("开始取消售后，售后单号数据:{},{}", aftersaleId, outAftersaleId);
        com.mall4j.cloud.order.vo.OrderRefundVO orderRefundVO = orderRefundService.getByAftersaleId(aftersaleId);
        if (Objects.isNull(orderRefundVO) && StrUtil.isNotBlank(outAftersaleId)) {
            orderRefundVO = orderRefundService.getByRefundNumber(outAftersaleId);
        }
        if (Objects.isNull(orderRefundVO)) {
            // 撤销失败 退款订单不存在
            return ServerResponseEntity.showFailMsg("退款订单不存在");
        }
//        if (Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.PROCESSING.value())) {
//            // 卖家正在处理退款，不能撤销退款申请
//            return ServerResponseEntity.showFailMsg("卖家正在处理退款，不能撤销退款申请");
//        }
//        if (Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.CONSIGNMENT.value())) {
//            // 买家已发货，不能撤销退款申请
//            return ServerResponseEntity.showFailMsg("买家已发货，不能撤销退款申请");
//        }
//        if (Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.RECEIVE.value())) {
//            // 卖家已收货，不能撤销退款申请
//            return ServerResponseEntity.showFailMsg("卖家已收货，不能撤销退款申请");
//        }
        if (Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.SUCCESS.value())) {
            return ServerResponseEntity.showFailMsg("退款已成功，不能撤销退款申请");
        }

        // 查看订单是否还有处于处理中的退款单，如果没有则修改订单退款状态为关闭状态
        Order order = orderService.getOrderByOrderIdAndUserId(orderRefundVO.getOrderId(), orderRefundVO.getUserId());

        //如果订单状态为待发货、包含运费、单个商品退款，且所有订单项都进行退款，则不能再取消退款（取消退款后再退款会导致重复退运费bug）
//        if (Objects.equals(order.getStatus(), OrderStatus.PAYED.value()) && Objects.equals(orderRefundVO.getRefundType(), RefundType.SINGLE.value())
//                && order.getFreightAmount() - order.getPlatformFreeFreightAmount() > 0) {
//            // 退款数量
//            int refundCount = orderRefundService.countByReturnMoneyStsAndOrderId(1, 6, order.getOrderId());
//            // 订单项数量
//            int orderItemCount = orderItemService.countByOrderId(order.getOrderId());
//            if (refundCount == orderItemCount) {
//                // 该订单所有商品都进行退款，已无法取消退款
//                return ServerResponseEntity.showFailMsg("该订单所有商品都进行退款，已无法取消退款");
//            }
//        }

        if (Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.SUCCESS.value()) ||
                Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.FAIL.value())) {
            // 撤销失败 当前状态不允许此操作
            return ServerResponseEntity.showFailMsg("撤销失败 当前状态不允许此操作");
        }
        orderRefundService.cancelRefund(orderRefundVO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> submitExpress(RefundDeliveryDTO refundDeliveryDTO) {
        com.mall4j.cloud.order.vo.OrderRefundVO orderRefundVO = orderRefundService.getByAftersaleId(refundDeliveryDTO.getAftersaleId());
//        com.mall4j.cloud.order.vo.OrderRefundVO orderRefundVO = orderRefundService.getByRefundNumber(refundDeliveryDTO.getRefundNo());
        if (Objects.isNull(orderRefundVO)) {
            // 查询不到退款信息
            Assert.faild("退款订单不存在");
        }
        refundDeliveryDTO.setRefundNo(orderRefundVO.getRefundNumber());


        if (!Objects.equals(orderRefundVO.getApplyType(), RefundApplyType.REFUND_AND_RETURNS.value())) {
            // 当前申请类型不允许提交物流信息操作
            Assert.faild("当前申请类型不允许提交物流信息操作");
        }
        if (!Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.PROCESSING.value())
                && !Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.CONSIGNMENT.value())) {
            // 当前状态不允许提交物流信息操作
            Assert.faild("当前状态不允许提交物流信息操作");
        }

        // 填写物流信息 申请退货的时候已经有退货信息了
        OrderRefundAddr orderRefundAddr = orderRefundAddrService.getByRefundId(orderRefundVO.getRefundId());
        orderRefundAddr.setDeliveryCompanyId(Long.valueOf(refundDeliveryDTO.getDeliveryCode()));
        orderRefundAddr.setDeliveryName(refundDeliveryDTO.getLogisticsName());
        orderRefundAddr.setDeliveryNo(refundDeliveryDTO.getLogisticNo());
        orderRefundAddr.setUpdateTime(new Date());

        OrderRefund orderRefund = mapperFacade.map(orderRefundVO, OrderRefund.class);
        orderRefundService.submitExpress(orderRefund, orderRefundAddr);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> modifyExpress(RefundDeliveryDTO refundDeliveryDTO) {
        com.mall4j.cloud.order.vo.OrderRefundVO orderRefundVO = orderRefundService.getByAftersaleId(refundDeliveryDTO.getAftersaleId());
        if (Objects.isNull(orderRefundVO)) {
            // 查询不到退款信息
            Assert.faild("退款订单不存在");
        }
        // 填写物流信息 申请退货的时候已经有退货信息了
        OrderRefundAddr orderRefundAddr = orderRefundAddrService.getByRefundId(orderRefundVO.getRefundId());
        orderRefundAddr.setDeliveryCompanyId(Long.valueOf(refundDeliveryDTO.getDeliveryCode()));
        orderRefundAddr.setDeliveryName(refundDeliveryDTO.getLogisticsName());
        orderRefundAddr.setDeliveryNo(refundDeliveryDTO.getLogisticNo());
        orderRefundAddr.setUpdateTime(new Date());

        OrderRefund orderRefund = mapperFacade.map(orderRefundVO, OrderRefund.class);
        orderRefundService.submitExpress(orderRefund, orderRefundAddr);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> returnSuccess(Long aftersaleId, String outAftersaleId) {
        com.mall4j.cloud.order.vo.OrderRefundVO orderRefundVO = orderRefundService.getByRefundNumber(outAftersaleId);
        if (Objects.isNull(orderRefundVO)) {
            orderRefundVO = orderRefundService.getByAftersaleId(aftersaleId);
            if(Objects.isNull(orderRefundVO)){
                // 查询不到退款信息
                Assert.faild("退款订单不存在");
            }
        }
        //TODO 判断是否存在退款结算记录，如果不存在退款结算记录，并且退款单的状态不为已完成。
        // 说明为视频号超时自动退款


        RefundNotifyBO refundNotifyBO = new RefundNotifyBO();
        refundNotifyBO.setRefundId(orderRefundVO.getRefundId());
        refundNotifyBO.setOrderId(orderRefundVO.getOrderId());
        orderRefundService.refundSuccess(refundNotifyBO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> complaintNotify(int event, Long complaintOrderId) {
        ComplaintDetailRequest complaintDetailRequest = new ComplaintDetailRequest();
        complaintDetailRequest.setComplaint_order_id(complaintOrderId);
        ServerResponseEntity<ComplaintDetailResponse> serverResponse = liveStoreClient.complaintDetail(complaintDetailRequest);
        if(serverResponse==null || serverResponse.isFail() || serverResponse.getData() ==null){
            Assert.faild(StrUtil.format("视频号查询纠纷单详情失败，纠纷单id：{}",complaintOrderId));
        }
        ComplaintDetailResponse complaintDetail = serverResponse.getData();
        //用户发起纠纷单 维护当前纠纷单编号到售后表中
        if(event==240001){
            orderRefundService.syncComplaintOrderId(complaintOrderId,complaintDetail.getOrder().getAfter_sale_order_id());
        }else{
            orderRefundService.syncComplaintOrderStatus(complaintOrderId);
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> channelsComplaintNotify(Long after_sale_order_id, Long complaintOrderId) {
        orderRefundService.syncComplaintOrderId(complaintOrderId,after_sale_order_id);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> offlineRefund(Long aftersaleId) {
        orderRefundService.offlineRefund(aftersaleId);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> offlineRefundSuccess(Long aftersaleId) {
        orderRefundService.offlineRefundSuccess(aftersaleId);
        return ServerResponseEntity.success();
    }

    @Override
    public void updateJointVentureRefundOrder(List<Long> refundIds, Integer jointVentureRefundStatus) {
        orderRefundService.updateJointVentureRefundOrder(refundIds,jointVentureRefundStatus);
    }
}
