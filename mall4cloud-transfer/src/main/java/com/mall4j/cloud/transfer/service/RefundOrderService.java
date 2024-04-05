package com.mall4j.cloud.transfer.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.transfer.mapper.*;
import com.mall4j.cloud.transfer.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 退款订单service
 *
 * @luzhengxiang
 * @create 2022-04-04 11:42 PM
 **/
@Slf4j
@Service
public class RefundOrderService {
    @Autowired
    MallSalesOrderRefMapper mallSalesOrderRefMapper;
    @Autowired
    MallSalesOrderRefMappingMapper mallSalesOrderRefMappingMapper;
    @Autowired
    MallSalesOrderRtMapper mallSalesOrderRtMapper;
    @Autowired
    MallSalesOrderRtMappingMapper mallSalesOrderRtMappingMapper;
    @Autowired
    OrderRefundMapper orderRefundMapper;
    @Autowired
    MallSalesOrderDtlMapper mallSalesOrderDtlMapper;
    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    RefundInfoMapper refundInfoMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderSettlementMapper orderSettlementMapper;
    @Autowired
    SegmentFeignClient segmentFeignClient;


    int currentPage = 1;
    int pageSize = 2000;

    /**
     * 退款单数据同步
     * order_refund
     * order_refund_addr
     * order_refund_settlement
     * <p>
     * refund_info
     */
    @Async
    public void refundOrderTransfer() {
        log.info("退款订单同步执行开始。");
        long startTime = System.currentTimeMillis();
        //分页处理，每次处理2000条数据批量保存。
        PageHelper.startPage(currentPage, pageSize);
        List<MallSalesOrderRef> mallSalesOrderRefs = mallSalesOrderRefMapper.list();
        PageInfo<MallSalesOrderRef> pageInfo = new PageInfo(mallSalesOrderRefs);
        createRefundOrder(mallSalesOrderRefs, currentPage);

        int totalPage = pageInfo.getPages();
        int totalCount = (int) pageInfo.getTotal();
        for (int i = 2; i <= totalPage; i++) {
            PageHelper.startPage(i, pageSize);
            List<MallSalesOrderRef> newMallSalesOrderRefs = mallSalesOrderRefMapper.list();
            createRefundOrder(newMallSalesOrderRefs, i);
        }
        log.info("退款订单同步执行结束，累计耗时:{}ms", System.currentTimeMillis() - startTime);

    }

    private void createRefundOrder(List<MallSalesOrderRef> mallSalesOrderRefs, int currentPage) {

        List<OrderRefund> orderRefunds = new ArrayList<>();
        for (MallSalesOrderRef mallSalesOrderRef : mallSalesOrderRefs) {
            List<MallSalesOrderRefMapping> refMappings = mallSalesOrderRefMappingMapper.listByRefundId(mallSalesOrderRef.getId());
            if(refMappings==null || refMappings.size()==0){
                log.error("退款单id:{}。退款单明细为空，数据异常不执行同步。 ",mallSalesOrderRef.getId());
                continue;
            }

            Long orderItemId;
            /**
             * 是否整单退款
             *  驿客的状态  IsSingleRefund 是否整单退款 0 否  1 是
             *  如果为整单退款，只创建一条 OrderRefund 记录
             *  如果不是，分别创建orderRefund记录
             */
            if (StrUtil.equals(mallSalesOrderRef.getIssinglerefund(), "1")) {
                zhengdan(mallSalesOrderRef, refMappings, orderRefunds);
            } else {
                chaidan(mallSalesOrderRef, refMappings, orderRefunds);
            }

        }
        if (CollUtil.isNotEmpty(orderRefunds)) {
            orderRefundMapper.batchSave(orderRefunds);
        }
        log.info("第{}页数据插入执行结束。插入退款订单条数:{}", currentPage, orderRefunds.size());
    }


