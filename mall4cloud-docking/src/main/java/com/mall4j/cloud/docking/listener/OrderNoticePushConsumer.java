package com.mall4j.cloud.docking.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.coupon.feign.CouponOrderFeignClient;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderDetailVO;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushOrderDto;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.vo.OrderAddrVO;
import com.mall4j.cloud.api.order.vo.OrderShopVO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.vo.SkuCodeVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.docking.skq_erp.service.IStdOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 根据订单Id推送订单信息
 *
 * @author zhangjie
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = RocketMqConstant.ORDER_NOTIFY_STD_TOPIC,
        consumerGroup = "GID_"+RocketMqConstant.ORDER_NOTIFY_STD_TOPIC,
        selectorExpression = RocketMqConstant.ORDER_NOTIFY_STD_TOPIC_TAG)
public class OrderNoticePushConsumer implements RocketMQListener<Long> {
    @Autowired
    private IStdOrderService stdOrderService;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private SkuFeignClient skuFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    CouponOrderFeignClient couponOrderFeignClient;
    @Autowired
    CouponFeignClient couponFeignClient;
    @Autowired
    StaffFeignClient staffFeignClient;

    @Override
    public void onMessage(Long orderId) {
        try {
            //根据订单id推送订单
            log.info("[推送订单] -- 订单id :{}", orderId);
            //查询订单明细信息
            ServerResponseEntity<OrderShopVO> orderShopVOServerResponseEntity = orderFeignClient.stdDetail(orderId);
            if (orderShopVOServerResponseEntity.isFail()) {
                throw new LuckException(ResponseEnum.EXCEPTION, orderShopVOServerResponseEntity.getMsg());
            }

            ArrayList<PushOrderDto> pushOrderDtos = new ArrayList<>();
            //封装订单信息
            PushOrderDto pushOrderDto = new PushOrderDto();
            buildPushOrderDto(pushOrderDto, orderShopVOServerResponseEntity.getData());
            //废弃 推送中台前价格检查
            checkprice(pushOrderDto);
            pushOrderDtos.add(pushOrderDto);
            //推送订单
            log.info("[订单推送参数] -- ：{}", JSONObject.toJSONString(pushOrderDtos));
            ServerResponseEntity<String> responseEntity = stdOrderService.pushOrder(pushOrderDtos);
            if(responseEntity==null || responseEntity.isFail()){
                Assert.faild(responseEntity.getMsg());
            }
        } catch (Exception e) {
            log.error("[推送订单] -- 异常输出 :{}", e.getMessage());
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

    }

    /**
     * 金额校验
     * @param pushOrderDto
     */
    private void checkprice(PushOrderDto pushOrderDto) {
        /**
         * 中台会对订单做金额校验。
         * 1、(price * num) - discount_fee = payment
         * 2、total_fee（应付金额）-discount_fee （折扣）=payment
         * 3、total_fee = price*num
         */
        BigDecimal order_Total_fee = BigDecimal.ZERO;
        BigDecimal order_discount_fee = BigDecimal.ZERO;
        for (PushOrderDto.Item item : pushOrderDto.getItems()) {
            // total_fee = price * num
            if(!item.getTotal_fee().equals(item.getPrice().multiply(new BigDecimal(item.getNum())))){
                item.setTotal_fee(item.getPrice().multiply(new BigDecimal(item.getNum())));
            }
            // total_fee（应付金额）-discount_fee （折扣）=payment
            // 如果不相等 discount_fee（折扣） = total_fee（应付金额） - payment
            if(!item.getTotal_fee().equals(item.getPayment().add(item.getDiscount_fee()))){
                item.setDiscount_fee(item.getTotal_fee().subtract(item.getPayment()));
            }
            order_Total_fee.add(item.getTotal_fee());
            order_discount_fee.add(item.getDiscount_fee());
        }
        pushOrderDto.setTotal_fee(order_Total_fee);
        pushOrderDto.setDiscount_fee(order_discount_fee);
    }

    private void buildPushOrderDto(PushOrderDto pushOrderDto, OrderShopVO data) {
        ServerResponseEntity<UserApiVO> userData = userFeignClient.getInsiderUserData(data.getUserId());
        if (userData.isFail()) {
            throw new LuckException("会员信息查询失败");
        }
        UserApiVO data1 = userData.getData();
        /**平台单号 tid*/
//        pushOrderDto.setTid(String.valueOf(data.getOrderId()));
        pushOrderDto.setTid(data.getOrderNumber());
        /**status 订单状态，数据枚举： WAIT_BUYER_PAY（待付款） WAIT_SELLER_SEND_GOODS（买家已付款 等待卖家发） WAIT_BUYER_CONFIRM_GOODS（卖家已发货 等待买家收货 ） TRADE_FINISHED（交易完成 ） TRADE_CANCELED（交易关闭） NO_DELIVERY(不可发货，如拼团订单)*/
        pushOrderDto.setStatus("WAIT_SELLER_SEND_GOODS");
        /**type 交易类型/支付方式,取值如下：在线支付 货到付款 京东支付 云闪付 微信 支付宝 银行卡 礼品卡 门店付款 赠品 现金 其他*/
        pushOrderDto.setType("微信");
        /**order_type 订单类型，枚举值如下：0（普通）1（预售）2（换货）3（分销采购单）4（ 补发单）101（员工内购单）*/
        pushOrderDto.setOrder_type(0);
        /**num 商品数量（订单商品总数量）*/
        pushOrderDto.setNum(data.getTotalNum());
        /**total_fee 商品金额（商品销售价总和）*/
        pushOrderDto.setTotal_fee(toYuan(data.getTotal()));

        /**discount_fee 整单优惠金额（订单级别的优惠，不包含商品级别优惠，如店铺优惠券）*/
        //因为需要不包含商品级别的优惠金额，当前我们的总优惠金额 = 各商品的优惠金额加总 所以这里直接传 0
//        pushOrderDto.setDiscount_fee(toYuan(data.getReduceAmount()));
        pushOrderDto.setDiscount_fee(BigDecimal.ZERO);

        /**adjust_fee 调整金额（如卖家手动后台调整金额，等于订单明细的调整金额之和，若平台不能给到明细级别的调整金额，则明细调整金额设置为空，OMS取头表调整金额并自己分摊到明细）*/


        /**post_fee 物流费*/
        pushOrderDto.setPost_fee(toYuan(data.getTransfee()));

        /**payment 实付金额，买家实际支付金额*/
        pushOrderDto.setPayment(toYuan(data.getActualTotal()));

        /** commission_fee 交易佣金*/


        /** plantfor_count 平台补贴,订单优惠金额中平台承担的部分*/


        /**trade_create_time 平台下单时间 格式 yyyy-MM-dd HH:mm:ss*/
        pushOrderDto.setTrade_create_time(DateUtil.format(data.getCreateTime(), DatePattern.NORM_DATETIME_FORMAT));

        /**pay_time 付款时间 格式 yyyy-MM-dd HH:mm:ss*/
        if(data.getPayTime()==null){
            pushOrderDto.setPay_time(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT));
        }else{
            pushOrderDto.setPay_time(DateUtil.format(data.getPayTime(), DatePattern.NORM_DATETIME_FORMAT));
        }


        /**trade_end_time 交易结束时间 格式 yyyy-MM-dd HH:mm:ss*/


        /**send_time 发货时间 格式 yyyy-MM-dd HH:mm:ss*/


        /** trade_update_time 平台修改时间 格式 yyyy-MM-dd HH:mm:ss*/


        /**seller_memo 卖家备注*/

        /**buyer_nick 买家昵称*/
        pushOrderDto.setBuyer_nick(data1.getNickName());

        /** buyer_email 买家邮箱*/

        /** buyer_message 买家留言*/
        pushOrderDto.setBuyer_message(data.getRemarks());

        /** mark_desc 异常描述*/

        /**sales_cp_c_store_ecode 下单门店*/
        if(data.getStoreId()!=null){
            StoreVO storeVO=storeFeignClient.findByStoreId(data.getStoreId());
            log.info("推送订单，查询storeCode 参数:{}，查询结果:{}",data.getStoreId(),JSONObject.toJSONString(storeVO));
            if(storeVO!=null){
                //如果不为虚拟门店，直接传门店code  如果为虚拟门店，传官店的code返回
                if(storeVO.getStoreInviteType()==0){
                    pushOrderDto.setSales_cp_c_store_ecode(storeVO.getStoreCode());
                }else{
                    pushOrderDto.setSales_cp_c_store_ecode(storeVO.getMainStoreCode());
                    pushOrderDto.setVirtual_store_id(storeVO.getStoreCode());
                    pushOrderDto.setVirtual_store_name(storeVO.getName());
                }

            }
        }

        OrderAddrVO orderAddr = data.getOrderAddr();
        /**receiver_name 收货人姓名*/
        pushOrderDto.setReceiver_name(orderAddr.getConsignee());
        /**收货省名称 receiver_province*/
        pushOrderDto.setReceiver_province(orderAddr.getProvince());
        /**receiver_city 收货市名称*/
        pushOrderDto.setReceiver_city(orderAddr.getCity());

        /**receiver_district 收货区/县名称*/
        pushOrderDto.setReceiver_district(orderAddr.getArea());
        /**receiver_address 收货详细地址*/
        pushOrderDto.setReceiver_address(orderAddr.getAddr());
        /** receiver_zip 收货人邮编*/
        pushOrderDto.setReceiver_zip(orderAddr.getPostCode());
        /**receiver_phone 收货人电话*/

        /**receiver_mobile 收货人手机号*/
        pushOrderDto.setReceiver_mobile(orderAddr.getMobile());
        /**shipping_type 物流类型，0:商家配送; 1:平台配送; 2:虚拟 3.自提*/
        pushOrderDto.setShipping_type("1");
        /**deliverno 物流单号*/

        /**logisticscompany 物流公司编码*/

        /**invoice_name 发票抬头*/

        /**invoicetype 发票类型，可取值：普通发票 增值税专用发票*/

        /** reserve_bigint02 预留字段12（自提/发货门店ID）*/

        /**cp_c_platform_ename 平台名称*/

        /**reserve_decimal02 实付积分*/

        /**分销佣金 reserve_decimal03*/

        /**导购员WID reserve_bigint01*/

        /**销售门店ID reserve_bigint03*/

        /**商品销售模式 reserve_bigint04*/


        /**发货前退款有退单，1：有退单，0：无退单 reserve_bigint05*/

        /**导购员名称 reserve_varchar01*/

        /**自提/发货门店名称 reserve_varchar02*/

        /**买家手机号 reserve_varchar03*/
        pushOrderDto.setReserve_varchar03(data1.getPhone());
        /**国家 reserve_varchar04*/

        /**主播账号 reserve_varchar05*/



        /**小程序下单会员号 order_vip_id*/
        pushOrderDto.setOrder_vip_id(data1.getVipcode());

        Integer is_group_purchase = 0;
        String coupon_code = "";
        String coupon_id = "";
        String coupon_name = "";
        ArrayList couponList = new ArrayList();
        couponList.add(data.getOrderId());
        ServerResponseEntity<List<TCouponUserOrderDetailVO>> couponUserOrderResponse = couponFeignClient.getCouponListBypByOrderIds(couponList);
        if(couponUserOrderResponse!=null && couponUserOrderResponse.isSuccess() && CollectionUtil.isNotEmpty(couponUserOrderResponse.getData())){
            for (TCouponUserOrderDetailVO datum : couponUserOrderResponse.getData()) {
                //如果类型为企业券，就为团购订单
                if(datum.getKind()==3){
                    is_group_purchase = 1;
                }
                coupon_code = datum.getCouponCode();
                coupon_id = StrUtil.toString(datum.getCouponId());
                coupon_name = datum.getCouponName();
            }
        }

        /**是否是团购订单 is_group_purchase*/
        pushOrderDto.setIs_group_purchase(is_group_purchase);
        /**订单优惠券码 coupon_code*/
        pushOrderDto.setCoupon_code(coupon_code);
        pushOrderDto.setCoupon_id(coupon_id);
        pushOrderDto.setCoupon_name(coupon_name);

        /**导购员工号 staff_no*/
        if(data.getDistributionUserId() > 0 && data.getDistributionUserType()==1){
            ServerResponseEntity<StaffVO> staffVOResponse = staffFeignClient.getStaffById(data.getDistributionUserId());
            if(staffVOResponse!=null && staffVOResponse.isSuccess() && staffVOResponse.getData()!=null){
                StaffVO staff = staffVOResponse.getData();
                pushOrderDto.setStaff_no(staff.getStaffNo());
            }
        }

        /**微客手机号 weike_phone*/
        if(data.getDistributionUserId() > 0 && data.getDistributionUserType()==2){
            Long weikeUserId =  data.getDistributionUserId();
            ServerResponseEntity<UserApiVO> weikeData = userFeignClient.getInsiderUserData(weikeUserId);
            if (userData.isFail()) {
                throw new LuckException("微客会员信息查询失败");
            }
            UserApiVO weikeUser = weikeData.getData();
            pushOrderDto.setWeike_phone(weikeUser.getPhone());
        }



        /**订单项 items*/
        pushOrderDto.setItems(buildItems(data.getOrderItems(),pushOrderDto.getSales_cp_c_store_ecode(),data.getOrderType()));
    }

