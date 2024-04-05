package com.mall4j.cloud.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.SendResult;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.mall4j.cloud.api.biz.dto.channels.request.EcuploadrefundcertificateRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.UploadCertificatesRequest;
import com.mall4j.cloud.api.biz.feign.ChannlesFeignClient;
import com.mall4j.cloud.api.biz.feign.LiveStoreClient;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.ChannelsAddressVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.distribution.constant.CommissionChangeTypeEnum;
import com.mall4j.cloud.api.distribution.feign.DistributionFeignClient;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushRefundDto;
import com.mall4j.cloud.api.docking.skq_erp.feign.StdOrderFeignClient;
import com.mall4j.cloud.api.group.feign.GroupFeignClient;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftReduceAppDTO;
import com.mall4j.cloud.api.multishop.bo.OrderChangeShopWalletAmountBO;
import com.mall4j.cloud.api.multishop.feign.ShopRefundAddrFeignClient;
import com.mall4j.cloud.api.multishop.vo.ShopRefundAddrVO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.bo.EsOrderItemBO;
import com.mall4j.cloud.api.order.bo.SendNotifyBO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.vo.OrderRefundAddrVO;
import com.mall4j.cloud.api.order.vo.OrderRefundProdEffectRespVO;
import com.mall4j.cloud.api.order.vo.OrderRefundSimpleVO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.vo.SkuCodeVO;
import com.mall4j.cloud.api.user.bo.UserOrderScoreBo;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.RefundNotifyBO;
import com.mall4j.cloud.common.order.bo.RefundReductionStockBO;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.constant.OrderCloseType;
import com.mall4j.cloud.common.order.constant.OrderSource;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.order.vo.RefundOrderItemVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.order.constant.*;
import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;
import com.mall4j.cloud.order.dto.app.OrderRefundPageDTO;
import com.mall4j.cloud.order.dto.multishop.OrderRefundDTO;
import com.mall4j.cloud.order.mapper.OrderRefundMapper;
import com.mall4j.cloud.order.mapper.OrderRefundOfflineCertificatesMapper;
import com.mall4j.cloud.order.mapper.OrderRefundSettlementMapper;
import com.mall4j.cloud.order.model.*;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.service.OrderRefundAddrService;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import com.mall4j.cloud.order.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 订单退款记录信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
@Slf4j
@Service
public class OrderRefundServiceImpl implements OrderRefundService {

    private static final Logger logger = LoggerFactory.getLogger(OrderRefundServiceImpl.class);

    @Autowired
    private OrderRefundMapper orderRefundMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderRefundAddrService orderRefundAddrService;
    @Autowired
    private ShopRefundAddrFeignClient shopRefundAddrFeignClient;
    @Autowired
    private OrderRefundSettlementMapper orderRefundSettlementMapper;
    @Autowired
    private OnsMQTemplate orderRefundSuccessCouponTemplate;
    @Autowired
    private OnsMQTemplate orderRefundSuccessGrowthTemplate;
    @Autowired
    private OnsMQTemplate orderRefundSuccessSettlementTemplate;
    @Autowired
    private OnsMQTemplate orderRefundSuccessStockTemplate;
    @Autowired
    private OnsMQTemplate sendNotifyToShopTemplate;
    @Autowired
    private OnsMQTemplate sendNotifyToUserTemplate;
    @Autowired
    private OnsMQTemplate orderRefundSuccessScoreTemplate;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StdOrderFeignClient stdOrderFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    OnsMQTemplate stdOrderRefundTemplate;
    @Autowired
    LiveStoreClient liveStoreClient;
    @Autowired
    OrderFeignClient orderFeignClient;
    @Autowired
    GroupFeignClient groupFeignClient;
    @Autowired
    WxMaApiFeignClient wxMaApiFeignClient;
    @Autowired
    private OnsMQTemplate sendMaSubcriptMessageTemplate;
    @Autowired
    private DistributionFeignClient distributionFeignClient;
    @Autowired
    OrderRefundOfflineCertificatesMapper orderRefundOfflineCertificatesMapper;
    @Autowired
    ChannlesFeignClient channlesFeignClient;