    private void zhengdan(MallSalesOrderRef mallSalesOrderRef, List<MallSalesOrderRefMapping> refMappings, List<OrderRefund> orderRefunds) {
        Order order = orderMapper.getByOrderId2(mallSalesOrderRef.getOrderid());
        if (order == null) {
            log.error("mallSalesOrderRef.orderid:{}在订单表中不存在，不执行当前记录的退款记录同步。", mallSalesOrderRef.getOrderid());
            return;
        }



        MallSalesOrderRt mallSalesOrderRt = mallSalesOrderRtMapper.getById(mallSalesOrderRef.getReturnid());
        OrderRefund orderRefund = new OrderRefund();

        List<MallSalesOrderRtMapping> mallSalesOrderRtMapping = null;
        if (mallSalesOrderRt != null) {
            orderRefund.setApplyType(2);//申请类型:1,仅退款,2退款退货
            mallSalesOrderRtMapping = mallSalesOrderRtMappingMapper.listByReturnId(mallSalesOrderRef.getReturnid());
            if(mallSalesOrderRtMapping==null || mallSalesOrderRtMapping.size()==0){
                log.error("退款记录id：{}, 退货单明细为空，数据缺失，当前记录不执行同步。",mallSalesOrderRef.getId());
                return;
            }
        } else {
            orderRefund.setApplyType(1);//申请类型:1,仅退款,2退款退货
        }

        ServerResponseEntity<Long> responseEntity = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_REFUND);
        Long refundId = responseEntity.getData();
        orderRefund.setRefundId(refundId);//退款单id
        orderRefund.setRefundNumber(mallSalesOrderRef.getRefundcode());
        orderRefund.setCreateTime(mallSalesOrderRef.getCreatedate());
        orderRefund.setUpdateTime(mallSalesOrderRef.getLastmodifieddate());
        orderRefund.setShopId(order.getStoreId());//店铺id
        orderRefund.setUserId(mallSalesOrderRef.getBuyerid());//买家ID
        orderRefund.setOrderId(mallSalesOrderRef.getOrderid());//订单id
        //退款单明细
        orderRefund.setOrderItemId(0L);//订单项ID(0:为全部订单项)
        if (mallSalesOrderRt != null) {
            orderRefund.setRefundCount(mallSalesOrderRt.getReturnqty());//退货数量(0:为全部订单项)
        } else {
            orderRefund.setRefundCount(0);
        }

        orderRefund.setRefundScore(0L);//退还积分
        BigDecimal refundAmount = new BigDecimal(mallSalesOrderRef.getAmountmenory()).multiply(new BigDecimal(100));
        orderRefund.setRefundAmount(refundAmount.longValue());//退款金额
        orderRefund.setPlatformRefundCommission(0L);//平台佣金退款金额
        orderRefund.setPlatformRefundAmount(0L);//平台退款金额（退款时将这部分钱退回给平台，所以商家要扣除从平台这里获取的金额）
        orderRefund.setDistributionTotalAmount(0L);//退款单总分销金额
        /**
         * 是否整单退款
         *  驿客的状态  IsSingleRefund 是否整单退款 0 否  1 是
         */
        orderRefund.setRefundType(1);//退款单类型（1:整单退款,2:单个物品退款）


        /**
         * 驿客没有这个属性
         * 只在退款明细表 mall_sales_order_ref_mapping PackageNo  发货包裹编号（已发货商品直接退款，未发货商品该值为空）
         * 有快递单号就是收到 没有就是未收到
         */
        orderRefund.setIsReceived(StrUtil.isEmptyIfStr(refMappings.get(0).getPackageno()) == true ? 1 : 0);//是否接收到商品(1:已收到,0:未收到)

        /**
         * 退款取消类型
         * 驿客 状态（0：全部；1：客户取消；2：客服拒绝）
         */
        if (mallSalesOrderRef.getRefundcancellationtype() != null && mallSalesOrderRef.getRefundcancellationtype() != 0) {
            orderRefund.setCloseType(mallSalesOrderRef.getRefundcancellationtype());//退款关闭原因(1.买家撤销退款 2.卖家拒绝退款 3.退款申请超时被系统关闭)
        } else {
            orderRefund.setCloseType(3);
        }


        /**
         * mall_sales_order_ref ProcessStatus 退款处理状态（1:处理中，2:处理完成）
         * mall_sales_order_ref refundStatus 0等待商家退款 1商家已经退款成功 2退款取消 3驳回
         *
         * mall_sales_order_rt returnStatus 0等待商家处理  1商家同意退货 2商家不同意退货 3买家发货 4商家确认收货 5商家未收货拒绝退货 6商家退款 7关闭
         *
         * order_refund.return_money_sts //处理退款状态:(1.买家申请 2.卖家接受 3.买家发货 4.卖家收货 5.退款成功  -1.退款关闭)详情见ReturnMoneyStsType
         */
        if (mallSalesOrderRef.getRefundstatus() == 0 || mallSalesOrderRef.getRefundstatus() == 1) {
            if (mallSalesOrderRef != null && mallSalesOrderRef.getRefundstatus() == 0) {
                orderRefund.setReturnMoneySts(1);
            } else if (mallSalesOrderRef != null && mallSalesOrderRef.getRefundstatus() == 1) {
                orderRefund.setReturnMoneySts(2);
            } else if (mallSalesOrderRef != null && mallSalesOrderRef.getRefundstatus() == 3) {
                orderRefund.setReturnMoneySts(3);
            } else if (mallSalesOrderRef != null && mallSalesOrderRef.getRefundstatus() == 4) {
                orderRefund.setReturnMoneySts(4);
            } else if (mallSalesOrderRef != null && mallSalesOrderRef.getRefundstatus() == 6) {
                orderRefund.setReturnMoneySts(5);
            } else {
                orderRefund.setReturnMoneySts(-1);
            }
        }
        if (mallSalesOrderRef.getRefundstatus() == 2) {
            orderRefund.setReturnMoneySts(5);
        }
        if (mallSalesOrderRef.getRefundstatus() == 3) {
            orderRefund.setReturnMoneySts(-1);
        }