    private ArrayList<PushOrderDto.Item> buildItems(List<OrderItemVO> itemVOList,String storeCode,Integer orderType) {
        if (CollectionUtil.isEmpty(itemVOList)) {
            return null;
        }
        ArrayList<PushOrderDto.Item> items = new ArrayList<>();
        itemVOList.forEach(orderItemVO -> {
            ServerResponseEntity<SkuCodeVO> codeBySkuId = skuFeignClient.getCodeBySkuId(orderItemVO.getSkuId());
            if (codeBySkuId.isFail()) {
                throw new LuckException("商品数据异常");
            }
            SkuCodeVO skuCodeVO = codeBySkuId.getData();
            PushOrderDto.Item item = new PushOrderDto.Item();
            /**tid 平台订单号"*/
//            item.setTid(String.valueOf(orderItemVO.getOrderId()));
            item.setTid(orderItemVO.getOrderNumber());
            /**oid 子订单编号，明细行单号，必须唯一"*/
            item.setOid(String.valueOf(orderItemVO.getOrderItemId()));
            /**refund_status 交易状态，可取枚举：WAIT_BUYER_PAY（待付款）WAIT_SELLER_SEND_GOODS（买家已付款 等待卖家发）WAIT_BUYER_CONFIRM_GOODS（卖家已发货 等待买家收货）TRADE_FINISHED（交易完成）TRADE_CANCELED（交易关闭）NO_DELIVERY(不可发货，如拼团订单)"*/
            item.setStatus("WAIT_SELLER_SEND_GOODS");
            /**refund_status 退款状态，可取枚举：0(未申请退款),1(申请退款，等待卖家同意),2(卖家同意退款/退货),3(买家已发货，等待卖家收货),4(卖家拒绝退款),5(退款关闭),6(退款完成)*/
            item.setRefund_status("0");
            /**title 商品标题"*/
            item.setTitle(orderItemVO.getSpuName());

            /**brandname 品牌名*/

            /**num_iid 平台商品编码*/
            item.setNum_iid(String.valueOf(skuCodeVO.getPriceCode()));

            /**sku_id 平台条码编码"*/
            item.setSku_id(String.valueOf(skuCodeVO.getSkuCode()));

            /**outer_iid 外部商品货号*/
            item.setOuter_iid(skuCodeVO.getSpuCode());

            /**outer_sku_id 外部商品条码"*/
            item.setOuter_sku_id(skuCodeVO.getSkuCode());

            /**num 商品数量"*/
            item.setNum(String.valueOf(orderItemVO.getCount()));

            /**price 商品原价"*/
            item.setPrice(toYuan(orderItemVO.getPrice()));
            /**如果为秒杀订单，一口价，团购订单 修改商品价格*/
            if(OrderType.SECKILL.value()==orderType || OrderType.GROUP.value()==orderType || OrderType.ONEPRICE.value()==orderType){
                item.setPrice(toYuan(orderItemVO.getSpuTotalAmount()/orderItemVO.getCount()));
                log.info("秒杀订单重置金额。计算price:{},",toYuan(orderItemVO.getSpuTotalAmount()/orderItemVO.getCount()));
            }

            /**total_fee 应付金额，当前商品的应付金额"*/
            item.setTotal_fee(toYuan(orderItemVO.getSpuTotalAmount()));

            /**payment 实付金额，当前商品买家实际支付金额"*/
            item.setPayment(toYuan(orderItemVO.getActualTotal()));
            /**discount_fee 商品优惠，商品级别的优惠，如商品促销优惠"*/
            item.setDiscount_fee(toYuan(orderItemVO.getShareReduce()));

            /**adjust_fee 平台优惠平摊金额*/

            /**reserve_decimal01 平台优惠平摊金额*/

            /**order_update_time 平台修改时间，格式：yyyy-MM-dd HH:mm:ss*/

            /**sku_properties_name 条码属性，如颜色分类:梦幻粉6310;尺码:35码*/
            item.setSku_properties_name(orderItemVO.getSkuName());

            /**pic_path 图片路径*/
            item.setPic_path(orderItemVO.getPic());

            /**buyer_message 买家留言*/

            /**reserve_varchar03 推广者昵称*/


            /**reserve_decimal02 推广者ID*/

            /**sales_cp_c_store_ecode 下单门店*/
            item.setSales_cp_c_store_ecode(storeCode);

            items.add(item);
        });
        return items;
    }


    private BigDecimal toYuan(Long total) {
        try {
            if (Objects.nonNull(total)) {
                return new BigDecimal(total).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            } else {
                return new BigDecimal(0);
            }
        } catch (Exception e) {
            log.error("数据转换异常 -- 返回空");
        }
        return new BigDecimal(0);
    }
}
