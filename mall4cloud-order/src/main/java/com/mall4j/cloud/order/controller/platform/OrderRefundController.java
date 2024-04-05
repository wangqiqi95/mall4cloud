package com.mall4j.cloud.order.controller.platform;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.biz.dto.channels.request.EcuploadrefundcertificateRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetaftersaleorderResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.UploadCertificatesRequest;
import com.mall4j.cloud.api.biz.feign.ChannlesFeignClient;
import com.mall4j.cloud.api.biz.feign.LiveStoreClient;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.multishop.feign.ShopRefundAddrFeignClient;
import com.mall4j.cloud.api.multishop.vo.ShopRefundAddrVO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.payment.feign.RefundFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.OrderSource;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.order.vo.RefundOrderItemVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.order.constant.*;
import com.mall4j.cloud.order.dto.app.OrderRefundDeliveryDTO;
import com.mall4j.cloud.order.dto.app.OrderRefundPageDTO;
import com.mall4j.cloud.order.dto.multishop.BatchreturnAndRefundAuditDTO;
import com.mall4j.cloud.order.dto.multishop.OrderRefundDTO;
import com.mall4j.cloud.order.model.*;
import com.mall4j.cloud.order.service.*;
import com.mall4j.cloud.order.vo.OrderRefundAddrVO;
import com.mall4j.cloud.order.vo.OrderRefundExcelVO;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author cl
 */
@Slf4j
@RestController("platformOrderRefundController")
@RequestMapping("/p/order_refund")
public class OrderRefundController {

    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private OrderRefundAddrService orderRefundAddrService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private OnsMQTransactionTemplate orderRefundTemplate;
    @Autowired
    private OrderSettlementService orderSettlementService;
    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private RefundFeignClient refundFeignClient;
    @Autowired
    StoreFeignClient storeFeignClient;
    @Autowired
    SpuFeignClient spuFeignClient;
    @Autowired
    private OnsMQTemplate sendMaSubcriptMessageTemplate;
    @Autowired
    WxMaApiFeignClient wxMaApiFeignClient;
    @Autowired
    ShopRefundAddrFeignClient shopRefundAddrFeignClient;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    LiveStoreClient liveStoreClient;
    @Autowired
    ChannlesFeignClient channlesFeignClient;

//    public static void main(String[] args) {
//        Date paytime = DateUtil.parse("2021-08-27").toJdkDate();
//        System.out.println(DateUtil.offsetDay(paytime,355));
//        System.out.println(DateUtil.compare(new Date(),DateUtil.offsetDay(paytime,355))>=0);
//    }

    @PostMapping("/initiateFromBackground")
    @ApiOperation(value = "后台发起退款", notes = "后台发起退款")
    public ServerResponseEntity<Long> apply(@Valid @RequestBody com.mall4j.cloud.order.dto.app.OrderRefundDTO orderRefundParam) {
        // 获取订单信息
        Order order = orderService.getByOrderId(orderRefundParam.getOrderId());

        // 视频号3.0订单不允许后台发起售后
        if(StrUtil.isNotEmpty(order.getTraceId())){
            throw new LuckException("视频号订单不允许后台发起售后");
        }

        // 视频号4.0订单不允许后台发起售后
        if(order.getOrderSource()== OrderSource.CHANNELS.value()){
            throw new LuckException("视频号小店订单不允许后台发起售后");
        }

        if (Objects.equals(order.getDistributionStatus(), 3)
                || Objects.equals(order.getDevelopingStatus(), 3)) {
            throw new LuckException("佣金提现中的订单不允许后台发起售后");
        }

        // 商品是否已经发货
        boolean isDelivered = Objects.equals(order.getStatus(), OrderStatus.CONSIGNMENT.value()) || Objects.equals(order.getStatus(), OrderStatus.SUCCESS.value());

        ServerResponseEntity<Long> checkOrderStatusResponse = checkOrderStatus(orderRefundParam, order, isDelivered);
        if (!checkOrderStatusResponse.isSuccess()) {
            return checkOrderStatusResponse;
        }

        if(DateUtil.compare(new Date(),DateUtil.offsetDay(order.getPayTime(),355))>=0){
            Assert.faild("订单支付时间超过355天，后台不允许发起退款申请。");
        }

        // 生成退款单信息
        OrderRefund newOrderRefund = new OrderRefund();

        // 获取所有正在进行中的退款订单
        List<OrderRefund> orderRefunds = orderRefundService.getProcessOrderRefundByOrderId(order.getOrderId());

        for (OrderRefund orderRefund : orderRefunds) {
            if (Objects.equals(RefundType.ALL.value(), orderRefund.getRefundType())) {
                return ServerResponseEntity.showFailMsg("该订单正在进行整单退款，无法进行新的退款操作");
            }
        }

        // 单个商品退款，判断是否有剩余商品可退
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
        newOrderRefund.setApplySource(1);
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
        return ServerResponseEntity.success(newOrderRefund.getRefundId());
    }