        /**
         * 跟我们平台的对应不上，我统一设置为 6 其他
         */
        orderRefund.setBuyerReason(6);//申请原因(具体见BuyerReasonType
        orderRefund.setBuyerDesc(mallSalesOrderRef.getRefundremark());//申请说明

        /**
         * todo 查询用户手机号
         * 生产环境才可以放开查询
         */
        orderRefund.setBuyerMobile("");//联系方式（退款时留下的手机号码）
//                ServerResponseEntity<UserApiVO> userResponse = userFeignClient.getUserAndOpenIdsByUserId(orderRefund.getUserId());
//                if(userResponse.isSuccess()){
//
//                }

        /**
         * 没有找到这个字段
         */
        orderRefund.setImgUrls("");//文件凭证(逗号隔开)
//            orderRefund.setOverTime();//超时时间（超过该时间不处理，系统将自动处理）（保留字段）
        orderRefund.setRejectMessage("");//拒绝原因
        orderRefund.setSellerMsg("");//卖家备注


//            orderRefund.setHandelTime();//受理时间
//            orderRefund.setDeliveryTime();//发货时间
//            orderRefund.setReceiveTime();//收货时间
//            orderRefund.setCloseTime();//关闭时间
//            orderRefund.setDecisionTime();//确定时间(确定退款时间)
//            orderRefund.setRefundTime();//退款时间