    @Override
    public PageVO<OrderRefundVO> page(PageDTO pageDTO, OrderRefundPageDTO orderRefundPageDTO) {
        PageVO<OrderRefundVO> pageVO = new PageVO<>();
        Long refundCount = orderRefundMapper.count(orderRefundPageDTO);
        pageVO.setTotal(refundCount);
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        // 没有退款的订单返回空
        if (Objects.equals(refundCount, 0L)) {
            pageVO.setList(Collections.emptyList());
            return pageVO;
        }

        List<OrderRefundVO> orderRefundList = orderRefundMapper.list(orderRefundPageDTO, new PageAdapter(pageDTO));

        List<Long> storeidList = orderRefundList.stream().filter(s -> s != null).map(OrderRefundVO::getShopId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StoreVO>> storesResponse = storeFeignClient.listBypByStoreIdList(storeidList);
        Map<Long, StoreVO> storeMaps = new HashMap<>();
        if (storesResponse != null && storesResponse.isSuccess() && storesResponse.getData().size() > 0) {
            storeMaps = storesResponse.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId, p -> p));
        }
        for (OrderRefundVO orderRefundVO : orderRefundList) {
            List<RefundOrderItemVO> orderItems = orderRefundVO.getOrderItems();
            // 不是整单退款
            if (!Objects.equals(orderRefundVO.getRefundType(), RefundType.ALL.value())) {
                orderItems.removeIf(next -> !Objects.equals(orderRefundVO.getOrderItemId(), next.getOrderItemId()));
            }
//            OrderLangUtil.orderRefundVOList(orderItems);
            orderRefundVO.setBuyerReasonValue(BuyerReasonType.instance(orderRefundVO.getBuyerReason()).getCn());
            StoreVO storeVO = storeMaps.get(orderRefundVO.getShopId());
            if (storeVO != null) {
                orderRefundVO.setShopName(storeVO.getName());
                orderRefundVO.setShopCode(storeVO.getStoreCode());
            }
        }
        pageVO.setList(orderRefundList);
        return pageVO;
    }

    @Override
    public PageVO<OrderRefundVO> excelPage(PageDTO pageDTO, OrderRefundPageDTO orderRefundPageDTO) {
        PageVO<OrderRefundVO> pageVO = new PageVO<>();
        Long refundCount = orderRefundMapper.count(orderRefundPageDTO);
        pageVO.setTotal(refundCount);
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        // 没有退款的订单返回空
        if (Objects.equals(refundCount, 0L)) {
            pageVO.setList(Collections.emptyList());
            return pageVO;
        }

        List<OrderRefundVO> orderRefundList = orderRefundMapper.list(orderRefundPageDTO, new PageAdapter(pageDTO));

        List<Long> storeidList = orderRefundList.stream().filter(s -> s != null).map(OrderRefundVO::getShopId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StoreVO>> storesResponse = storeFeignClient.listByStoreIdList(storeidList);
        Map<Long, StoreVO> storeMaps = new HashMap<>();
        if (storesResponse != null && storesResponse.isSuccess() && storesResponse.getData().size() > 0) {
            storeMaps = storesResponse.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId, p -> p));
        }
        for (OrderRefundVO orderRefundVO : orderRefundList) {
//            OrderLangUtil.orderRefundVOList(orderItems);
            orderRefundVO.setBuyerReasonValue(BuyerReasonType.instance(orderRefundVO.getBuyerReason()).getCn());
            StoreVO storeVO = storeMaps.get(orderRefundVO.getShopId());
            if (storeVO != null) {
                orderRefundVO.setShopName(storeVO.getName());
                orderRefundVO.setShopCode(storeVO.getStoreCode());
            }
        }
        pageVO.setList(orderRefundList);
        return pageVO;
    }

    @Override
    public OrderRefundVO getByRefundId(Long refundId) {
        return orderRefundMapper.getByRefundId(refundId);
    }

    @Override
    public OrderRefundVO getByRefundNumber(String refundNumber) {
        return orderRefundMapper.getByRefundNumber(refundNumber);
    }

    @Override
    public OrderRefundVO getByAftersaleId(Long AftersaleId) {
        return orderRefundMapper.getByAftersaleId(AftersaleId);
    }

    @Override
    public List<OrderRefundVO> getByAftersaleIds(List<Long> AftersaleId) {
        return orderRefundMapper.getByAftersaleIds(AftersaleId);
    }

    @Override
    public Integer getCountByOrderItemId(Long orderItemId) {
        return orderRefundMapper.getCountByOrderItemId(orderItemId);
    }

    @Override
    public OrderRefundVO getDetailByRefundId(Long refundId) {
        OrderRefundVO orderRefundVO = orderRefundMapper.getDetailByRefundId(refundId);
        orderRefundVO.setBuyerReasonValue(BuyerReasonType.instance(orderRefundVO.getBuyerReason()).getCn());
        List<RefundOrderItemVO> orderItems = orderRefundVO.getOrderItems();
        // 不是整单退款
        if (!Objects.equals(orderRefundVO.getRefundType(), RefundType.ALL.value())) {
            orderItems.removeIf(next -> !Objects.equals(orderRefundVO.getOrderItemId(), next.getOrderItemId()));
        }
        OrderRefundAddr orderRefundAddr = orderRefundAddrService.getByRefundId(refundId);
        com.mall4j.cloud.order.vo.OrderRefundAddrVO orderRefundAddrVO = new com.mall4j.cloud.order.vo.OrderRefundAddrVO();
        BeanUtil.copyProperties(orderRefundAddr,orderRefundAddrVO);
        orderRefundVO.setOrderRefundAddr(orderRefundAddrVO);
//        OrderLangUtil.orderRefundVOList(orderRefundVO.getOrderItems());
        return orderRefundVO;
    }

    @Override
    public OrderRefundVO getDetailByRefundNumber(String refundId) {
        OrderRefundVO orderRefundVO = orderRefundMapper.getDetailByRefundNumber(refundId);
        orderRefundVO.setBuyerReasonValue(BuyerReasonType.instance(orderRefundVO.getBuyerReason()).getCn());
        List<RefundOrderItemVO> orderItems = orderRefundVO.getOrderItems();
        // 不是整单退款
        if (!Objects.equals(orderRefundVO.getRefundType(), RefundType.ALL.value())) {
            orderItems.removeIf(next -> !Objects.equals(orderRefundVO.getOrderItemId(), next.getOrderItemId()));
        }
//        OrderLangUtil.orderRefundVOList(orderRefundVO.getOrderItems());
        return orderRefundVO;
    }

    @Override
    public void save(OrderRefund orderRefund) {
        orderRefundMapper.save(orderRefund);
    }

    @Override
    public void update(OrderRefund orderRefund) {
        orderRefundMapper.update(orderRefund);
    }

    @Override
    public void deleteById(Long refundId) {
        orderRefundMapper.deleteById(refundId);
    }

    @Override
    public boolean checkRefundDate(Order order) {
        // 判断是否超过支持的退款天数
        if (Objects.equals(order.getStatus(), OrderStatus.SUCCESS.value())) {
            long finallyTime = order.getFinallyTime().getTime();
            long currentTime = System.currentTimeMillis();
            int miniTime = Constant.MAX_FINALLY_REFUND_TIME * 24 * 60 * 60 * 1000;
            return currentTime - finallyTime <= miniTime;
        }
        return true;
    }

    @Override
    public List<OrderRefund> getProcessOrderRefundByOrderId(Long orderId) {
        return orderRefundMapper.getProcessOrderRefundByOrderId(orderId);
    }

    /**
     * 生成退款单
     *
     * @param orderRefund 退款对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyRefund(OrderRefund orderRefund) {
        // 生成退款单
        orderRefund.setRefundNumber(getRefundNumber());
        save(orderRefund);
        // 更新订单状态
        Order order = new Order();
        order.setOrderId(orderRefund.getOrderId());
        order.setRefundStatus(RefundStatusEnum.APPLY.value());
        orderService.update(order);

        // 更新订单项状态
        if (Objects.equals(RefundType.ALL.value(), orderRefund.getRefundType())) {
            orderItemService.updateRefundStatusByOrderId(order.getOrderId(), RefundStatusEnum.APPLY.value());
        } else {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(orderRefund.getOrderItemId());
            orderItem.setRefundStatus(RefundStatusEnum.APPLY.value());
            orderItem.setFinalRefundId(orderRefund.getRefundId());
            orderItemService.update(orderItem);
        }


        // 消息推送-申请退款
        List<SendNotifyBO> notifyList = orderService.listByOrderIds(Collections.singletonList(orderRefund.getOrderId()));
        SendNotifyBO notifyBO = notifyList.get(0);
        notifyBO.setPrice(PriceUtil.toDecimalPrice(orderRefund.getRefundAmount()).toString());
        notifyBO.setSendType(SendTypeEnum.LAUNCH_REFUND.getValue());
        notifyBO.setBizId(orderRefund.getOrderId());
        notifyBO.setRemark(orderRefund.getBuyerDesc());
        notifyBO.setRejectMessage(BuyerReasonType.instance(orderRefund.getBuyerReason()).getCn());
        sendNotifyToShopTemplate.syncSend(notifyBO);
//        if (!Objects.equals(sendBizStatus, SendStatus.SEND_OK)) {
//            // 这个回调方法会多次进行回调
//            logger.warn("推送退款成功消息失败，退款id为："+ orderRefund.getRefundId());
//        }
        pushRefund(orderRefund.getRefundId(), 1);


    }

    @Override
    public int syncAftersaleId(Long refundId, Long AftersaleId) {
        return orderRefundMapper.syncAftersaleId(refundId,AftersaleId);
    }

    public String getRefundNumber() {
        String refundNumber = "TD";
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        //打乱顺序
//        time=cn.hutool.core.util.RandomUtil.randomString(time,14);

        refundNumber = refundNumber + time + cn.hutool.core.util.RandomUtil.randomNumbers(6);
        log.info("生成退单编号: {}", refundNumber);
        return refundNumber;
    }


    /**
     * 买家提交物流信息
     *
     * @param orderRefund     退款单对象
     * @param orderRefundAddr 退款物流单对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitExpress(OrderRefund orderRefund, OrderRefundAddr orderRefundAddr) {
        orderRefundAddrService.update(orderRefundAddr);

        // 更新退款单信息
        if (!Objects.equals(ReturnProcessStatusEnum.CONSIGNMENT.value(), orderRefund.getReturnMoneySts())) {
            orderRefund.setReturnMoneySts(ReturnProcessStatusEnum.CONSIGNMENT.value());
        }
        orderRefund.setDeliveryTime(new Date());
        orderRefund.setUpdateTime(new Date());
        update(orderRefund);

        // 消息推送-用户已退货
        List<SendNotifyBO> notifyList = orderService.listByOrderIds(Collections.singletonList(orderRefund.getOrderId()));
        SendNotifyBO notifyBO = notifyList.get(0);
        notifyBO.setPrice(PriceUtil.toDecimalPrice(orderRefund.getRefundAmount()).toString());
        notifyBO.setSendType(SendTypeEnum.RETURN_REFUND.getValue());
        notifyBO.setBizId(orderRefund.getOrderId());
        notifyBO.setRemark(orderRefund.getBuyerDesc());
        notifyBO.setRejectMessage(BuyerReasonType.instance(orderRefund.getBuyerReason()).getCn());
        sendNotifyToShopTemplate.syncSend(notifyBO);

        //notifyTemplateService.sendNotifyByUserRefundDelivery(order, SendType.RETURN_REFUND);
        pushRefund(orderRefund.getRefundId(), 3);
    }

    @Override
    public Integer countByReturnMoneyStsAndOrderId(Integer returnMoneySts, Integer returnMoneySts2, Long orderId) {
        return orderRefundMapper.countByReturnMoneyStsAndOrderId(returnMoneySts, returnMoneySts2, orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void agreeReturns(OrderRefundDTO orderRefundParam, OrderRefundVO orderRefundVo) {
        OrderRefundAddr orderRefundAddr = new OrderRefundAddr();
        orderRefundAddr.setShopId(orderRefundVo.getShopId());
        orderRefundAddr.setRefundId(orderRefundVo.getRefundId());
        orderRefundAddr.setUserId(orderRefundVo.getUserId());


        //视频号4。0 订单退款调用视频号的退款逻辑
        if(orderRefundParam.getOrderSource()==2){
            liveStoreClient.ecaftersaleAcceptreturn(orderRefundVo.getRefundId());
        }

        //视频号4。0 订单退款调用视频号的退款逻辑
        if (orderRefundParam.getOrderSource()==3) {
            ServerResponseEntity<ChannelsAddressVO> serverResponse = channlesFeignClient.getAddressById(orderRefundParam.getShopRefundAddrId());
            if(serverResponse==null || serverResponse.isFail() || serverResponse.getData() ==null){
                Assert.faild(StrUtil.format("获取视频号4.0退货物流失败，请检查后重新提交。退货地址id:{}",orderRefundParam.getShopRefundAddrId()));
            }
            ChannelsAddressVO channelsAddressVO = serverResponse.getData();

            orderRefundAddr.setConsigneeName(channelsAddressVO.getReceiverName());
            orderRefundAddr.setConsigneeMobile(channelsAddressVO.getTelNumber());
            orderRefundAddr.setConsigneePostCode(channelsAddressVO.getPostCode());
            String addr2 = channelsAddressVO.getProvince() + channelsAddressVO.getCity() + channelsAddressVO.getTown() + channelsAddressVO.getDetailedAddress();
            orderRefundAddr.setConsigneeAddr(addr2);

            ServerResponseEntity<EcBaseResponse> response = channlesFeignClient.ecaftersaleAcceptreturn(orderRefundParam.getOutRefundId(),Long.parseLong(channelsAddressVO.getAddressId()));
            if(response==null || response.isFail()){
                Assert.faild("视频号4.0订单同意退货失败");
            }
        }else{
            // 同意退款退货操作
            // 设置退货物流信息
            ServerResponseEntity<ShopRefundAddrVO> refundAddrResponse = shopRefundAddrFeignClient.getShopRefundAddrByRefundAddrId(orderRefundParam.getShopRefundAddrId());
            if (!refundAddrResponse.isSuccess() || refundAddrResponse.getData() == null) {
                throw new LuckException("请设置退货物流信息");
            }
            ShopRefundAddrVO refundAddr = refundAddrResponse.getData();

            orderRefundAddr.setShopId(orderRefundVo.getShopId());
            orderRefundAddr.setRefundId(orderRefundVo.getRefundId());
            orderRefundAddr.setUserId(orderRefundVo.getUserId());
            orderRefundAddr.setConsigneeName(refundAddr.getConsignee());
            orderRefundAddr.setConsigneeMobile(refundAddr.getMobile());
            orderRefundAddr.setConsigneePostCode(refundAddr.getPostCode());
            String addr = refundAddr.getProvince() + refundAddr.getCity() + refundAddr.getArea() + refundAddr.getAddr();
            orderRefundAddr.setConsigneeAddr(addr);
        }

        //保存退单的退货物流地址。
        //这里因为视频号纠纷单存在发起后修改，状态重置为买家申请，这里可能在此之前已经产生了退货物流表数据。不能每次商家同意都新增保存一条数据。
        OrderRefundAddr dbAddr = orderRefundAddrService.getByRefundId(orderRefundVo.getRefundId());
        if(dbAddr==null){
            orderRefundAddrService.save(orderRefundAddr);
        }


        int agreeStats = orderRefundMapper.agreeReturns(orderRefundParam.getSellerMsg(), orderRefundVo.getRefundId());
        if (agreeStats == 0) {
            throw new LuckException("订单退款状态已发生改变，请勿重复操作");
        }
        // TODO 消息推送--同意退款
        List<SendNotifyBO> notifyBOList = orderService.listByOrderIds(Collections.singletonList(orderRefundVo.getOrderId()));
        SendNotifyBO sendNotifyBO = notifyBOList.get(0);
        sendNotifyBO.setSendType(SendTypeEnum.AGREE_REFUND.getValue());
        sendNotifyBO.setPrice(PriceUtil.toDecimalPrice(orderRefundVo.getRefundAmount()).toString());
        sendNotifyToUserTemplate.syncSend(notifyBOList);
        pushRefund(orderRefundParam.getRefundId(), 2);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnFreightAmount(OrderRefundDTO orderRefundParam, OrderRefundVO orderRefundVo) {
        //查询当前订单的运费金额。
//        OrderVO orderVO = orderService.getOrderByOrderId(orderRefundVo.getOrderId());
        Order orderVO = orderService.getByOrderId(orderRefundVo.getOrderId());

        //如果退运费则判断当前退单是否包含退费金额,如果需要退运费，则查询当前退单是否已经包含退费
        if(orderRefundParam.getReturnFreightFlag()==1){
            //需要退运费并且，运费金额等于0
            if(orderRefundVo.getRefundFreightAmount()==0){
                // 获取所有正在进行中的退款订单
                List<OrderRefund> orderRefunds = this.getProcessOrderRefundByOrderId(orderRefundVo.getOrderId());
                for (OrderRefund orderRefund : orderRefunds) {
                    if (!ObjectUtil.equals(orderRefund.getRefundId(),orderRefundVo.getRefundId()) &&
                            orderRefund.getRefundFreightAmount()>0) {
                        Assert.faild("当前订单正在或者已经退了运费，不允许重复退运费。");
                    }
                }
                //修改退单的退费金额增加运费的金额
                orderRefundMapper.returnFreightAmount(orderRefundVo.getRefundId(),orderVO.getFreightAmount());
                orderRefundVo.setRefundFreightAmount(orderRefundVo.getRefundAmount() + orderVO.getFreightAmount());
                orderRefundVo.setRefundAmount(orderRefundVo.getRefundAmount() + orderVO.getFreightAmount());
            }else{
                //需要退运费，并且当前订单退费金额大于0，不做处理。
            }
        }

        //如果不退运费
        if(orderRefundParam.getReturnFreightFlag()==2){
            //判断当前退单是否已经包含运费了，如果已经包含运费，则修改退单金额
            if(orderRefundVo.getRefundFreightAmount()>0){
                orderRefundMapper.unReturnFreightAmount(orderRefundVo.getRefundId());
                orderRefundVo.setRefundFreightAmount(orderRefundVo.getRefundAmount() - orderVO.getFreightAmount());
                orderRefundVo.setRefundAmount(orderRefundVo.getRefundAmount() - orderVO.getFreightAmount());
            }else{
                //不退运费，并且当前退单的退费金额=0，不做处理
            }

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundSuccess(RefundNotifyBO refundNotifyBO) {
        Long refundId = refundNotifyBO.getRefundId();
        OrderRefundVO orderRefundVO = orderRefundMapper.getByRefundId(refundId);
        // 已经进行过退款了
        if (Objects.equals(orderRefundVO.getReturnMoneySts(), ReturnProcessStatusEnum.SUCCESS.value())) {
            return;
        }


        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setRefundId(refundId);
        orderRefund.setRefundTime(new Date());
        orderRefund.setReturnMoneySts(ReturnProcessStatusEnum.SUCCESS.value());
        // 更新退款状态
        orderRefundMapper.update(orderRefund);
        // 更新结算单状态
        int updateStatus = orderRefundSettlementMapper.updateToSuccessByRefundId(refundId);
        // 视频号订单超时自动退款的记录这里没有生成订单结算记录。这里注释掉更新失败抛出异常操作。
//        if (updateStatus < 1) {
//            throw new LuckException(ResponseEnum.EXCEPTION);
//        }

        Order dbOrder = orderService.getByOrderId(refundNotifyBO.getOrderId());
        OrderItemVO dbOrderItem = orderItemService.getByOrderItemId(orderRefundVO.getOrderItemId());
        Order order = new Order();

        // 处理订单退款更新
        handleOrderRefund(refundNotifyBO, orderRefundVO, dbOrder, order, dbOrderItem);
        // 还原库存 视频号4.0的订单这里不还原库存。
        if(dbOrder.getOrderSource()!= OrderSource.CHANNELS.value()){
            reductionStock(orderRefundVO, dbOrder, dbOrderItem);
        }

        boolean canRefundFlag = Objects.equals(dbOrder.getStatus(), OrderStatus.PAYED.value())
                || Objects.equals(dbOrder.getStatus(), OrderStatus.CONSIGNMENT.value())
                || Objects.equals(dbOrder.getStatus(), OrderStatus.SUCCESS.value());
        if (canRefundFlag && Objects.equals(order.getStatus(), OrderStatus.CLOSE.value())) {
            // 通知还原优惠券
            refundCoupon(dbOrder, order);
            // 订单在已支付，已发货，确认收货的情况下，退款导致订单关闭
            // 需要判断下是否要进行结算给商家的操作，这个结算的计算是退款完成后结算的，所以不要随便改变顺序
            handlePartialRefund(dbOrder);
        }
        // 还原成长值
        refundGrowth(refundNotifyBO.getOrderId(), dbOrder, order);
        // 如果使用了积分抵扣就需要将积分退给用户
        refundScore(refundNotifyBO.getRefundId(), dbOrder);
        pushRefund(orderRefund.getRefundId(), 4);

        // 平台退单成功订阅消息通知
        try {
            OrderRefundVO orderRefundVo = this.getDetailByRefundId(refundId);
            List<String> businessIds = new ArrayList<>();
            businessIds.add(StrUtil.toString(refundNotifyBO.getRefundId()));
            ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.CHARGE_BACK_COMPLETE.getValue(),
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
                        String page = "packageRefund/pages/refund-detail/refund-detail?refundId="+orderRefund.getRefundId();
                        String spuName = orderRefundVo.getOrderItems().get(0).getSpuName();
                        String auditResult = "成功";

                        /**
                         * 值替换
                         * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                         * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                         * 当前微客 场景下{orderId} 订单编号、{amount} 退款金额、  {spuName} 商品名称 {refundId} 退款单编号 {auditResult} 审核结果(通过/拒绝) {auditTime} 审核时间
                         * 构建参数map.
                         */
                        Map<String,String> paramMap = new HashMap();
                        paramMap.put("{orderId}",StrUtil.toString(dbOrder.getOrderNumber()));
                        paramMap.put("{amount}", StrUtil.toString(new BigDecimal(orderRefundVo.getRefundAmount()).divide(new BigDecimal(100).setScale(2))));
                        paramMap.put("{spuName}",spuName);
                        paramMap.put("{refundId}",orderRefundVO.getRefundNumber());
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
                    log.info("退单成功发送订阅消息，构建参数对象 {}", JSONObject.toJSONString(notifyList));
                    sendMaSubcriptMessageTemplate.syncSend(notifyList);
                }
            }
        } catch (Exception e) {
            log.error("退单成功通知订阅通知发送异常",e);
        }
    }

    /**
     * 处理订单退款更新
     *
     * @param refundNotifyBO 退款通知
     * @param orderRefundVO  订单退款信息
     * @param dbOrder        订单信息
     * @param order          订单更新数据
     * @param dbOrderItem    订单项信息
     */
    private void handleOrderRefund(RefundNotifyBO refundNotifyBO, OrderRefundVO orderRefundVO, Order dbOrder, Order order, OrderItemVO dbOrderItem) {
        order.setOrderId(refundNotifyBO.getOrderId());
        if (Objects.equals(orderRefundVO.getRefundType(), RefundType.ALL.value())) {
            order.setStatus(OrderStatus.CLOSE.value());
        } else {
            // 查看是否已经达到了所有的订单数量，如是则订单关闭
            // 已退款单
            Integer refundSuccessRefundCount = orderRefundMapper.allCountRefundSuccessRefundCountByOrderId(refundNotifyBO.getOrderId());
            Integer itemNum = orderItemService.allCountByOrderId(refundNotifyBO.getOrderId());
            // 整个订单完成退款的时候
            if (refundSuccessRefundCount >= itemNum) {
                order.setStatus(OrderStatus.CLOSE.value());
            } else {
                order.setRefundStatus(RefundStatusEnum.PARTIAL_SUCCESS.value());
            }

            // 退款成功后，订单没有关闭，且订单为待发货，减少订单待发货商品数量
            if (!Objects.equals(order.getStatus(), OrderStatus.CLOSE.value()) && Objects.equals(dbOrder.getStatus(), OrderStatus.PAYED.value())) {
                Long orderItemId = orderRefundVO.getOrderItemId();
                // 减少订单待发货商品数量
                orderItemService.reduceUnDeliveryNumByOrderItemId(orderItemId, orderRefundVO.getRefundCount());
                //
                int unDeliveryNum = orderItemService.countUnDeliveryNumByOrderId(refundNotifyBO.getOrderId());
                if (Objects.equals(unDeliveryNum, 0)) {
                    // 如果订单为快递发货情况，查询所有的订单项发货情况，如果有个是快递发货则是快递发货，否则全是无需快递则订单也为无需快递方式。
                    if (Objects.equals(dbOrder.getDeliveryType(), DeliveryType.DELIVERY.value())) {
                        List<OrderItemVO> orderItemList = orderItemService.listOrderItemAndLangByOrderId(refundNotifyBO.getOrderId());
                        int deliveryType = DeliveryType.NOT_DELIVERY.value();
                        for (OrderItemVO orderItemVO : orderItemList) {
                            if (Objects.nonNull(orderItemVO.getDeliveryType()) && Objects.equals(orderItemVO.getDeliveryType(), DeliveryType.DELIVERY.value())) {
                                deliveryType = DeliveryType.DELIVERY.value();
                                break;
                            }
                        }
                        order.setDeliveryType(deliveryType);
                    }
                    order.setStatus(OrderStatus.CONSIGNMENT.value());
                    order.setDeliveryTime(new Date());
                }
            }
        }
        if (Objects.equals(order.getStatus(), OrderStatus.CLOSE.value())) {
            order.setCancelTime(new Date());
            order.setUpdateTime(new Date());
            order.setCloseType(OrderCloseType.REFUND.value());
            order.setRefundStatus(RefundStatusEnum.SUCCEED.value());
        }
        // 更新订单项状态
        if (Objects.equals(orderRefundVO.getRefundType(), RefundType.ALL.value())) {
            orderItemService.updateRefundStatusByOrderId(order.getOrderId(), RefundStatusEnum.SUCCEED.value());
            orderItemService.jointVentureCommissionOrderItemSettled(Collections.singletonList(order.getOrderId()), 0, 1);
            handleDistributionAmount(true, dbOrder, null, null);
        } else {
            dbOrderItem = orderItemService.getByOrderItemId(orderRefundVO.getOrderItemId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(orderRefundVO.getOrderItemId());
            // 因为我们商城目前设计为一个订单项只能进行一次退款，无法进行多次退款
            if (dbOrderItem.getCount() > orderRefundVO.getRefundCount()) {
                orderItem.setRefundStatus(RefundStatusEnum.PARTIAL_SUCCESS.value());
            } else {
                orderItem.setRefundStatus(RefundStatusEnum.SUCCEED.value());
            }
            orderItem.setJointVentureRefundStatus(1);
            orderItem.setJointVentureCommissionStatus(0);
            handleDistributionAmount(false, dbOrder, dbOrderItem, orderItem);
            orderItemService.update(orderItem);
        }
        // 处理联营分佣退款状态
        // 如果订单未被申请过分佣，且为整单退款，直接将订单置为不分佣
        if (dbOrder.getJointVentureCommissionStatus() == 0
                && Objects.equals(order.getStatus(), OrderStatus.CLOSE.value())) {
            order.setJointVentureCommissionStatus(-1);
        } else {
            order.setJointVentureCommissionStatus(0);
        }
        order.setJointVentureRefundStatus(1);
        orderService.update(order);
    }

    /**
     * 处理分销佣金
     *
     * @param fullRefundFlag  是否整单退款标志 true-是 false-否
     * @param dbOrder         数据库订单对象
     * @param dbOrderItem     数据库订单子项对象
     * @param updateOrderItem 待修改订单子项对象
     */
    private void handleDistributionAmount(boolean fullRefundFlag, Order dbOrder, OrderItemVO dbOrderItem, OrderItem updateOrderItem) {
        if (dbOrder.getDistributionStatus() == 0) { // Tips：未结算的佣金这里不处理，留着在结算任务中回去处理并扣减待结算金额
            return;
        }
        try {
            // 整单退款
            if (fullRefundFlag) {
                // 判断是否重复处理
                List<OrderItemVO> orderItems = orderItemService.getOrderItemsByOrderIds(Collections.singletonList(dbOrder.getOrderId()));
                if (CollectionUtils.isEmpty(orderItems)) {
                    return;
                }
                boolean processed = orderItems.stream().anyMatch(orderItem -> orderItem.getDistributionRefundStatus() == 1);
                if (processed) {
                    log.error("订单退款成功扣减已提现佣金失败：已扣减，重复回调! 订单ID：{}", dbOrder.getOrderId());
                    return;
                }
                // 处理分销已提现需退款情况
                doHandleDistributionAmount(dbOrder, dbOrder.getDistributionAmount(), dbOrder.getDevelopingAmount());
                EsOrderBO esOrderBO = orderService.getEsOrder(dbOrder.getOrderId());
                if (esOrderBO != null && CollectionUtils.isNotEmpty(esOrderBO.getOrderItems())) {
                    // 全部退单
                    esOrderBO.getOrderItems().forEach(esOrderItemBO -> esOrderItemBO.setDistributionAmount(0L));
                    esOrderBO.getOrderItems().forEach(esOrderItemBO -> esOrderItemBO.setDistributionParentAmount(0L));
                    orderItemService.updateBatchDistributionAmount(esOrderBO.getOrderItems());
                }
                orderItemService.updateDistributionRefundStatusByOrderId(dbOrder.getOrderId(), 1);
            } else { // 非整单退款
                // 判断是否重复处理
                if (dbOrderItem.getDistributionRefundStatus() == 1) {
                    log.error("订单子项退款成功扣减已提现佣金失败：已扣减，重复回调! 订单子项ID：{}", dbOrderItem.getOrderItemId());
                    return;
                }
                AtomicReference<EsOrderItemBO> refundEsOrderItemBOReference = new AtomicReference<>();
                EsOrderBO esOrderBO = orderService.getEsOrder(dbOrder.getOrderId());
                if (esOrderBO != null && CollectionUtils.isNotEmpty(esOrderBO.getOrderItems())) {
                    esOrderBO.getOrderItems().forEach(esOrderItemBO -> {
                        if (Objects.equals(esOrderItemBO.getOrderItemId(), dbOrderItem.getOrderItemId())) {
                            EsOrderItemBO copyOfEsOrderItemBO = new EsOrderItemBO();
                            copyOfEsOrderItemBO.setDistributionAmount(esOrderItemBO.getDistributionAmount());
                            copyOfEsOrderItemBO.setDistributionParentAmount(esOrderItemBO.getDistributionParentAmount());
                            refundEsOrderItemBOReference.set(copyOfEsOrderItemBO);
                            esOrderItemBO.setDistributionAmount(0L);
                            esOrderItemBO.setDistributionParentAmount(0L);
                        }
                    });
                    orderItemService.updateBatchDistributionAmount(esOrderBO.getOrderItems());
                }
                if (refundEsOrderItemBOReference.get() == null) {
                    log.error("订单子项退款成功扣减已提现佣金失败：找不到对应的退款子订单项信息！订单子项ID：{}", dbOrderItem.getOrderItemId());
                    return;
                }
                EsOrderItemBO esOrderItemBO = refundEsOrderItemBOReference.get();
                doHandleDistributionAmount(dbOrder, esOrderItemBO.getDistributionAmount(), esOrderItemBO.getDistributionParentAmount());
                updateOrderItem.setDistributionRefundStatus(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("售后退款成功处理分销佣金出现错误：orderId: {}, exception: {}", dbOrder.getOrderId(), e.getLocalizedMessage());
        }
    }

    private void doHandleDistributionAmount(Order dbOrder, Long distributionAmount, Long developingAmount) {
        if (dbOrder.getDistributionUserId() > 0 && distributionAmount > 0) {
            // 处理分销已提现需退款情况
            if (Objects.equals(dbOrder.getDistributionStatus(), 2) || Objects.equals(dbOrder.getDistributionStatus(), 3)) {
                distributionFeignClient.updateCommissionWithLogByOneOrder(
                        dbOrder.getDistributionUserType(), dbOrder.getDistributionUserId(),
                        distributionAmount, CommissionChangeTypeEnum.ADD_WITHDRAW_NEED_REFUND.getType(),
                        dbOrder.getOrderId(), 3, 1);
            } else if (Objects.equals(dbOrder.getDistributionStatus(), 1)) { // 处理已结算需退款情况，可提现金额减少
                distributionFeignClient.updateCommissionWithLogByOneOrder(
                        dbOrder.getDistributionUserType(), dbOrder.getDistributionUserId(),
                        distributionAmount, CommissionChangeTypeEnum.REDUCE_CAN_WITHDRAW.getType(),
                        dbOrder.getOrderId(), 3, 1);
            }
        }
        if (dbOrder.getDevelopingUserId() > 0 && developingAmount > 0) {
            // 处理发展已提现需退款情况
            if (Objects.equals(dbOrder.getDevelopingStatus(), 2) || Objects.equals(dbOrder.getDevelopingStatus(), 3)) {
                distributionFeignClient.updateCommissionWithLogByOneOrder(
                        1, dbOrder.getDevelopingUserId(),
                        developingAmount, CommissionChangeTypeEnum.ADD_WITHDRAW_NEED_REFUND.getType(),
                        dbOrder.getOrderId(), 3, 2);
            } else if (Objects.equals(dbOrder.getDevelopingStatus(), 1)) { // 处理已结算需退款情况，可提现金额减少
                distributionFeignClient.updateCommissionWithLogByOneOrder(
                        1, dbOrder.getDevelopingUserId(),
                        developingAmount, CommissionChangeTypeEnum.REDUCE_CAN_WITHDRAW.getType(),
                        dbOrder.getOrderId(), 3, 2);
            }
        }
    }

    /**
     * 处理下部分退款完成，订单关闭的时候未结算变成已结算的金额处理，退款超时定时任务。
     *
     * @param order 订单信息
     */
    private void handlePartialRefund(Order order) {
        // 1.获取到部分退款并且订单关闭后，需要结算给商家、平台的钱
        // 获取所有正在进行中的退款订单
        List<OrderRefund> orderRefunds = getProcessOrderRefundByOrderId(order.getOrderId());
        long alreadyRefundAmount = 0L;
        long changePlatformCommission = 0L;
        long platformAllowanceAmount = 0L;
        for (OrderRefund orderRefund : orderRefunds) {
            if (Objects.equals(RefundType.ALL.value(), orderRefund.getRefundType())) {
                return;
            }
            alreadyRefundAmount += orderRefund.getRefundAmount()==null?0l:orderRefund.getRefundAmount();
            changePlatformCommission += orderRefund.getPlatformRefundCommission()==null?0l:orderRefund.getPlatformRefundCommission();
            platformAllowanceAmount += orderRefund.getPlatformRefundAmount()==null?0l:orderRefund.getPlatformRefundAmount();
        }
        long partialRefundAmount = order.getActualTotal() - alreadyRefundAmount;
        if (partialRefundAmount <= 0) {
            return;
        }
        OrderChangeShopWalletAmountBO orderChangeShopWalletAmountBO = new OrderChangeShopWalletAmountBO();
        orderChangeShopWalletAmountBO.setOrderStatus(order.getStatus());
        orderChangeShopWalletAmountBO.setActualTotal(order.getActualTotal());
        orderChangeShopWalletAmountBO.setRefundAmount(alreadyRefundAmount);
        orderChangeShopWalletAmountBO.setPlatformAllowanceAmount(order.getPlatformAmount() - platformAllowanceAmount);
        orderChangeShopWalletAmountBO.setOrderId(order.getOrderId());
        orderChangeShopWalletAmountBO.setShopId(order.getShopId());
        orderChangeShopWalletAmountBO.setPlatformCommission(order.getPlatformCommission());
        orderChangeShopWalletAmountBO.setChangePlatformCommission(changePlatformCommission);
        orderChangeShopWalletAmountBO.setDistributionAmount(order.getDistributionAmount());
        // 2.发送消息给商家，将需要结算的钱进行结算
        // 减少商家待结算金额
        SendResult sendResult = orderRefundSuccessSettlementTemplate.syncSend(orderChangeShopWalletAmountBO);
        if (sendResult == null || sendResult.getMessageId() == null) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    /**
     * 还原下单的成长值
     *
     * @param orderId 订单号
     * @param dbOrder 数据库中，未修改订单之前的订单信息
     * @param order   订单参数
     */
    private void refundGrowth(Long orderId, Order dbOrder, Order order) {
        // 订单的成长值是再确认收货时才发给用户的,所以成长值的退还，必须是已收货状态的订单
        // 并且订单退款状态将修改成已关闭的订单
        if (Objects.equals(dbOrder.getStatus(), OrderStatus.SUCCESS.value()) || Objects.equals(order.getStatus(), OrderStatus.CLOSE.value())) {
            // 通知退还成长值
            SendResult sendResult = orderRefundSuccessGrowthTemplate.syncSend(orderId);
            if (sendResult == null || sendResult.getMessageId() == null) {
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
        }
    }

    private void refundScore(Long refundId, Order dbOrder) {
        // 订单已关闭就不退积分
        if (Objects.equals(dbOrder.getStatus(), OrderStatus.CLOSE.value())) {
            return;
        }
        UserOrderScoreBo userOrderScoreBo = new UserOrderScoreBo();
        userOrderScoreBo.setUserId(dbOrder.getUserId());
        userOrderScoreBo.setRefundId(refundId);
        userOrderScoreBo.setOrderId(dbOrder.getOrderId());
        userOrderScoreBo.setOrderScore(dbOrder.getOrderScore());
        // 通知退还购物时抵扣使用的积分
        SendResult sendResult = orderRefundSuccessScoreTemplate.syncSend(userOrderScoreBo);
        if (sendResult == null || sendResult.getMessageId() == null) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    private void refundCoupon(Order dbOrder, Order order) {
        // 通知还原优惠券
        SendResult sendResult = orderRefundSuccessCouponTemplate.syncSend(order.getOrderId());
        if (sendResult == null || sendResult.getMessageId() == null) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    private void reductionStock(OrderRefundVO orderRefundVO, Order dbOrder, OrderItemVO dbOrderItem) {

        ArrayList<OrderGiftReduceAppDTO> orderGiftReduceAppDTOS = new ArrayList<>();

        // 订单待发货，结果就被取消了，还原库存
        if (Objects.equals(dbOrder.getStatus(), OrderStatus.PAYED.value())) {
            List<RefundReductionStockBO> refundReductionStocks;
            // 整单退款
            if (Objects.equals(orderRefundVO.getRefundType(), RefundType.ALL.value())) {
                List<OrderItem> orderItems = orderItemService.listOrderItemsByOrderId(orderRefundVO.getOrderId());
                refundReductionStocks = orderItems.stream().map(orderItem -> {
                    RefundReductionStockBO refundReductionStockBO = new RefundReductionStockBO();
                    refundReductionStockBO.setCount(orderItem.getCount());
                    refundReductionStockBO.setSkuId(orderItem.getSkuId());
                    refundReductionStockBO.setSpuId(orderItem.getSpuId());
                    return refundReductionStockBO;
                }).collect(Collectors.toList());

                //筛选出赠品订单
                for (OrderItem orderItem : orderItems) {
                    if(orderItem.getType() == 1 && orderItem.getGiftActivityId()!=null){
                        OrderGiftReduceAppDTO orderGiftReduceAppDTO = new OrderGiftReduceAppDTO();
                        orderGiftReduceAppDTO.setActivityId(orderItem.getGiftActivityId().intValue());
                        orderGiftReduceAppDTO.setCommodityId(orderItem.getSpuId());
                        orderGiftReduceAppDTO.setNum(orderItem.getCount());
                        orderGiftReduceAppDTOS.add(orderGiftReduceAppDTO);
                    }
                }
            } else {
                RefundReductionStockBO refundReductionStockBO = new RefundReductionStockBO();
                refundReductionStockBO.setCount(dbOrderItem.getCount());
                refundReductionStockBO.setSkuId(dbOrderItem.getSkuId());
                refundReductionStockBO.setSpuId(dbOrderItem.getSpuId());
                refundReductionStocks = Collections.singletonList(refundReductionStockBO);
                //如果包含赠品订单
//                log.info("dbOrderItem 订单详情：{}", JSONObject.toJSONString(dbOrderItem));
//                if(dbOrderItem.getType() == 1 && null != dbOrderItem.getGiftActivityId()){
//                    OrderGiftReduceAppDTO orderGiftReduceAppDTO = new OrderGiftReduceAppDTO();
//                    orderGiftReduceAppDTO.setActivityId(dbOrderItem.getGiftActivityId().intValue());
//                    orderGiftReduceAppDTO.setCommodityId(dbOrderItem.getSpuId());
//                    orderGiftReduceAppDTO.setNum(dbOrderItem.getCount());
//                    orderGiftReduceAppDTOS.add(orderGiftReduceAppDTO);
//                }
            }

            // 通知还原库存
            SendResult sendResult = orderRefundSuccessStockTemplate.syncSend(refundReductionStocks);
            if (sendResult == null || sendResult.getMessageId() == null) {
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
        }

        if(CollUtil.isNotEmpty(orderGiftReduceAppDTOS)){
            groupFeignClient.unLockStock(orderGiftReduceAppDTOS);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelRefund(OrderRefundVO orderRefundVO) {


        // 更新退款状态为已取消
        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setReturnMoneySts(ReturnProcessStatusEnum.FAIL.value());
        orderRefund.setCloseType(RefundCloseType.CANCEL.value());
        orderRefund.setCloseTime(new Date());
        orderRefund.setRefundId(orderRefundVO.getRefundId());
        update(orderRefund);

        closeOrderAndOrderItemRefundStatus(orderRefundVO);

        pushRefund(orderRefundVO.getRefundId(), ReturnProcessStatusEnum.FAIL.value());
    }

    /**
     * 关闭订单和订单项的退款状态
     *
     * @param orderRefundVo
     */
    private void closeOrderAndOrderItemRefundStatus(OrderRefundVO orderRefundVo) {
        Integer orderRefundSuccessCount = orderRefundMapper.countReturnProcessingItemByOrderId(orderRefundVo.getOrderId());

        // 更新订单
        // 如果有订单项退款成功，则订单状态为部分退款成功，否则为退款失败
        if (Objects.equals(orderRefundSuccessCount, 0)) {
            Order order = new Order();
            order.setOrderId(orderRefundVo.getOrderId());
            order.setRefundStatus(RefundStatusEnum.DISAGREE.value());
            orderService.update(order);
        }
        // 更新订单项状态
        if (Objects.equals(orderRefundVo.getRefundType(), RefundType.ALL.value())) {
            orderItemService.updateRefundStatusByOrderId(orderRefundVo.getOrderId(), RefundStatusEnum.DISAGREE.value());
        } else {
            // 拒绝退款
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(orderRefundVo.getOrderItemId());
            orderItem.setRefundStatus(RefundStatusEnum.DISAGREE.value());
            orderItemService.update(orderItem);
        }

    }

    /**
     * 不同意退款
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disagreeRefund(OrderRefundDTO orderRefundParam, OrderRefundVO orderRefundVo) {

        ServerResponseEntity<EsOrderBO> serverResponse = orderFeignClient.getEsOrder(orderRefundVo.getOrderId());
        if (serverResponse == null || serverResponse.isFail() || serverResponse.getData() == null) {
            Assert.faild(StrUtil.format("执行不同意退款操作，订单id不存在，参数信息：{}", orderRefundVo.getOrderId()));
        }
        EsOrderBO orderBO = serverResponse.getData();
        //视频号3。0 订单退款调用视频号的退款逻辑
        if (StrUtil.isNotEmpty(orderBO.getTraceId()) && orderBO.getOrderSource()==2) {
            ServerResponseEntity<BaseResponse> baseResponseServerResponse = liveStoreClient.ecaftersaleReject(orderRefundVo.getRefundId());
        }

        //视频号4。0 订单退款调用视频号的退款逻辑
        if (orderBO.getOrderSource()==3) {
            ServerResponseEntity<EcBaseResponse> response = channlesFeignClient.ecaftersaleReject(orderRefundParam.getOutRefundId(),orderRefundParam.getRejectMessage());
        }


        int agreeStats = orderRefundMapper.disagreeRefund(orderRefundParam.getSellerMsg(), orderRefundParam.getRejectMessage(),
                orderRefundVo.getRefundId(), orderRefundVo.getReturnMoneySts(), orderRefundParam.getIsReceived());
        if (agreeStats == 0) {
            throw new LuckException("订单退款状态已发生改变，请勿重复操作");
        }
        closeOrderAndOrderItemRefundStatus(orderRefundVo);

        // TODO 消息推送--拒绝退款
        List<SendNotifyBO> notifyBOList = orderService.listByOrderIds(Collections.singletonList(orderRefundVo.getOrderId()));
        SendNotifyBO sendNotifyBO = notifyBOList.get(0);
        sendNotifyBO.setSendType(SendTypeEnum.REFUSE_REFUND.getValue());
        sendNotifyBO.setRejectMessage(orderRefundParam.getRejectMessage());
        sendNotifyBO.setPrice(PriceUtil.toDecimalPrice(orderRefundVo.getRefundAmount()).toString());
        sendNotifyToUserTemplate.syncSend(notifyBOList);
        pushRefund(orderRefundParam.getRefundId(), 5);
    }

    /**
     * 同意退款操作（生成结算记录）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int agreeRefund(OrderRefundVO orderRefundVo, OrderRefundDTO orderRefundParam) {
        Date nowDate = new Date();

        // 订单正在进行退款结算,之前已经同意过了，不需要再操作了
        if (orderRefundSettlementMapper.countByRefundId(orderRefundVo.getRefundId()) > 0) {
            return 0;
        }

        // 生成退款结算记录
        OrderRefundSettlement orderRefundSettlement = new OrderRefundSettlement();
        orderRefundSettlement.setOrderId(orderRefundVo.getOrderId());
        orderRefundSettlement.setPayId(orderRefundVo.getPayId());
        orderRefundSettlement.setRefundId(orderRefundVo.getRefundId());
        orderRefundSettlement.setRefundNumber(orderRefundVo.getRefundNumber());
        orderRefundSettlement.setPayType(orderRefundVo.getPayType());
        orderRefundSettlement.setRefundAmount(orderRefundVo.getRefundAmount());
        orderRefundSettlement.setOrderTotalAmount(orderRefundVo.getActualTotal());
        orderRefundSettlement.setUserId(orderRefundVo.getUserId());
        orderRefundSettlement.setRefundStatus(1);
        orderRefundSettlement.setCreateTime(nowDate);
        orderRefundSettlement.setUpdateTime(nowDate);
        orderRefundSettlementMapper.save(orderRefundSettlement);

        // 正在处理退款的状态,如果是仅退款到这里就更新一下处理时间，不是则不更新这时间
        // 获取退款单信息
        OrderRefundVO orderRefundDb = orderRefundMapper.getDetailByRefundId(orderRefundParam.getRefundId());
        int updateStatus = orderRefundMapper.agreeRefund(orderRefundParam.getSellerMsg(), orderRefundVo.getRefundId(),
                orderRefundVo.getReturnMoneySts(), orderRefundParam.getIsReceived(), orderRefundDb.getApplyType());
        if (updateStatus == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public Integer getRefundStatus(Long refundId) {
        return orderRefundMapper.getRefundStatus(refundId);
    }


    @Override
    public int initRefundStatus(Long refundId,Integer status,String buyDesc,String imgs) {
        return orderRefundMapper.initRefundStatus(refundId,status,buyDesc,imgs);
    }

    @Override
    public int ecInitRefundStatus(Long refundId, Integer status, Integer applyType,Long refundAmount) {
        return orderRefundMapper.ecInitRefundStatus(refundId,status,applyType,refundAmount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createGroupUnSuccessRefundInfo(RefundNotifyBO refundNotifyBO) {
        Order dbOrder = orderService.getByOrderId(refundNotifyBO.getOrderId());
        // 生成退款单信息
        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setRefundId(refundNotifyBO.getRefundId());
        orderRefund.setRefundNumber(refundNotifyBO.getRefundNumber());
        orderRefund.setShopId(dbOrder.getShopId());
        orderRefund.setOrderId(refundNotifyBO.getOrderId());
        orderRefund.setRefundType(RefundType.ALL.value());
        orderRefund.setRefundAmount(refundNotifyBO.getRefundAmount());
        orderRefund.setApplyType(1);
        orderRefund.setApplySource(2);//团购失败自动创建
        orderRefund.setIsReceived(0);
        orderRefund.setPlatformRefundAmount(dbOrder.getPlatformAmount());
        orderRefund.setBuyerReason(BuyerReasonType.GROUP_FAILED.value());
        orderRefund.setBuyerDesc("拼团失败：系统自动退款");
        orderRefund.setReturnMoneySts(ReturnProcessStatusEnum.APPLY.value());
        orderRefund.setSellerMsg("拼团失败：系统自动退款");
        orderRefund.setHandelTime(new Date());
        orderRefund.setUpdateTime(new Date());
        orderRefund.setUserId(dbOrder.getUserId());
        orderRefund.setOrderItemId(0L);
        orderRefundMapper.save(orderRefund);

        OrderRefundSettlement orderRefundSettlement = new OrderRefundSettlement();
        orderRefundSettlement.setOrderId(refundNotifyBO.getOrderId());
        orderRefundSettlement.setPayId(refundNotifyBO.getPayId());
        orderRefundSettlement.setRefundId(refundNotifyBO.getRefundId());
        orderRefundSettlement.setRefundAmount(refundNotifyBO.getRefundAmount());
        orderRefundSettlement.setOrderTotalAmount(refundNotifyBO.getRefundAmount());
        orderRefundSettlement.setRefundStatus(1);
        orderRefundSettlement.setUserId(dbOrder.getUserId());
        orderRefundSettlement.setPayType(dbOrder.getPayType());
        orderRefundSettlementMapper.save(orderRefundSettlement);

        refundSuccess(refundNotifyBO);
    }

    @Override
    public List<OrderRefundVO> getProcessingOrderRefundByOrderId(Long orderId) {
        return orderRefundMapper.getProcessingOrderRefundByOrderId(orderId);
    }

    @Override
    public List<OrderRefundProdEffectRespVO> getProdRefundEffectByDateAndProdIds(List<Long> spuIds, Date startTime, Date endTime) {
        return orderRefundMapper.getProdRefundEffectByDateAndProdIds(spuIds, startTime, endTime);
    }

    @Override
    public List<com.mall4j.cloud.api.order.vo.OrderRefundVO> getOrderRefundByOrderIdAndRefundStatus(Long orderId, Integer returnMoneySts) {
        return orderRefundMapper.getOrderRefundByOrderIdAndRefundStatus(orderId, returnMoneySts);
    }

    @Override
    public com.mall4j.cloud.api.order.vo.OrderRefundVO getOrderRefundByAftersaleId(Long aftersaleId) {
        return orderRefundMapper.getOrderRefundByAftersaleId(aftersaleId);
    }

    @Override
    public void aftersaleReturnOverdue(Long aftersaleId) {
        log.info("视频号处理退货超时回调处理。aftersaleId:{}",aftersaleId);
        com.mall4j.cloud.api.order.vo.OrderRefundVO orderRefundVO = this.getOrderRefundByAftersaleId(aftersaleId);
        if(orderRefundVO==null){
            Assert.faild(StrUtil.format("售后单据在平台查询不到记录，aftersaleId:{}",aftersaleId));
        }

        ServerResponseEntity<ShopRefundAddrVO> refundAddrResponse = shopRefundAddrFeignClient.getMainRefundAddr();
        if (!refundAddrResponse.isSuccess() || refundAddrResponse.getData() == null) {
            throw new LuckException("请设置退货物流信息");
        }
        ShopRefundAddrVO refundAddr = refundAddrResponse.getData();

        OrderRefundAddr orderRefundAddr = new OrderRefundAddr();
        orderRefundAddr.setShopId(orderRefundVO.getShopId());
        orderRefundAddr.setRefundId(orderRefundVO.getRefundId());
        orderRefundAddr.setUserId(orderRefundVO.getUserId());
        orderRefundAddr.setConsigneeName(refundAddr.getConsignee());
        orderRefundAddr.setConsigneeMobile(refundAddr.getMobile());
        orderRefundAddr.setConsigneePostCode(refundAddr.getPostCode());
        String addr = refundAddr.getProvince() + refundAddr.getCity() + refundAddr.getArea() + refundAddr.getAddr();
        orderRefundAddr.setConsigneeAddr(addr);
        orderRefundAddrService.save(orderRefundAddr);

        int agreeStats = orderRefundMapper.agreeReturns("", orderRefundVO.getRefundId());
        if (agreeStats == 0) {
            throw new LuckException("订单退款状态已发生改变，请勿重复操作");
        }
        // TODO 消息推送--同意退款
//        List<SendNotifyBO> notifyBOList = orderService.listByOrderIds(Collections.singletonList(orderRefundVo.getOrderId()));
//        SendNotifyBO sendNotifyBO = notifyBOList.get(0);
//        sendNotifyBO.setSendType(SendTypeEnum.AGREE_REFUND.getValue());
//        sendNotifyBO.setPrice(PriceUtil.toDecimalPrice(orderRefundVo.getRefundAmount()).toString());
//        sendNotifyToUserTemplate.syncSend(notifyBOList);
        pushRefund(orderRefundVO.getRefundId(), 2);
    }

    @Override
    public List<OrderRefundSimpleVO> listOrderIdsByRefundIds(List<Long> refundIds) {
        if (CollUtil.isEmpty(refundIds)) {
            return new ArrayList<>();
        }
        return orderRefundMapper.listOrderIdsByRefundIds(refundIds);
    }

    @Override
    public List<OrderRefundVO> listOrderRefundTimeOut(Date date) {
        return orderRefundMapper.listOrderRefundTimeOut(date);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelWhenTimeOut(List<OrderRefundVO> orderRefundList) {
        Date now = new Date();
        List<Order> orders = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRefundVO orderRefund : orderRefundList) {
            orderRefund.setReturnMoneySts(ReturnProcessStatusEnum.FAIL.value());
            orderRefund.setSellerMsg("申请退款超时");
            orderRefund.setCloseTime(now);
            orderRefund.setCloseType(RefundCloseType.TIME_OUT.value());
            orderRefund.setUpdateTime(now);
            orderRefund.setHandelTime(now);
            // 批量修改订单退款状态
            Order order = new Order();
            order.setOrderId(orderRefund.getOrderId());
            // 查询退款成功的订单商品数量
            Integer orderRefundSuccessCount = orderRefundMapper.countRefundSuccessRefundCountByOrderId(orderRefund.getOrderId());
            // 更新订单,如果有订单项退款成功，则订单状态为部分退款成功，否则为退款失败
            if (orderRefundSuccessCount > 0) {
                order.setRefundStatus(RefundStatusEnum.PARTIAL_SUCCESS.value());
            } else {
                order.setRefundStatus(RefundStatusEnum.DISAGREE.value());
            }
            orders.add(order);

            // 更新订单项状态
            if (Objects.equals(orderRefund.getRefundType(), RefundType.ALL.value())) {
                orderItemService.updateRefundStatusByOrderId(orderRefund.getOrderId(), RefundStatusEnum.DISAGREE.value());
            } else {
                // 退款失败
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderItemId(orderRefund.getOrderItemId());
                orderItem.setRefundStatus(RefundStatusEnum.DISAGREE.value());
                orderItemService.update(orderItem);
            }

        }
        orderRefundMapper.updateBatchById(orderRefundList);
        orderService.updateRefundStatusBatchById(orders);
//        if(CollectionUtil.isNotEmpty(orderItems)) {
//            orderItemService.updateBatch(orderItems);
//        }
    }

    /**
     * 退单推送处理
     *
     * @param refundId     退款单号
     * @param returnStatus 退款状态，枚举申请退款 等待卖家同意（1）卖家同意退款/退货(2)买家已发货 等待卖家收货(3)退款完成（4）已拒绝（5）关闭（6）
     */
    @Override
    public void pushRefund(Long refundId, Integer returnStatus) {
        logger.info("退单推送中台开始 refundId:{}, returnStatus:{}", refundId, returnStatus);
        try {
            List<PushRefundDto> dtos = buildPushRefundParam(refundId, returnStatus);
            if (CollectionUtils.isEmpty(dtos)) {
                logger.info("退单推送中台参数异常 refundId:{}, returnStatus:{}}", refundId, returnStatus);
                return;
            }
            logger.info("推送中台退单参数 refunds:{}", JSON.toJSONString(dtos));

            stdOrderRefundTemplate.syncSend(dtos, RocketMqConstant.ORDER_REFUND_STD_TOPIC_TAG);
//            ServerResponseEntity<String> pushRefund = stdOrderFeignClient.pushRefund(dtos);
//            if (pushRefund.isSuccess()){
//                logger.info("退单推送中台成功 refundId:{}, returnStatus:{}", refundId, returnStatus);
//            } else {
//                logger.info("退单推送中台失败 refundId:{}, returnStatus:{}", refundId, returnStatus);
//            }
        } catch (Exception e) {
            logger.info("退单推送中台异常 refundId:{}, returnStatus:{}", refundId, returnStatus, e);
        }
    }


    private List<PushRefundDto> buildPushRefundParam(Long refundId, Integer returnStatus) {
        List<PushRefundDto> dtos = new ArrayList<>();
        OrderRefundVO refundVO = orderRefundMapper.getByRefundId(refundId);
        if (null == refundVO) {
            logger.info("退单不存在 refundId:{}, returnStatus:{}", refundId, returnStatus);
            return dtos;
        }
        Order order = orderService.getByOrderId(refundVO.getOrderId());
        if (null == order) {
            logger.info("订单不存在 refundId:{}, returnStatus:{}", refundId, returnStatus);
            return dtos;
        }
        ServerResponseEntity<UserApiVO> entity = userFeignClient.getInsiderUserData(order.getUserId());
        if (!entity.isSuccess() || null == entity.getData()) {
            logger.info("下单用户不存在 refundId:{}, returnStatus:{}", refundId, returnStatus);
            return dtos;
        }
        PushRefundDto dto = new PushRefundDto();
        String refundNumber = refundVO.getRefundNumber();
//        dto.setReturn_no(refundVO.getRefundId().toString());
        dto.setReturn_no(refundNumber);
        dto.setReturn_status(returnStatus);
//        dto.setOrder_no(order.getOrderId().toString());
        dto.setOrder_no(order.getOrderNumber());
        if (order.getStatus() == 2) {
            dto.setOrder_status("WAIT_SELLER_SEND_GOODS");
        } else if (order.getStatus() == 3) {
            dto.setOrder_status("WAIT_BUYER_CONFIRM_GOODS");
        } else if (order.getStatus() == 6) {
            dto.setOrder_status("TRADE_CLOSED");
        }
        dto.setRefund_type(refundVO.getApplyType());
        dto.setBuyer_nick(Optional.ofNullable(entity.getData().getNickName()).orElse("暂无"));
        dto.setBuyer_remark(refundVO.getBuyerDesc());
        dto.setBuyer_mobile(entity.getData().getUserMobile());
        dto.setReturn_reason(refundVO.getBuyerDesc());
        dto.setRefund_amount(new BigDecimal(refundVO.getRefundAmount()).divide(new BigDecimal(100), 2));
        dto.setOrder_credit(Optional.ofNullable(order.getOrderScore()).orElse(0L).intValue());
        dto.setReturn_credit(Optional.ofNullable(refundVO.getRefundScore()).orElse(0L).intValue());
        dto.setCreated(DateUtil.formatDateTime(refundVO.getCreateTime()));
        if (returnStatus == 3) {
            OrderRefundAddr refundAddr = orderRefundAddrService.getByRefundId(refundId);
            if (null == refundAddr) {
                logger.info("退单推送中台 refundId:{}, returnStatus:{}", refundId, returnStatus);
                return dtos;
            }
            dto.setCompany_name(refundAddr.getDeliveryName());
            dto.setLogistics_no(refundAddr.getDeliveryNo());
        }
        List<PushRefundDto.Item> itemList = new ArrayList<>();
        if (refundVO.getOrderItemId() == 0) {
            List<OrderItemVO> orderItemVOS = orderItemService.listOrderItemAndLangByOrderId(order.getOrderId());
            orderItemVOS.forEach(orderItemVO -> {
                PushRefundDto.Item item = new PushRefundDto.Item();
//                item.setReturn_no(refundVO.getRefundId().toString());
                item.setReturn_no(refundNumber);
                item.setSub_order_id(orderItemVO.getOrderItemId().toString());
                ServerResponseEntity<SkuCodeVO> skuData = skuFeignClient.getCodeBySkuId(orderItemVO.getSkuId());
                if (skuData.isSuccess() && null != skuData.getData()) {
                    item.setSku_id(skuData.getData().getSkuCode());
                    item.setSku(skuData.getData().getSkuCode());
                    item.setProductcode(skuData.getData().getPriceCode());
                }
                item.setQuantity(orderItemVO.getCount());
                item.setReturn_quantity(orderItemVO.getCount());
                item.setPrice(new BigDecimal(orderItemVO.getPrice()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                item.setTotal_fee(new BigDecimal(orderItemVO.getSpuTotalAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                item.setPayment(new BigDecimal(orderItemVO.getActualTotal()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                item.setDiscount_fee(new BigDecimal(orderItemVO.getShareReduce()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                item.setRefund_fee(new BigDecimal(orderItemVO.getActualTotal()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                itemList.add(item);
            });
        } else {
            OrderItemVO itemVO = orderItemService.getByOrderItemId(refundVO.getOrderItemId());
            PushRefundDto.Item item = new PushRefundDto.Item();
            item.setReturn_no(refundNumber);
            item.setSub_order_id(itemVO.getOrderItemId().toString());
            ServerResponseEntity<SkuCodeVO> skuData = skuFeignClient.getCodeBySkuId(itemVO.getSkuId());
            if (skuData.isSuccess() && null != skuData.getData()) {
                item.setSku_id(skuData.getData().getSkuCode());
                item.setSku(skuData.getData().getSkuCode());
                item.setProductcode(skuData.getData().getPriceCode());
            }
            item.setQuantity(itemVO.getCount());
            item.setReturn_quantity(refundVO.getRefundCount());
            item.setPrice(new BigDecimal(itemVO.getPrice()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            item.setTotal_fee(new BigDecimal(itemVO.getSpuTotalAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            item.setPayment(new BigDecimal(itemVO.getActualTotal()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            item.setDiscount_fee(new BigDecimal(itemVO.getShareReduce()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            item.setRefund_fee(new BigDecimal(itemVO.getActualTotal()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            itemList.add(item);
        }
        dto.setItems(itemList);
        dtos.add(dto);
        return dtos;
    }


    @Override
    public List<OrderRefundVO> listProximityRefundTimeOut(Date date, Date overDate) {
        return orderRefundMapper.listProximityRefundTimeOut(date, overDate);
    }

    @Override
    public com.mall4j.cloud.api.order.vo.OrderRefundVO getOrderRefundByRefundId(Long refundId) {
        OrderRefundVO orderRefundVO = getByRefundId(refundId);

        EsOrderBO esOrderBO = orderService.getEsOrder(orderRefundVO.getOrderId());
        orderRefundVO.setOrderNumber(esOrderBO.getOrderNumber());

        OrderRefundAddr orderRefundAddr = orderRefundAddrService.getByRefundId(refundId);

        com.mall4j.cloud.order.vo.OrderRefundAddrVO orderRefundAddrVO = new com.mall4j.cloud.order.vo.OrderRefundAddrVO();
        BeanUtil.copyProperties(orderRefundAddr,orderRefundAddrVO);

        orderRefundVO.setOrderRefundAddr(orderRefundAddrVO);

        return mapperFacade.map(orderRefundVO, com.mall4j.cloud.api.order.vo.OrderRefundVO.class);
    }

    @Override
    public com.mall4j.cloud.api.order.vo.OrderRefundVO getOrderRefundByRefundNumber(String refundNumber) {
        OrderRefundVO orderRefundVO = getByRefundNumber(refundNumber);

        EsOrderBO esOrderBO = orderService.getEsOrder(orderRefundVO.getOrderId());
        orderRefundVO.setOrderNumber(esOrderBO.getOrderNumber());

        OrderRefundAddr orderRefundAddr = orderRefundAddrService.getByRefundId(orderRefundVO.getRefundId());

        com.mall4j.cloud.order.vo.OrderRefundAddrVO orderRefundAddrVO = new com.mall4j.cloud.order.vo.OrderRefundAddrVO();
        BeanUtil.copyProperties(orderRefundAddr,orderRefundAddrVO);

        orderRefundVO.setOrderRefundAddr(orderRefundAddrVO);

        return mapperFacade.map(orderRefundVO, com.mall4j.cloud.api.order.vo.OrderRefundVO.class);
    }

    @Override
    public Integer countRefundSuccessRefundCountByOrderId(Long orderId) {
        return orderRefundMapper.countRefundSuccessRefundCountByOrderId(orderId);
    }

    @Override
    public Integer countReturnProcessingItemByOrderId(Long orderId) {
        return orderRefundMapper.countReturnProcessingItemByOrderId(orderId);
    }

    @DS("slave")
    @Override
    public Long countDistributionRefundAmount(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderRefundMapper.countDistributionRefundAmount(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Integer countDistributionRefundNum(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderRefundMapper.countDistributionRefundNum(distributionSalesStatDTO);
    }

    @Override
    public Long countStoreDistributionRefundAmount(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderRefundMapper.countStoreDistributionRefundAmount(distributionSalesStatDTO);
    }

    @Override
    public Integer countStoreDistributionRefundNum(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderRefundMapper.countStoreDistributionRefundNum(distributionSalesStatDTO);
    }

    @Override
    public void editPlatformRemark(Long refundId, String remark) {
        orderRefundMapper.editPlatformRemark(refundId, remark);
    }

    @Override
    public OrderRefundAddrVO getRefundAddr(Long refundId) {
        OrderRefundAddrVO orderRefundAddrVO = new OrderRefundAddrVO();
        OrderRefundAddr refundAddr = orderRefundAddrService.getByRefundId(refundId);
        BeanUtils.copyProperties(refundAddr, orderRefundAddrVO);
        return orderRefundAddrVO;
    }

    @Override
    public void syncComplaintOrderId(Long complaintOrderId, Long aftersaleId) {
        orderRefundMapper.syncComplaintOrderId(complaintOrderId,aftersaleId);
    }

    @Override
    public void syncComplaintOrderStatus(Long complaintOrderId) {
        orderRefundMapper.syncComplaintOrderStatus(complaintOrderId);
    }

    @Override
    public int offlineRefund(Long aftersaleId) {
        return orderRefundMapper.offlineRefund(aftersaleId);
    }

    @Override
    public int offlineRefundSuccess(Long aftersaleId) {
        return orderRefundMapper.offlineRefundSuccess(aftersaleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadCertificates(UploadCertificatesRequest uploadCertificatesRequest) {

        com.mall4j.cloud.api.order.vo.OrderRefundVO orderRefundVO =
                this.getOrderRefundByAftersaleId(uploadCertificatesRequest.getAftersale_id());
        if(orderRefundVO==null){
            Assert.faild("当前视频号售后单id不存在，请检查数据后再次上传");
        }

        EsOrderBO esOrderBO = orderService.getEsOrder(orderRefundVO.getOrderId());
        if(esOrderBO==null){
            Assert.faild("订单查询失败，请检查数据后再次上传");
        }

        OrderRefundOfflineCertificates orderRefundOfflineCertificates = new OrderRefundOfflineCertificates();
        orderRefundOfflineCertificates.setAftersaleId(uploadCertificatesRequest.getAftersale_id());

        String certificates = CollUtil.join(uploadCertificatesRequest.getCertificates(),",");
        orderRefundOfflineCertificates.setCertificates(certificates);
        orderRefundOfflineCertificates.setRefundDesc(uploadCertificatesRequest.getRefund_desc());

        orderRefundOfflineCertificates.setOrderId(esOrderBO.getOrderId());
        orderRefundOfflineCertificates.setOrderNumber(esOrderBO.getOrderNumber());

        orderRefundOfflineCertificates.setRefundId(orderRefundVO.getRefundId());
        orderRefundOfflineCertificates.setRefundNumber(orderRefundVO.getRefundNumber());
        orderRefundOfflineCertificatesMapper.insert(orderRefundOfflineCertificates);

        orderRefundMapper.offlineUploadCertificates(uploadCertificatesRequest.getAftersale_id());

        ServerResponseEntity<BaseResponse> baseResponse = liveStoreClient.uploadCertificates(uploadCertificatesRequest);
        if(baseResponse==null ||baseResponse.isFail() || baseResponse.getData().getErrcode()!=0 ){
            Assert.faild(StrUtil.format("上传线下退款凭证失败，请联系客服。"));
        }
    }

    @Override
    public void uploadrefundcertificate(EcuploadrefundcertificateRequest request) {
        com.mall4j.cloud.api.order.vo.OrderRefundVO orderRefundVO =
                this.getOrderRefundByAftersaleId(request.getAfter_sale_order_id());
        if(orderRefundVO==null){
            Assert.faild("当前视频号售后单id不存在，请检查数据后再次上传");
        }

        EsOrderBO esOrderBO = orderService.getEsOrder(orderRefundVO.getOrderId());
        if(esOrderBO==null){
            Assert.faild("订单查询失败，请检查数据后再次上传");
        }
        channlesFeignClient.uploadrefundcertificate(request);
    }

    @Override
    public void ecAftersaleReturnOverdue(Long aftersaleId) {
        log.info("视频号40待买家退货回调处理。aftersaleId:{}",aftersaleId);
        com.mall4j.cloud.api.order.vo.OrderRefundVO orderRefundVO = this.getOrderRefundByAftersaleId(aftersaleId);
        if(orderRefundVO==null){
            Assert.faild(StrUtil.format("售后单据在平台查询不到记录，aftersaleId:{}",aftersaleId));
        }
        //当前退单状态为待买家发货。
        if(orderRefundVO.getReturnMoneySts()==2){
            log.info("视频号40待买家退货回调。当前售后单状态已经为买家申请，不做处理。aftersaleId:{}",aftersaleId);
            return;
        }
        OrderRefundAddr dbAddr = orderRefundAddrService.getByRefundId(orderRefundVO.getRefundId());

        ServerResponseEntity<ShopRefundAddrVO> refundAddrResponse = shopRefundAddrFeignClient.getMainRefundAddr();
        if (!refundAddrResponse.isSuccess() || refundAddrResponse.getData() == null) {
            throw new LuckException("请设置退货物流信息");
        }
        ShopRefundAddrVO refundAddr = refundAddrResponse.getData();

        OrderRefundAddr orderRefundAddr = new OrderRefundAddr();
        orderRefundAddr.setShopId(orderRefundVO.getShopId());
        orderRefundAddr.setRefundId(orderRefundVO.getRefundId());
        orderRefundAddr.setUserId(orderRefundVO.getUserId());
        orderRefundAddr.setConsigneeName(refundAddr.getConsignee());
        orderRefundAddr.setConsigneeMobile(refundAddr.getMobile());
        orderRefundAddr.setConsigneePostCode(refundAddr.getPostCode());
        String addr = refundAddr.getProvince() + refundAddr.getCity() + refundAddr.getArea() + refundAddr.getAddr();
        orderRefundAddr.setConsigneeAddr(addr);
        //只有退货地址记录不存在才保存当前退单记录。
        if(dbAddr==null){
            orderRefundAddrService.save(orderRefundAddr);
        }

        int agreeStats = orderRefundMapper.ecAgreeReturns("", orderRefundVO.getRefundId());
        if (agreeStats == 0) {
            throw new LuckException("订单退款状态已发生改变，请勿重复操作");
        }
        // TODO 消息推送--同意退款
//        List<SendNotifyBO> notifyBOList = orderService.listByOrderIds(Collections.singletonList(orderRefundVo.getOrderId()));
//        SendNotifyBO sendNotifyBO = notifyBOList.get(0);
//        sendNotifyBO.setSendType(SendTypeEnum.AGREE_REFUND.getValue());
//        sendNotifyBO.setPrice(PriceUtil.toDecimalPrice(orderRefundVo.getRefundAmount()).toString());
//        sendNotifyToUserTemplate.syncSend(notifyBOList);
        pushRefund(orderRefundVO.getRefundId(), 2);
    }

    @Override
    public void updateJointVentureRefundOrder(List<Long> refundIds, Integer jointVentureRefundStatus) {
        orderRefundMapper.updateJointVentureRefundOrder(refundIds,jointVentureRefundStatus);
    }
}