    private ServerResponseEntity<Long> checkOrderStatus(com.mall4j.cloud.order.dto.app.OrderRefundDTO orderRefundParam, Order order, boolean isDelivered) {
        log.info("checkOrderStatus 。orderRefundParam:{},order:{},isDelivered:{}",JSONObject.toJSONString(orderRefundParam),JSONObject.toJSONString(order),isDelivered);
        //待收货或者确认收货 -> 整单退款 -> 退款金额 < 订单金额 - 运费金额
        //后台发起售后，不做运费判断，已发货也仍然可以退运费。
//        if (isDelivered && orderRefundParam.getRefundType().equals(RefundType.ALL.value())) {
//            Long refundAmount = order.getActualTotal() - order.getFreightAmount() - order.getPlatformFreeFreightAmount();
//            if (orderRefundParam.getRefundAmount() > refundAmount) {
//                // 退款金额已超出订单金额，无法申请
////                return ServerResponseEntity.showFailMsg("退款金额已超出订单金额");
//                Assert.faild("退款金额已超出订单金额");
//            }
//        }

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
            log.info("退款金额：{},订单支付金额：{}",orderRefundParam.getRefundAmount(),order.getActualTotal());
            return ServerResponseEntity.showFailMsg("退款金额已超出订单金额，无法申请");
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

    private ServerResponseEntity<Long> doItemRefund(com.mall4j.cloud.order.dto.app.OrderRefundDTO orderRefundParam, Order order, boolean isDelivered, OrderRefund newOrderRefund, List<OrderRefund> orderRefunds, List<OrderItem> orderItemList, int orderItemCount) {
        log.info("doItemRefund。orderRefundParam：{},order:{},isDelivered:{},newOrderRefund:{},orderRefunds:{},orderItemList:{},orderItemCount:{}"
                ,JSONObject.toJSONString(orderRefundParam),JSONObject.toJSONString(order),isDelivered,JSONObject.toJSONString(newOrderRefund),
                JSONObject.toJSONString(orderRefunds),JSONObject.toJSONString(orderItemList),orderItemCount);
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
//            return ServerResponseEntity.showFailMsg("该订单部分订单项支付金额和积分为0，无法使用部分退款");
            Assert.faild("该订单部分订单项支付金额和积分为0，无法使用部分退款");
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
//            return ServerResponseEntity.showFailMsg("退款物品数量已超出订单中的数量，不允许申请");
            Assert.faild("退款物品数量已超出订单中的数量，不允许申请");
        }

        // 判断退款金额是否超出订单金额3种情况
        double refundSingleAmount = Arith.div(orderRefundParam.getRefundAmount(), orderRefundParam.getRefundCount(), 3);
        double singleAmount = Arith.div(orderItemVO.getActualTotal(), orderItemVO.getCount(), 3);
        // 可以退款的实际金额应该为
        double productTotalAmount = orderItemVO.getSpuTotalAmount() + orderItemVO.getShopChangeFreeAmount();
        // 1.如果是此笔订单最后一件并且不为确认收货或待收货状态，则订单项加上运费进行判断。
        //后台发起收货，不判断是否发货，只要是最后一件商品发起售后，都可以选择退运费
//        if (isEndItem && !isDelivered) {
        if (isEndItem) {
            productTotalAmount = Arith.add(productTotalAmount, freightAmount);
            singleAmount = Arith.add(singleAmount, freightAmount);
        }
        // 2.如果不是直接跟订单项进行判断
        if (refundSingleAmount > productTotalAmount || refundSingleAmount > singleAmount) {
            // 退款金额已超出订单金额，无法申请
//            return ServerResponseEntity.showFailMsg("退款金额已超出订单金额，无法申请");
            log.info("refundSingleAmount:{},productTotalAmount:{},singleAmount:{}",refundSingleAmount,productTotalAmount,singleAmount);
            Assert.faild("退款金额已超出订单金额，无法申请");
        }
        // 3.当前退款金额  +  已申请退款金额 > 订单实际支付总额， 就不能退款了
        double refundAmount = orderRefundParam.getRefundAmount();
        // 已退款总金额
        double alreadyRefundTotal = orderRefunds.stream().mapToDouble(OrderRefund::getRefundAmount).sum();
        if (Arith.add(refundAmount, alreadyRefundTotal) > order.getActualTotal()) {
            // 退款金额已超出订单金额，无法申请
//            return ServerResponseEntity.showFailMsg("退款金额已超出订单金额，无法申请");
            log.info("refundAmount:{},alreadyRefundTotal:{},ActualTotal:{}",refundAmount,alreadyRefundTotal,order.getActualTotal());
            Assert.faild("退款金额已超出订单金额，无法申请");
        }
        // 一个订单项只能申请一次退款
        for (OrderRefund orderRefund : orderRefunds) {
            if (Objects.equals(orderRefund.getOrderId(), orderItemVO.getOrderItemId())) {
                // 退款订单项已处理，请勿重复申请
//                return ServerResponseEntity.showFailMsg("退款订单项已处理，请勿重复申请");
                Assert.faild("退款订单项已处理，请勿重复申请");
            }
        }
        return ServerResponseEntity.success();
    }