        orderRefunds.add(orderRefund);
        if (orderRefund.getReturnMoneySts() == 5) {
            RefundInfo refundInfo = new RefundInfo();
            refundInfo.setRefundId(refundId);//id
            refundInfo.setCreateTime(mallSalesOrderRef.getCreatedate());//创建时间
            refundInfo.setUpdateTime(mallSalesOrderRef.getLastmodifieddate());//更新时间
            refundInfo.setOrderId(orderRefund.getOrderId());//关联订单id
            //在驿客存在一笔订单多次退款，我们这里的订单结算
            OrderSettlement orderSettlement = orderSettlementMapper.getByOrderId2(orderRefund.getOrderId());
            if (orderSettlement != null) {
                refundInfo.setPayId(orderSettlement.getPayId());//关联支付单id
            } else {
                log.info("订单id:{}，查询订单结算记录不存在，不保存当前记录的支付单id", refundInfo.getOrderId());
            }
            refundInfo.setUserId(orderRefund.getUserId());//用户id
            refundInfo.setRefundStatus(2);//退款状态
            refundInfo.setRefundAmount(orderRefund.getRefundAmount());//退款金额
            refundInfo.setPayType(8);//支付方式
            refundInfoMapper.save(refundInfo);
//                    refundInfo.setCallbackContent();//回调内容
//                    refundInfo.setCallbackTime();//回调时间
        }
    }


    private void chaidan(MallSalesOrderRef mallSalesOrderRef, List<MallSalesOrderRefMapping> refMappings, List<OrderRefund> orderRefunds) {
        Order order = orderMapper.getByOrderId2(mallSalesOrderRef.getOrderid());
        if (order == null) {
            log.error("mallSalesOrderRef.orderid:{}在订单表中不存在，不执行当前记录的退款记录同步。", mallSalesOrderRef.getOrderid());
            return;
        }

        MallSalesOrderRt mallSalesOrderRt = mallSalesOrderRtMapper.getById(mallSalesOrderRef.getReturnid());

        List<MallSalesOrderRtMapping> mallSalesOrderRtMapping = null;
        if (mallSalesOrderRt != null) {
            mallSalesOrderRtMapping = mallSalesOrderRtMappingMapper.listByReturnId(mallSalesOrderRef.getReturnid());
        }

        int i = 1;
        //拆单 根据退款单明细 创建我们的退款记录
        for (MallSalesOrderRefMapping refMapping : refMappings) {
            OrderRefund orderRefund = new OrderRefund();
            ServerResponseEntity<Long> responseEntity = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_REFUND);
            Long refundId = responseEntity.getData();
            orderRefund.setRefundId(refundId);//退款单id
            orderRefund.setRefundNumber(mallSalesOrderRef.getRefundcode() + "-" + i);
            orderRefund.setCreateTime(mallSalesOrderRef.getCreatedate());
            orderRefund.setUpdateTime(mallSalesOrderRef.getLastmodifieddate());
            orderRefund.setShopId(order.getStoreId());//店铺id
            orderRefund.setUserId(mallSalesOrderRef.getBuyerid());//买家ID
            orderRefund.setOrderId(mallSalesOrderRef.getOrderid());//订单id
            //退款单明细
            orderRefund.setOrderItemId(refMapping.getOrderdtlid());//订单项ID(0:为全部订单项)
            orderRefund.setRefundCount(refMappings.size());//退货数量(0:为全部订单项)
            orderRefund.setRefundScore(0L);//退还积分
            BigDecimal refundAmount = new BigDecimal(refMapping.getRefundmoney()).multiply(new BigDecimal(100));
            orderRefund.setRefundAmount(refundAmount.longValue());//退款金额
            orderRefund.setPlatformRefundCommission(0L);//平台佣金退款金额
            orderRefund.setPlatformRefundAmount(0L);//平台退款金额（退款时将这部分钱退回给平台，所以商家要扣除从平台这里获取的金额）
            orderRefund.setDistributionTotalAmount(0L);//退款单总分销金额
            /**
             * 是否整单退款
             *  驿客的状态  IsSingleRefund 是否整单退款 0 否  1 是
             */
            orderRefund.setRefundType(2);//退款单类型（1:整单退款,2:单个物品退款）

            /**
             * 我们存在主表中。
             * 驿客是存在明细表中。mall_sales_order_ref_mapping RefType 退款类型，0未发货退款，1已发货退款（pc端已发货直接退款）
             */
            orderRefund.setApplyType(refMapping.getReftype() + 1);//申请类型:1,仅退款,2退款退货
            /**
             * 驿客没有这个属性
             * 只在退款明细表 mall_sales_order_ref_mapping PackageNo  发货包裹编号（已发货商品直接退款，未发货商品该值为空）
             * 有快递单号就是收到 没有就是未收到
             */
            orderRefund.setIsReceived(StrUtil.isEmptyIfStr(refMapping.getPackageno()) == true ? 1 : 0);//是否接收到商品(1:已收到,0:未收到)

            /**
             * 退款取消类型
             * 驿客 状态（0：全部；1：客户取消；2：客服拒绝）
             */
            if (mallSalesOrderRef.getRefundcancellationtype() != null && mallSalesOrderRef.getRefundcancellationtype() != 0) {
                orderRefund.setCloseType(mallSalesOrderRef.getRefundcancellationtype());//退款关闭原因(1.买家撤销退款 2.卖家拒绝退款 3.退款申请超时被系统关闭)
            } else {
                orderRefund.setCloseType(3);
            }

            /**
             * todo
             * mall_sales_order_ref ProcessStatus 退款处理状态（1:处理中，2:处理完成）
             * mall_sales_order_ref refundStatus 0等待商家退款 1商家已经退款成功 2退款取消 3驳回
             *
             * mall_sales_order_rt returnStatus 0等待商家处理  1商家同意退货 2商家不同意退货 3买家发货 4商家确认收货 5商家未收货拒绝退货 6商家退款 7关闭
             *
             */
            if (mallSalesOrderRef.getRefundstatus() == 0 || mallSalesOrderRef.getRefundstatus() == 1) {
                if (mallSalesOrderRef != null && mallSalesOrderRef.getRefundstatus() == 0) {
                    orderRefund.setReturnMoneySts(1);
                } else if (mallSalesOrderRef != null && mallSalesOrderRef.getRefundstatus() == 1) {
                    orderRefund.setReturnMoneySts(2);
                } else if (mallSalesOrderRef != null && mallSalesOrderRef.getRefundstatus() == 3) {
                    orderRefund.setReturnMoneySts(3);
                } else if (mallSalesOrderRef != null && mallSalesOrderRef.getRefundstatus() == 4) {
                    orderRefund.setReturnMoneySts(4);
                } else if (mallSalesOrderRef != null && mallSalesOrderRef.getRefundstatus() == 6) {
                    orderRefund.setReturnMoneySts(5);
                } else {
                    orderRefund.setReturnMoneySts(-1);
                }
            }
            if (mallSalesOrderRef.getRefundstatus() == 2) {
                orderRefund.setReturnMoneySts(5);
            }
            if (mallSalesOrderRef.getRefundstatus() == 3) {
                orderRefund.setReturnMoneySts(-1);
            }
            /**
             * 跟我们平台的对应不上，我统一设置为 6 其他
             */
            orderRefund.setBuyerReason(6);//申请原因(具体见BuyerReasonType
            orderRefund.setBuyerDesc(mallSalesOrderRef.getRefundremark());//申请说明

            /**
             * todo 查询用户手机号
             * 生产环境才可以放开查询
             */
            orderRefund.setBuyerMobile("");//联系方式（退款时留下的手机号码）
//                ServerResponseEntity<UserApiVO> userResponse = userFeignClient.getUserAndOpenIdsByUserId(orderRefund.getUserId());
//                if(userResponse.isSuccess()){
//
//                }

            /**
             * 没有找到这个字段
             */
            orderRefund.setImgUrls("");//文件凭证(逗号隔开)
//            orderRefund.setOverTime();//超时时间（超过该时间不处理，系统将自动处理）（保留字段）
            orderRefund.setRejectMessage("");//拒绝原因
            orderRefund.setSellerMsg("");//卖家备注


//            orderRefund.setHandelTime();//受理时间
//            orderRefund.setDeliveryTime();//发货时间
//            orderRefund.setReceiveTime();//收货时间
//            orderRefund.setCloseTime();//关闭时间
//            orderRefund.setDecisionTime();//确定时间(确定退款时间)
//            orderRefund.setRefundTime();//退款时间

            orderRefunds.add(orderRefund);
            if (orderRefund.getReturnMoneySts() == 5) {
                RefundInfo refundInfo = new RefundInfo();
                refundInfo.setRefundId(refundId);//id
                refundInfo.setCreateTime(mallSalesOrderRef.getCreatedate());//创建时间
                refundInfo.setUpdateTime(mallSalesOrderRef.getLastmodifieddate());//更新时间
                refundInfo.setOrderId(orderRefund.getOrderId());//关联订单id
                //在驿客存在一笔订单多次退款，我们这里的订单结算
                OrderSettlement orderSettlement = orderSettlementMapper.getByOrderId2(orderRefund.getOrderId());
                if (orderSettlement != null) {
                    refundInfo.setPayId(orderSettlement.getPayId());//关联支付单id
                } else {
                    log.info("订单id:{}，查询订单结算记录不存在，不保存当前记录的支付单id", refundInfo.getOrderId());
                }
                refundInfo.setUserId(orderRefund.getUserId());//用户id
                refundInfo.setRefundStatus(2);//退款状态
                refundInfo.setRefundAmount(orderRefund.getRefundAmount());//退款金额
                refundInfo.setPayType(8);//支付方式
                refundInfoMapper.save(refundInfo);
//                    refundInfo.setCallbackContent();//回调内容
//                    refundInfo.setCallbackTime();//回调时间
            }
            i++;
        }
    }


    @Async
    public void refundOrderTransfer2() {
        log.info("退款订单2同步执行开始。");
        long startTime = System.currentTimeMillis();

        List<MallSalesOrderRt> list = mallSalesOrderRtMapper.list2();

        for (MallSalesOrderRt mallSalesOrderRt : list) {

            Order order = orderMapper.getByOrderId2(mallSalesOrderRt.getOrderid());
            if (order == null) {
                log.error("mallSalesOrderRt.orderid:{}在订单表中不存在，不执行当前记录的退款记录同步。", mallSalesOrderRt.getOrderid());
                return;
            }
            //是否整单退款 0 否  1 是
            if ("1".equals(mallSalesOrderRt.getIssinglereturn())) {
                zhendan2(mallSalesOrderRt, order);
            } else {
                chaidan2(mallSalesOrderRt, order);
            }

        }

//        //分页处理，每次处理2000条数据批量保存。
//        PageHelper.startPage(currentPage, pageSize);
//        List<MallSalesOrderRef> mallSalesOrderRefs = mallSalesOrderRefMapper.list();
//        PageInfo<MallSalesOrderRef> pageInfo = new PageInfo(mallSalesOrderRefs);
//        createRefundOrder(mallSalesOrderRefs, currentPage);
//
//        int totalPage = pageInfo.getPages();
//        int totalCount = (int) pageInfo.getTotal();
//        for (int i = 2; i <= totalPage; i++) {
//            PageHelper.startPage(i, pageSize);
//            List<MallSalesOrderRef> newMallSalesOrderRefs = mallSalesOrderRefMapper.list();
//            createRefundOrder(newMallSalesOrderRefs, i);
//        }
        log.info("退款订单2同步执行结束，累计耗时:{}ms", System.currentTimeMillis() - startTime);

    }

    private void chaidan2(MallSalesOrderRt mallSalesOrderRt, Order order) {
        List<MallSalesOrderRtMapping> mallSalesOrderRtMapping = mallSalesOrderRtMappingMapper.listByReturnId(mallSalesOrderRt.getId());
        int i = 1;
        for (MallSalesOrderRtMapping rtMapping : mallSalesOrderRtMapping) {
            MallSalesOrderDtl mallSalesOrderDtl = mallSalesOrderDtlMapper.getById(rtMapping.getOrderdtlid());

            ServerResponseEntity<Long> responseEntity = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_REFUND);
            Long refundId = responseEntity.getData();
            //整单 根据退款单明细 创建我们的退款记录
            OrderRefund orderRefund = new OrderRefund();

            orderRefund.setRefundId(refundId);//退款单id
            orderRefund.setRefundNumber(mallSalesOrderRt.getReturncode() + "-" + i);
            orderRefund.setCreateTime(mallSalesOrderRt.getCreatedate());
            orderRefund.setUpdateTime(mallSalesOrderRt.getLastmodifieddate());
            orderRefund.setShopId(order.getStoreId());//店铺id
            orderRefund.setUserId(mallSalesOrderRt.getBuyerid());//买家ID
            orderRefund.setOrderId(mallSalesOrderRt.getOrderid());//订单id
            //退款单明细
            orderRefund.setOrderItemId(0L);//订单项ID(0:为全部订单项)
            orderRefund.setRefundCount(rtMapping.getReturnqty());//退货数量(0:为全部订单项)
            orderRefund.setRefundScore(0L);//退还积分

            BigDecimal returnMoney = new BigDecimal(mallSalesOrderDtl.getReturnmoney()).multiply(new BigDecimal(100)); //获取订单明细金额作为退款金额
            orderRefund.setRefundAmount(returnMoney.longValue());//退款金额
            orderRefund.setPlatformRefundCommission(0L);//平台佣金退款金额
            orderRefund.setPlatformRefundAmount(0L);//平台退款金额（退款时将这部分钱退回给平台，所以商家要扣除从平台这里获取的金额）
            orderRefund.setDistributionTotalAmount(0L);//退款单总分销金额
            /**
             * 是否整单退款
             *  驿客的状态  IsSingleRefund 是否整单退款 0 否  1 是
             */
            orderRefund.setRefundType(2);//退款单类型（1:整单退款,2:单个物品退款）

            /**
             * 我们存在主表中。
             * 驿客是存在明细表中。mall_sales_order_ref_mapping RefType 退款类型，0未发货退款，1已发货退款（pc端已发货直接退款）
             */
            orderRefund.setApplyType(2);//申请类型:1,仅退款,2退款退货
            /**
             * 驿客没有这个属性
             * 只在退款明细表 mall_sales_order_ref_mapping PackageNo  发货包裹编号（已发货商品直接退款，未发货商品该值为空）
             * 有快递单号就是收到 没有就是未收到
             */
//            orderRefund.setIsReceived(StrUtil.isEmptyIfStr(refMapping.getPackageno()) == true ? 1 : 0);//是否接收到商品(1:已收到,0:未收到)

            /**
             * 退款取消类型
             * 驿客 状态（0：全部；1：客户取消；2：客服拒绝）
             */
//            if (mallSalesOrderRef.getRefundcancellationtype() != null && mallSalesOrderRef.getRefundcancellationtype() != 0) {
//                orderRefund.setCloseType(mallSalesOrderRef.getRefundcancellationtype());//退款关闭原因(1.买家撤销退款 2.卖家拒绝退款 3.退款申请超时被系统关闭)
//            } else {
//                orderRefund.setCloseType(3);
//            }

            /**
             * todo
             * mall_sales_order_ref ProcessStatus 退款处理状态（1:处理中，2:处理完成）
             * mall_sales_order_ref refundStatus 1等待商家退款 2商家已经退款成功 3退款取消 4驳回
             *
             * mall_sales_order_rt returnStatus 0等待商家处理  1商家同意退货 2商家不同意退货 3买家发货 4商家确认收货 5商家未收货拒绝退货 6商家退款 7关闭
             *
             */
            if (mallSalesOrderRt.getReturnstatus() == 0) {
                orderRefund.setReturnMoneySts(1);
            } else if (mallSalesOrderRt.getReturnstatus() == 1) {
                orderRefund.setReturnMoneySts(2);
            } else if (mallSalesOrderRt.getReturnstatus() == 3) {
                orderRefund.setReturnMoneySts(3);
            } else {
                orderRefund.setReturnMoneySts(-1);
            }

            /**
             * 跟我们平台的对应不上，我统一设置为 6 其他
             */
            orderRefund.setBuyerReason(6);//申请原因(具体见BuyerReasonType
            orderRefund.setBuyerDesc(mallSalesOrderRt.getReturnreason());//申请说明

            /**
             * todo 查询用户手机号
             * 生产环境才可以放开查询
             */
            orderRefund.setBuyerMobile("");//联系方式（退款时留下的手机号码）
//                ServerResponseEntity<UserApiVO> userResponse = userFeignClient.getUserAndOpenIdsByUserId(orderRefund.getUserId());
//                if(userResponse.isSuccess()){
//
//                }

            /**
             * 没有找到这个字段
             */
            orderRefund.setImgUrls("");//文件凭证(逗号隔开)
//            orderRefund.setOverTime();//超时时间（超过该时间不处理，系统将自动处理）（保留字段）
            orderRefund.setRejectMessage("");//拒绝原因
            orderRefund.setSellerMsg("");//卖家备注


//            orderRefund.setHandelTime();//受理时间
//            orderRefund.setDeliveryTime();//发货时间
//            orderRefund.setReceiveTime();//收货时间
//            orderRefund.setCloseTime();//关闭时间
//            orderRefund.setDecisionTime();//确定时间(确定退款时间)
//            orderRefund.setRefundTime();//退款时间

            orderRefundMapper.save2(orderRefund);
            if (orderRefund.getReturnMoneySts() == 5) {
                RefundInfo refundInfo = new RefundInfo();
                refundInfo.setRefundId(refundId);//id
                refundInfo.setCreateTime(mallSalesOrderRt.getCreatedate());//创建时间
                refundInfo.setUpdateTime(mallSalesOrderRt.getLastmodifieddate());//更新时间
                refundInfo.setOrderId(orderRefund.getOrderId());//关联订单id
                //在驿客存在一笔订单多次退款，我们这里的订单结算
                OrderSettlement orderSettlement = orderSettlementMapper.getByOrderId2(orderRefund.getOrderId());
                if (orderSettlement != null) {
                    refundInfo.setPayId(orderSettlement.getPayId());//关联支付单id
                } else {
                    log.info("订单id:{}，查询订单结算记录不存在，不保存当前记录的支付单id", refundInfo.getOrderId());
                }
                refundInfo.setUserId(orderRefund.getUserId());//用户id
                refundInfo.setRefundStatus(2);//退款状态
                refundInfo.setRefundAmount(orderRefund.getRefundAmount());//退款金额
                refundInfo.setPayType(8);//支付方式
                refundInfoMapper.save2(refundInfo);
            }
            i++;
        }

    }

    private void zhendan2(MallSalesOrderRt mallSalesOrderRt, Order order) {
        List<MallSalesOrderRtMapping> mallSalesOrderRtMapping = mallSalesOrderRtMappingMapper.listByReturnId(mallSalesOrderRt.getId());


        ServerResponseEntity<Long> responseEntity = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_REFUND);
        Long refundId = responseEntity.getData();

        //整单 根据退款单明细 创建我们的退款记录
        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setRefundId(refundId);//退款单id
        orderRefund.setRefundNumber(mallSalesOrderRt.getReturncode());
        orderRefund.setCreateTime(mallSalesOrderRt.getCreatedate());
        orderRefund.setUpdateTime(mallSalesOrderRt.getLastmodifieddate());
        orderRefund.setShopId(order.getStoreId());//店铺id
        orderRefund.setUserId(mallSalesOrderRt.getBuyerid());//买家ID
        orderRefund.setOrderId(mallSalesOrderRt.getOrderid());//订单id
        //退款单明细
        orderRefund.setOrderItemId(0L);//订单项ID(0:为全部订单项)
        orderRefund.setRefundCount(mallSalesOrderRt.getReturnqty());//退货数量(0:为全部订单项)
        orderRefund.setRefundScore(0L);//退还积分
        BigDecimal returnMoney = new BigDecimal(mallSalesOrderRt.getReturnmoney()).multiply(new BigDecimal(100));
        orderRefund.setRefundAmount(returnMoney.longValue());//退款金额
        orderRefund.setPlatformRefundCommission(0L);//平台佣金退款金额
        orderRefund.setPlatformRefundAmount(0L);//平台退款金额（退款时将这部分钱退回给平台，所以商家要扣除从平台这里获取的金额）
        orderRefund.setDistributionTotalAmount(0L);//退款单总分销金额
        /**
         * 是否整单退款
         *  驿客的状态  IsSingleRefund 是否整单退款 0 否  1 是
         */
        orderRefund.setRefundType(1);//退款单类型（1:整单退款,2:单个物品退款）

        /**
         * 我们存在主表中。
         * 驿客是存在明细表中。mall_sales_order_ref_mapping RefType 退款类型，0未发货退款，1已发货退款（pc端已发货直接退款）
         */
        orderRefund.setApplyType(2);//申请类型:1,仅退款,2退款退货
        /**
         * 驿客没有这个属性
         * 只在退款明细表 mall_sales_order_ref_mapping PackageNo  发货包裹编号（已发货商品直接退款，未发货商品该值为空）
         * 有快递单号就是收到 没有就是未收到
         */
//            orderRefund.setIsReceived(StrUtil.isEmptyIfStr(refMapping.getPackageno()) == true ? 1 : 0);//是否接收到商品(1:已收到,0:未收到)

        /**
         * 退款取消类型
         * 驿客 状态（0：全部；1：客户取消；2：客服拒绝）
         */
//            if (mallSalesOrderRef.getRefundcancellationtype() != null && mallSalesOrderRef.getRefundcancellationtype() != 0) {
//                orderRefund.setCloseType(mallSalesOrderRef.getRefundcancellationtype());//退款关闭原因(1.买家撤销退款 2.卖家拒绝退款 3.退款申请超时被系统关闭)
//            } else {
//                orderRefund.setCloseType(3);
//            }

        /**
         * todo
         * mall_sales_order_ref ProcessStatus 退款处理状态（1:处理中，2:处理完成）
         * mall_sales_order_ref refundStatus 1等待商家退款 2商家已经退款成功 3退款取消 4驳回
         *
         * mall_sales_order_rt returnStatus 0等待商家处理  1商家同意退货 2商家不同意退货 3买家发货 4商家确认收货 5商家未收货拒绝退货 6商家退款 7关闭
         *
         */
        if (mallSalesOrderRt.getReturnstatus() == 0) {
            orderRefund.setReturnMoneySts(1);
        } else if (mallSalesOrderRt.getReturnstatus() == 1) {
            orderRefund.setReturnMoneySts(2);
        } else if (mallSalesOrderRt.getReturnstatus() == 3) {
            orderRefund.setReturnMoneySts(3);
        } else {
            orderRefund.setReturnMoneySts(-1);
        }

        /**
         * 跟我们平台的对应不上，我统一设置为 6 其他
         */
        orderRefund.setBuyerReason(6);//申请原因(具体见BuyerReasonType
        orderRefund.setBuyerDesc(mallSalesOrderRt.getReturnreason());//申请说明

        /**
         * todo 查询用户手机号
         * 生产环境才可以放开查询
         */
        orderRefund.setBuyerMobile("");//联系方式（退款时留下的手机号码）