    /**
     * 查看我的退款订单列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "我的退款订单列表", notes = "我的退款订单列表，显示数量时候")
    public ServerResponseEntity<PageVO<OrderRefundVO>> list(PageDTO page, OrderRefundPageDTO orderRefundPageDTO) {
        PageVO<OrderRefundVO> pageList = orderRefundService.page(page, orderRefundPageDTO);
        return ServerResponseEntity.success(pageList);
    }

    /**
     * 通过id查询
     *
     * @param refundId id
     * @return 查询详细信息
     */
    @GetMapping("/info/{refundId}")
    public ServerResponseEntity<OrderRefundVO> getById(@PathVariable("refundId") Long refundId) {
        OrderRefundVO orderRefund = orderRefundService.getDetailByRefundId(refundId);
        if (Objects.equals(orderRefund.getApplyType(), RefundApplyType.REFUND_AND_RETURNS.value())&& orderRefund.getHandelTime() !=null) {
            OrderRefundAddr orderRefundAddr = orderRefundAddrService.getByRefundId(refundId);
            orderRefund.setOrderRefundAddr(mapperFacade.map(orderRefundAddr, OrderRefundAddrVO.class));
        }
        EsOrderBO order =orderService.getEsOrder(orderRefund.getOrderId());
        orderRefund.setOrderSource(order.getOrderSource());
        //视频号4。0订单获取微信端售后的详情
        if(order.getOrderSource()==OrderSource.CHANNELS.value()){
            ServerResponseEntity<EcGetaftersaleorderResponse> serverResponse = channlesFeignClient.getEcaftersale(orderRefund.getAftersaleId());
            orderRefund.setEcGetaftersaleorderResponse(serverResponse.getData());
        }

        return ServerResponseEntity.success(orderRefund);
    }



    /**
     * 进入这个方法，会出现两种情况：
     * 1. 仅退款，此时商家同意买家的退款申请，执行发放退款的操作
     * 2. 退货退款操作:
     *   2.1)退货退款的第一步，商家允许买家退款的申请，商家进行设置退货地址，不执行发放退款的操作
     *   2.2)退货退款的第二步，当商家收到货之后，同意买家退款，此时需要发放退款，但不会执行这个方法，执行的是下面这个方法
     *   @see com.mall4j.cloud.order.controller.multishop.OrderRefundController#returnMoney(OrderRefundDTO)
     *
     */
    @PutMapping("/return_and_refund_audit")
    public ServerResponseEntity<Void> returnAndRefundAudit(@RequestBody OrderRefundDTO orderRefundParam) {
        // 处理退款操作


        OrderRefundVO orderRefundVo = orderRefundService.getDetailByRefundId(orderRefundParam.getRefundId());
        if (!Objects.equals(ReturnProcessStatusEnum.APPLY.value(), orderRefundVo.getReturnMoneySts())) {
            // 订单退款状态已发生改变，请勿重复操作
            return ServerResponseEntity.showFailMsg("订单退款状态已发生改变，请勿重复操作");
        }
        orderRefundParam.setOutRefundId(orderRefundVo.getAftersaleId());
        EsOrderBO esOrderBO = orderService.getEsOrder(orderRefundVo.getOrderId());
        log.info("退款审核，查询当前订单信息。{}",JSONObject.toJSONString(esOrderBO));
        Assert.isNull(esOrderBO,"订单不存在，请检查数据再退款。");
        orderRefundParam.setOrderSource(esOrderBO.getOrderSource());

        /**
         * 判断是否需要处理退运费 非视频号订单才可以更新是否退运费，修改运费。
         */
        if((esOrderBO.getOrderSource()== OrderSource.NORMAL.value() || esOrderBO.getOrderSource()==OrderSource.LIVE.value()) &&  orderRefundParam.getReturnFreightFlag()>0){
            orderRefundService.returnFreightAmount(orderRefundParam, orderRefundVo);
        }

//        if (!Objects.equals(orderRefundVo.getShopId(), AuthUserContext.get().getTenantId())) {
//            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
//        }
        try {
            // 平台退单审核订阅消息通知
            List<String> businessIds = new ArrayList<>();
            businessIds.add(StrUtil.toString(orderRefundParam.getRefundId()));
            ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.CHARGE_BACK_EXCHANGE.getValue(),
                    businessIds);
            log.info("subscriptResp {}", JSONObject.toJSONString(subscriptResp.getData()));
            if (subscriptResp.isSuccess()) {
                WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
                List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();

                if (!org.springframework.util.CollectionUtils.isEmpty(userRecords)) {

                    List<WeixinMaSubscriptTmessageSendVO> notifyList = userRecords.stream().map(u -> {
                        /**
                         *  1: 要跳转的 地址
                         *  pages/order-detail/order-detail?orderId=
                         *  需要拼接订单编号参数
                         */
//                        String page =StrUtil.isEmpty(subscriptResp.getData().getPage())?"":subscriptResp.getData().getPage()+orderNo;
                        String page = "packageRefund/pages/refund-detail/refund-detail?refundId="+orderRefundParam.getRefundId();
                        String spuName = orderRefundVo.getOrderItems().get(0).getSpuName();
                        String auditResult = Objects.equals(orderRefundParam.getRefundSts(), RefundStsType.DISAGREE.value())==true?"拒绝":"同意";

                        /**
                         * 值替换
                         * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                         * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                         * 当前微客 场景下{orderId} 订单编号、{amount} 退款金额、  {spuName} 商品名称 {refundId} 退款单编号 {auditResult} 审核结果(通过/拒绝) {auditTime} 审核时间
                         * 构建参数map.
                         */
                        Map<String,String> paramMap = new HashMap();
                        paramMap.put("{orderId}",StrUtil.toString(orderRefundVo.getOrderNumber()));
                        paramMap.put("{amount}", StrUtil.toString(new BigDecimal(orderRefundVo.getRefundAmount()).divide(new BigDecimal(100).setScale(2))));
                        paramMap.put("{spuName}",spuName);
                        paramMap.put("{refundId}",orderRefundVo.getRefundNumber());
                        paramMap.put("{auditResult}",auditResult);
                        paramMap.put("{auditTime}", DateUtil.now());  //取当前时间作为审核时间

                        //构建msgdata参数
                        List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                            WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                            msgData.setName(t.getTemplateValueName());
                            msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue()))?t.getTemplateValue():paramMap.get(t.getTemplateValue()));
                            return msgData;
                        }).collect(Collectors.toList());