//                ServerResponseEntity<UserApiVO> userResponse = userFeignClient.getUserAndOpenIdsByUserId(orderRefund.getUserId());
//                if(userResponse.isSuccess()){
//
//                }

        /**
         * 没有找到这个字段
         */
        orderRefund.setImgUrls("");//文件凭证(逗号隔开)
//            orderRefund.setOverTime();//超时时间（超过该时间不处理，系统将自动处理）（保留字段）
        orderRefund.setRejectMessage("");//拒绝原因
        orderRefund.setSellerMsg("");//卖家备注


//            orderRefund.setHandelTime();//受理时间
//            orderRefund.setDeliveryTime();//发货时间
//            orderRefund.setReceiveTime();//收货时间
//            orderRefund.setCloseTime();//关闭时间
//            orderRefund.setDecisionTime();//确定时间(确定退款时间)
//            orderRefund.setRefundTime();//退款时间

        orderRefundMapper.save2(orderRefund);
        if (orderRefund.getReturnMoneySts() == 5) {
            RefundInfo refundInfo = new RefundInfo();
            refundInfo.setRefundId(mallSalesOrderRt.getId());//id
            refundInfo.setCreateTime(mallSalesOrderRt.getCreatedate());//创建时间
            refundInfo.setUpdateTime(mallSalesOrderRt.getLastmodifieddate());//更新时间
            refundInfo.setOrderId(orderRefund.getOrderId());//关联订单id
            //在驿客存在一笔订单多次退款，我们这里的订单结算
            OrderSettlement orderSettlement = orderSettlementMapper.getByOrderId2(orderRefund.getOrderId());
            if (orderSettlement != null) {
                refundInfo.setPayId(orderSettlement.getPayId());//关联支付单id
            } else {
                log.info("订单id:{}，查询订单结算记录不存在，不保存当前记录的支付单id", refundInfo.getOrderId());
            }
            refundInfo.setUserId(orderRefund.getUserId());//用户id
            refundInfo.setRefundStatus(2);//退款状态
            refundInfo.setRefundAmount(orderRefund.getRefundAmount());//退款金额
            refundInfo.setPayType(8);//支付方式
            refundInfoMapper.save2(refundInfo);
        }
    }


}