                        WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                        sendVO.setUserSubscriptRecordId(u.getId());
                        sendVO.setData(msgDataList);
                        sendVO.setPage(page);
                        return sendVO;
                    }).collect(Collectors.toList());
                    log.info("退单审核通知发送订阅消息，构建参数对象 {}", JSONObject.toJSONString(notifyList));
                    sendMaSubcriptMessageTemplate.syncSend(notifyList);
                }
            }
        } catch (Exception e) {
            log.error("退单审核通知订阅通知发送异常",e);
        }

        // 拒绝退款，可以不需要分布式事务啥的，因为就单纯拒绝，发个通知给用户，做幂等处理就好了
        if (Objects.equals(orderRefundParam.getRefundSts(), RefundStsType.DISAGREE.value())) {
            orderRefundService.disagreeRefund(orderRefundParam, orderRefundVo);
            return ServerResponseEntity.success();
        }


        // 同意退货，可以不需要分布式事务啥的，因为就单纯拒绝，发个通知给用户，做幂等处理就好了
        if (Objects.equals(orderRefundVo.getApplyType(), RefundApplyType.REFUND_AND_RETURNS.value())) {
            orderRefundService.agreeReturns(orderRefundParam, orderRefundVo);
            return ServerResponseEntity.success();
        }
        orderRefundService.pushRefund(orderRefundParam.getRefundId(), 2);

        // 同意退款，上面只是同意退货，关系到钱要看下面的
        return agreeRefund(orderRefundParam,orderRefundVo);
    }

    /**
     * 批量同意退款信息
     * @param orderRefundParam
     * @return
     */
    @PutMapping("/batch/return_and_refund_audit")
    public ServerResponseEntity<Void> batchreturnAndRefundAudit(@RequestBody BatchreturnAndRefundAuditDTO orderRefundParam) {
        if(CollUtil.isEmpty(orderRefundParam.getRefundIds())){
            Assert.faild("批量操作的退单信息为空，请勾选要操作的退单信息。");
        }

        ServerResponseEntity<ShopRefundAddrVO> refundAddrResponse = shopRefundAddrFeignClient.getMainRefundAddr();
        if(refundAddrResponse==null || refundAddrResponse.isFail() || refundAddrResponse.getData()==null){
            Assert.faild("获取默认收货地址失败，请稍后重试。");
        }

        for (Long refundId : orderRefundParam.getRefundIds()) {
            OrderRefundDTO orderRefundDTO = new OrderRefundDTO();
            orderRefundDTO.setRefundId(refundId);
            orderRefundDTO.setRefundSts(2);
            orderRefundDTO.setShopRefundAddrId(refundAddrResponse.getData().getShopRefundAddrId());
//            log.info("执行批量操作，组装参数信息:{}",JSONObject.toJSONString(orderRefundDTO));
            returnAndRefundAudit(orderRefundDTO);
        }


        return ServerResponseEntity.success();
    }



    /**
     * 退货退款的第二步，当商家收到货之后，同意买家退款，此时需要发放退款
     */
    @PutMapping("/return_money")
    public ServerResponseEntity<Void> returnMoney(@Valid @RequestBody OrderRefundDTO orderRefundParam) {

        // 获取退款单信息
        OrderRefundVO orderRefundVo = orderRefundService.getDetailByRefundId(orderRefundParam.getRefundId());
        if (!Objects.equals(ReturnProcessStatusEnum.CONSIGNMENT.value(), orderRefundVo.getReturnMoneySts())) {
            throw new LuckException("订单退款状态已发生改变，请勿重复操作");
        }
//        if (!Objects.equals(orderRefundVo.getShopId(), AuthUserContext.get().getTenantId())) {
//            throw new LuckException(ResponseEnum.UNAUTHORIZED);
//        }
        // 拒绝退款，可以不需要分布式事务啥的，因为就单纯拒绝，发个通知给用户，做幂等处理就好了
        if (Objects.equals(orderRefundParam.getRefundSts(), RefundStsType.DISAGREE.value())) {
            orderRefundParam.setOutRefundId(orderRefundVo.getAftersaleId());
            orderRefundService.disagreeRefund(orderRefundParam, orderRefundVo);
            return ServerResponseEntity.success();
        }
        //todo 如果有传入退单单号，快递公司，更新记录的退单信息
        if(orderRefundParam.getRefundAddrId()!=null){
            OrderRefundAddr orderRefundAddr = orderRefundAddrService.getByRefundAddrId(orderRefundParam.getRefundAddrId());
            orderRefundAddr.setDeliveryCompanyId(orderRefundParam.getDeliveryCompanyId());
            orderRefundAddr.setDeliveryName(orderRefundParam.getDeliveryName());
            orderRefundAddr.setDeliveryNo(orderRefundParam.getDeliveryNo());
            orderRefundAddrService.update(orderRefundAddr);
        }

        /**
         *
         * 当前运费退费计算逻辑：（前端计算）
         * 只要订单已发货，退单的时候就不退运费了。
         * 未发货，单件商品退款的时候，最后一个商品申请退款的时候会计算运费一起退掉。
         * 未发货，整单退款，合计运费一起退。
         *
         * todo 如果勾选了退运费，
         *  判断当前订单是否已经退了运费？
         *  没有退再判断当前退单记录的退单金额是否已经包含了运费？
         *      如果已经包含了运费：
         *      1，修改当前订单的退运费标识为已退运费
         *      2，修改当前退单的退运费标识为已退运费
         *  都没有的情况。
         *  1，更新订单的运费退款标识。修改退单的退运费标识为已退运费。
         *  2，修改当前退单的退款金额。增加运费
         *  3，修改orderRefundVo参数的退款金额，增加运费。
         *
         * todo
         *  如果勾选了不退运费，
         *  判断当前退单金额是否已经包含了运费。
         *      如果已经包含了运费金额，
         *      1，修改当前退单的退款金额减去运费。
         *      不包含：啥也不干
         *
         *
         */
//        if(orderRefundParam.getReturnFreightAmount()==1){
//
//        }



        return agreeRefund(orderRefundParam,orderRefundVo);
    }


    private ServerResponseEntity<Void> agreeRefund(OrderRefundDTO orderRefundParam, OrderRefundVO orderRefundVo) {

        OrderSettlement orderSettlement = orderSettlementService.getByOrderId(orderRefundVo.getOrderId());
        if (orderSettlement == null) {
            return ServerResponseEntity.showFailMsg("没有查询到支付记录，无法申请退款");
        }



        orderRefundVo.setPayId(orderSettlement.getPayId());
        orderRefundVo.setPayType(orderSettlement.getPayType());
        SendResult sendResult = orderRefundTemplate.sendMessageInTransaction(orderRefundVo, orderRefundParam);

        if (sendResult == null || sendResult.getMessageId() == null) {
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
        return ServerResponseEntity.success();
    }

    /**
     * 修改订单的平台备注
     */
    @PutMapping("/{orderId}/platformRemark")
    @ApiOperation(value = "修改订单的平台备注", notes = "修改订单的平台备注")
    public ServerResponseEntity<Void> platformRemark(@PathVariable("refundId") Long refundId,@RequestParam("remark")String remark) {
        orderRefundService.editPlatformRemark(refundId,remark);
        return ServerResponseEntity.success();
    }

    /**
     * 后台提交物流公司信息
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
        if (Objects.nonNull(esOrder.getTraceId())){
            liveStoreClient.aftersaleUploadLogistics(orderRefundExpressParam.getRefundId());
        }
        return ServerResponseEntity.success();
    }


    @PostMapping("/uploadCertificates")
    @ApiOperation(value = "视频号订单 线下退款 商家上传退款凭证", notes = "视频号订单 线下退款 商家上传退款凭证")
    public ServerResponseEntity<Void> uploadCertificates(@Valid @RequestBody UploadCertificatesRequest uploadCertificatesRequest) {
        orderRefundService.uploadCertificates(uploadCertificatesRequest);
        return ServerResponseEntity.success();
    }

    @PostMapping("/ua/uploadCertificates")
    @ApiOperation(value = "测试视频号订单 线下退款 商家上传退款凭证", notes = "视频号订单 线下退款 商家上传退款凭证")
    public ServerResponseEntity<Void> testuploadCertificates(@Valid @RequestBody UploadCertificatesRequest uploadCertificatesRequest) {
        orderRefundService.uploadCertificates(uploadCertificatesRequest);
        return ServerResponseEntity.success();
    }

    @PostMapping("/channels/uploadrefundcertificate")
    @ApiOperation(value = "视频号4.0订单 商家上传退款凭证", notes = "视频号4.0订单 商家上传退款凭证")
    public ServerResponseEntity<Void> uploadrefundcertificate(@Valid @RequestBody EcuploadrefundcertificateRequest request) {
        orderRefundService.uploadrefundcertificate(request);
        return ServerResponseEntity.success();
    }

    @GetMapping("/sold_excel")
    @ApiOperation(value = "导出excel", notes = "导出订单excel")
    public void orderSoldExcel(HttpServletResponse response, OrderRefundPageDTO orderRefundPageDTO) throws IOException {
//        Assert.isNull(orderRefundPageDTO.getBeginTime(),"退款申请开始时间不能为空");
//        Assert.isNull(orderRefundPageDTO.getEndTime(),"退款申请结束时间不能为空");

//        if (orderRefundPageDTO.getBeginTime()==null && orderRefundPageDTO.getRefundBeginTime()==null){
//            Assert.faild("退款申请或者退款成功时间不允许都为空。");
//        }
//        if(orderRefundPageDTO.getBeginTime()!=null){
//            if(DateUtil.betweenDay(orderRefundPageDTO.getBeginTime(),orderRefundPageDTO.getEndTime(),false)>32){
//                Assert.faild("导出退款申请日期条件不允许选择超过31天。");
//            }
//        }
//        if(orderRefundPageDTO.getRefundBeginTime()!=null){
//            if(DateUtil.betweenDay(orderRefundPageDTO.getRefundBeginTime(),orderRefundPageDTO.getRefundEndTime(),false)>32){
//                Assert.faild("导出退款成功日期条件不允许选择超过31天。");
//            }
//        }

        //直接调用列表接口调用的的查询
        PageDTO page = new PageDTO();
        page.setPageNum(1);
        page.setPageSize(50000,true);
        PageVO<OrderRefundVO> pageList = orderRefundService.page(page, orderRefundPageDTO);
        if(pageList ==null ||pageList.getList().size() ==0){
            Assert.faild("当前没有符合条件的数据可以导出。");
        }

        List<OrderRefundExcelVO> orderRefundVOList =renderExportData(pageList.getList());
//        ExcelUtil.soleExcel(response, orderRefundVOList, OrderRefundExcelVO.EXCEL_NAME, OrderRefundExcelVO.MERGE_ROW_INDEX,  OrderRefundExcelVO.class);
        try{
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
//            response.setContentType("application/vnd.ms-excel");
//            response.setCharacterEncoding("utf-8");
//            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//            String fileName = URLEncoder.encode("111", "UTF-8");
//            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//            ExcelWriter excelWriter  = EasyExcel.write(response.getOutputStream()).build();
//            WriteSheet sheetWriter = EasyExcel.writerSheet("sheet").head(OrderRefundExcelVO.class).build();
//            excelWriter.write(list,sheetWriter);

//            EasyExcel.write(response.getOutputStream(), OrderRefundExcelVO.class).autoCloseStream(Boolean.FALSE).sheet("1111")
//                    .doWrite(orderRefundVOList);
//        return ServerResponseEntity.success();

            //导出
            ExcelUtil.soleExcel(response, orderRefundVOList,
                    OrderRefundExcelVO.EXCEL_NAME,
                    OrderRefundExcelVO.MERGE_ROW_INDEX,
                    OrderRefundExcelVO.MERGE_COLUMN_INDEX,
                    OrderRefundExcelVO.class);
        }catch (Exception e){
            e.printStackTrace();
            Assert.faild("导出失败");
        }

//        try{
//            ServletOutputStream out = response.getOutputStream();
//            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
//            String fileName = "测试exportExcel";
//            Sheet sheet = new Sheet(1, 0,OrderRefundExcelVO.class);
//            //设置自适应宽度
//            sheet.setAutoWidth(Boolean.TRUE);
//            // 第一个 sheet 名称
//            sheet.setSheetName("第一个sheet");˙
//            writer.write(orderRefundVOList, sheet);
//            //通知浏览器以附件的形式下载处理，设置返回头要注意文件名有中文
//            response.setHeader("Content-disposition", "attachment;filename=" + new String( fileName.getBytes("gb2312"), "ISO8859-1" ) + ".xlsx");
//            writer.finish();
//            response.setContentType("multipart/form-data");
//            response.setCharacterEncoding("utf-8");
//            out.flush();
//        }catch (Exception e){
//
//        }
    }

    private List<OrderRefundExcelVO> renderExportData(List<OrderRefundVO> orderRefundVOList){
        List<OrderRefundExcelVO> list = new ArrayList<>();
        Map<String,String> freightAmountMap = new HashMap<>();

        /**
         * 查询店铺列表
         */

        for (OrderRefundVO refundVO : orderRefundVOList) {

            /**
             * sku
             */
            List<Long> skuList = refundVO.getOrderItems().stream().map(RefundOrderItemVO::getSkuId).distinct().collect(Collectors.toList());
            ServerResponseEntity<List<SkuVO>> skuVOResponse = skuFeignClient.listSkuCodeByIds(skuList);
            Map<Long,SkuVO> skuMaps = new HashMap<>();
            if(skuVOResponse!=null && skuVOResponse.isSuccess() && skuVOResponse.getData().size()>0){
                skuMaps = skuVOResponse.getData().stream().collect(Collectors.toMap(SkuVO::getSkuId , p->p));
            }

            /**
             * sku
             */
            List<Long> spuList = refundVO.getOrderItems().stream().map(RefundOrderItemVO::getSpuId).distinct().collect(Collectors.toList());
            ServerResponseEntity<List<SpuVO>> spuVOResponse = spuFeignClient.listSpuNameBypBySpuIds(spuList);
            Map<Long, SpuVO> spuMaps = new HashMap<>();
            if(spuVOResponse!=null && spuVOResponse.isSuccess() && spuVOResponse.getData().size()>0){
                spuMaps = spuVOResponse.getData().stream().collect(Collectors.toMap(SpuVO::getSpuId , p->p));
            }

            /**
             * 代客下单人
             */
            Map<Long, StaffVO> valetOrderMaps = new HashMap<>();
            if (refundVO.getBuyStaffId() != null && refundVO.getBuyStaffId() > 0) {
                ServerResponseEntity<List<StaffVO>> valetOrderResponse = staffFeignClient.getStaffByIds(Collections.singletonList(refundVO.getBuyStaffId()));

                if (valetOrderResponse != null && valetOrderResponse.isSuccess() && valetOrderResponse.getData().size() > 0) {
                    valetOrderMaps = valetOrderResponse.getData().stream().collect(Collectors.toMap(StaffVO::getId, p -> p));
                }
            }

            //根据退款商品记录拆单。
            for (RefundOrderItemVO orderItem : refundVO.getOrderItems()) {
                OrderRefundExcelVO excelVO = new OrderRefundExcelVO();
                BeanUtil.copyProperties(refundVO,excelVO);
                excelVO.setFreifhtAmount(orderItem.getFreightAmount());
//                excelVO.setAftersaleId(StrUtil.toString(refundVO.getAftersaleId()));
                excelVO.setWechatOrderId(refundVO.getWechatOrderId());

                String applyTypeName = refundVO.getApplyType()==1?"仅退款":"退款退货";
                excelVO.setApplyTypeName(applyTypeName);
                String refundTypeName = refundVO.getRefundType()==1?"整单退款":"单个物品退款";
                excelVO.setRefundTypeName(refundTypeName);

                /**
                 * 处理退款状态:(1.买家申请 2.卖家接受 3.买家发货 4.卖家收货 5.退款成功  -1.退款关闭)详情见ReturnMoneyStsType
                 */
                String statusName = "";
                if(refundVO.getReturnMoneySts()==1){
                    statusName = "买家申请";
                }else if(refundVO.getReturnMoneySts()==2){
                    statusName = "卖家接受";
                }else if(refundVO.getReturnMoneySts()==3){
                    statusName = "买家发货";
                }else if(refundVO.getReturnMoneySts()==4){
                    statusName = "卖家收货";
                }else if(refundVO.getReturnMoneySts()==5){
                    statusName = "退款成功";
                }else{
                    statusName = "退款关闭";
                }

                // 判断计算出【需售后处理时间】
                try {
                    if(refundVO.getOrderSource().equals(RefundOrderSourceEnum.VIDEO_THREE.value()) || refundVO.getOrderSource().equals(RefundOrderSourceEnum.VIDEO_FOUR.value())){
                        Date now = new Date();
                        String tempDate = null;
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if(refundVO.getReturnMoneySts().equals(ReturnProcessStatusEnum.APPLY.value())){
                            calendar.setTime(refundVO.getCreateTime());
                            calendar.add(Calendar.DAY_OF_MONTH, +2);
                            tempDate = format.format(calendar.getTime());
                        }
                        if(refundVO.getApplyType().equals(RefundApplyType.REFUND_AND_RETURNS.value()) && refundVO.getReturnMoneySts().equals(ReturnProcessStatusEnum.PROCESSING.value())){
                            calendar.setTime(refundVO.getHandelTime());
                            calendar.add(Calendar.DAY_OF_MONTH, +7);
                            tempDate = format.format(calendar.getTime());
                        }
                        if(refundVO.getApplyType().equals(RefundApplyType.REFUND_AND_RETURNS.value()) && refundVO.getReturnMoneySts().equals(ReturnProcessStatusEnum.CONSIGNMENT.value())){
                            calendar.setTime(refundVO.getDeliveryTime());
                            calendar.add(Calendar.DAY_OF_MONTH, +10);
                            tempDate = format.format(calendar.getTime());
                        }
                        if(Objects.nonNull(tempDate) && format.parse(tempDate).after(now)){
                            excelVO.setAfterSalesTime(tempDate);
                        }
                    }
                } catch (ParseException e) {
                    log.error("判断计算出【需售后处理时间】出现异常：", e);
                }

                //代客下单人
                String valetOrderStaffName = "";
                String valetOrderStaffPhone = "";
                if (refundVO.getBuyStaffId() != null && refundVO.getBuyStaffId() > 0) {
                    StaffVO valetOrderStaffVO = valetOrderMaps.get(refundVO.getBuyStaffId());
                    if (valetOrderStaffVO != null) {
                        valetOrderStaffName = valetOrderStaffVO.getStaffName();
                        valetOrderStaffPhone = valetOrderStaffVO.getMobile();
                    }
                }
                excelVO.setValetOrderStaffName(valetOrderStaffName);
                excelVO.setValetOrderStaffPhone(valetOrderStaffPhone);

                excelVO.setStatusName(statusName);
                excelVO.setStoreCode(refundVO.getShopCode());
                excelVO.setStoreName(refundVO.getShopName());

//                excelVO.setRefundAmount(StrUtil.toString(Double.valueOf(excelVO.getRefundAmount())/100));
                excelVO.setRefundAmount(StrUtil.toString((double)orderItem.getActualTotal()/100));
                excelVO.setActualTotal(StrUtil.toString(Double.valueOf(excelVO.getActualTotal())/100));

                excelVO.setPlatformRemarks(refundVO.getOrderPlatformRemarks());
                excelVO.setDeliveryCreateTime(refundVO.getDeliveryCreateTime());

                RefundOrderItemVO orderItemVO =refundVO.getOrderItems().get(0);
                excelVO.setSpuName(orderItem.getSpuName());
                excelVO.setPrice(StrUtil.toString((double)orderItem.getPrice()/100));
                int refundCount = refundVO.getRefundType()==1?orderItem.getCount():refundVO.getRefundCount();
                excelVO.setCount(refundCount);
                excelVO.setSkuCode("");
                if(skuMaps.get(orderItem.getSkuId())!=null){
                    excelVO.setSkuCode(skuMaps.get(orderItem.getSkuId()).getPriceCode());
                    excelVO.setBarcode(skuMaps.get(orderItem.getSkuId()).getSkuCode());
                }
                if(spuMaps.get(orderItem.getSpuId())!=null){
                    excelVO.setSpuCode(spuMaps.get(orderItem.getSpuId()).getSpuCode());
                }
                if(refundVO.getRefundAmount()==null){
                    log.info("refundAmount is null :{}",JSONObject.toJSONString(refundVO));
                }
                excelVO.setTotalPrice(StrUtil.toString((double)refundVO.getRefundAmount()/100));
                list.add(excelVO);
                //订单运费金额大于0，并且还没有生成运费记录，
//                log.info("退单导出。refundVO对象:{},orderItem对象:{}", JSONObject.toJSONString(refundVO),JSONObject.toJSONString(orderItem));
                if(orderItem.getFreightAmount()>0 && !freightAmountMap.containsKey(refundVO.getOrderNumber())){
                    //退款金额等于订单总支付金额，创建运费退单记录 ,整单退款
                    if(refundVO.getActualTotal().longValue()==refundVO.getRefundAmount().longValue() && refundVO.getRefundType() == 1){
                        //
                        log.info("退款金额等于订单总支付金额，创建运费退单记录。refundVO对象:{},orderItem对象:{}", JSONObject.toJSONString(refundVO),JSONObject.toJSONString(orderItem));
                        list.add(createFreifhtAmountRecord(excelVO));
                        freightAmountMap.put(refundVO.getOrderNumber(),"1");
                    }
                    //退款金额大于商品支付金额且 差额等于运费金额时。创建运费退单记录 单个退款
                    if(refundVO.getRefundAmount().longValue() > orderItem.getSpuTotalAmount().longValue() &&
                            (refundVO.getRefundAmount().longValue() - orderItem.getSpuTotalAmount().longValue()) == orderItem.getFreightAmount().longValue()
                            && refundVO.getRefundType() == 2){
                        log.info("退款金额大于商品支付金额且 差额等于运费金额时。创建运费退单记录。refundVO对象:{},orderItem对象:{}", JSONObject.toJSONString(refundVO),JSONObject.toJSONString(orderItem));
                        list.add(createFreifhtAmountRecord(excelVO));
                        freightAmountMap.put(refundVO.getOrderNumber(),"1");
                    }
                }
            }
        }
        return list;
    }

    private OrderRefundExcelVO createFreifhtAmountRecord(OrderRefundExcelVO excelVO){
        OrderRefundExcelVO freifhtAmountRecord = new OrderRefundExcelVO();
        BeanUtil.copyProperties(excelVO,freifhtAmountRecord);
        freifhtAmountRecord.setPrice(StrUtil.toString((double)excelVO.getFreifhtAmount()/100));
        freifhtAmountRecord.setRefundAmount(StrUtil.toString((double)excelVO.getFreifhtAmount()/100));
        freifhtAmountRecord.setSpuName("运费");
        freifhtAmountRecord.setSpuCode("运费");
        freifhtAmountRecord.setSkuCode("运费");
        freifhtAmountRecord.setBarcode("运费");
        return freifhtAmountRecord;
    }

}
