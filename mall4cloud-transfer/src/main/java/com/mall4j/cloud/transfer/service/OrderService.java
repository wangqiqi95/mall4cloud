package com.mall4j.cloud.transfer.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.transfer.mapper.*;
import com.mall4j.cloud.transfer.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @luzhengxiang
 * @create 2022-04-04 10:39 PM
 **/
@Slf4j
@Service
public class OrderService {
    @Autowired
    private SegmentFeignClient segmentFeignClient;
    @Autowired
    MallSalesOrderMapper mallSalesOrderMapper;
    @Autowired
    OrderAddrMapper orderAddrMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    MallSalesOrderDtlMapper mallSalesOrderDtlMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    SkuFeignClient skuFeignClient;
    @Autowired
    SpuFeignClient spuFeignClient;

    @Autowired
    MallSalesOrderRemarkMapper mallSalesOrderRemarkMapper;
    @Autowired
    PayInfoMapper payInfoMapper;
    @Autowired
    OrderSettlementMapper orderSettlementMapper;
    @Autowired
    EdBaseShopMapper edBaseShopMapper;
    @Autowired
    StoreFeignClient storeFeignClient;

    int currentPage = 1;
    int pageSize = 2000;

    Map<String,List<StoreCodeVO>> storeMap = new HashMap<>();


    /**
     * 订单数据同步
     */
    @Async
    public void orderTransfer() {

        /**
         * 首先查询 订单主表 mall_sales_order
         * order
         * order_addr
         *
         * order_item
         * order_settlement
         * pay_info
         *
         */
        log.info("订单同步开始。");
        long startTime = System.currentTimeMillis();
        //分页处理，每次处理2000条数据批量保存。
        PageHelper.startPage(currentPage, pageSize);
        List<MallSalesOrder> mallSalesOrders = mallSalesOrderMapper.list();
        PageInfo<MallSalesOrder> pageInfo = new PageInfo(mallSalesOrders);
        createOrder(mallSalesOrders, currentPage);

        int totalPage = pageInfo.getPages();
        int totalCount = (int)pageInfo.getTotal();
        for(int i = 2; i<=totalPage; i++){
            PageHelper.startPage(i,pageSize);
            List<MallSalesOrder> newMallSalesOrders = mallSalesOrderMapper.list();
            createOrder(newMallSalesOrders,i);
        }
        log.info("订单同步执行结束，累计耗时:{}ms",System.currentTimeMillis() - startTime);
    }

    private void createOrder(List<MallSalesOrder> mallSalesOrders, int currentPage) {
        List<Order> orders = new ArrayList<>();

        for (MallSalesOrder mallSalesOrder : mallSalesOrders) {
            Order order = new Order();
            order.setOrderId(mallSalesOrder.getId());
            order.setOrderNumber(mallSalesOrder.getCode());
            order.setCreateTime(mallSalesOrder.getCreatedate());//创建时间
            order.setUpdateTime(mallSalesOrder.getLastmodifieddate());//修改时间

            /**
             * todo  mall_sales_order.EShopId，根据Id查编码
             * 怎么跟我们的门店对应上。
             * mall_sales_order.EShopId 查询 ed_base_shop 获取门店code 通过门店code查询 门店服务转换成我们系统的门店id
             *
             */
            EdBaseShop edBaseShop = edBaseShopMapper.getById(mallSalesOrder.getEshopid().longValue());
            List shopCodes = new ArrayList();
            shopCodes.add(edBaseShop.getCode());
            List<StoreCodeVO> storeCodeS = null;
            if(storeMap.containsKey(shopCodes)){
                storeCodeS = storeMap.get(shopCodes);
            }else{
                storeCodeS = storeFeignClient.findByStoreCodes(shopCodes);
                storeMap.put(StrUtil.toString(shopCodes),storeCodeS);
            }

            if(CollUtil.isNotEmpty(storeCodeS)){
                order.setStoreId(storeCodeS.get(0).getStoreId());//门店id
            }else{
                order.setStoreId(0L);//门店id
                log.info("当前门店code:{}不存在，当前订单不同步",edBaseShop.getCode());
//                continue;
            }



//            order.setShopId();//门店id

            order.setUserId(mallSalesOrder.getBuyerid());
//            order.setBuyStaffId();

            order.setTotal(mallSalesOrder.getTotalmoney().longValue() * 100); //订单总金额
            order.setActualTotal(mallSalesOrder.getPayamount().longValue() * 100); //订单实际总值

            //驿客 物流方式 0快递，1自提
            //我们系统物流方式 配送类型 1:快递 2:自提 3：无需快递 4同城配送
            order.setDeliveryType(mallSalesOrder.getDelivtype() + 1);

            order.setReduceAmount(mallSalesOrder.getDiscountamount().longValue() * 100);//优惠总额

            order.setPlatformCouponAmount(mallSalesOrder.getCouponuseamount().longValue() * 100);//平台优惠券优惠金额
            order.setDiscountAmount(order.getReduceAmount() - order.getPlatformCouponAmount());//满减优惠金额

            order.setScoreAmount(0L);//积分抵扣金额
            order.setMemberAmount(0L);//会员折扣金额
            order.setShopCouponAmount(0L);//商家优惠券优惠金额
            order.setPlatformFreeFreightAmount(0L);//平台运费减免金额
            order.setFreeFreightAmount(0L);//商家运费减免金额
            order.setShopChangeFreeAmount(0L);//店铺改价优惠金额

            order.setTentacleNo("");//触点号
//            order.setDistributionRelation();//分销关系 1分销关系 2服务关系 3自主下单 4代客下单
            order.setDistributionAmount(0L);//分销佣金
            order.setDistributionStatus(0);//分销佣金状态 0-待结算 1-已结算-待提现 2-已结算-已提现
            order.setDistributionUserType(0);//分销用户类型 1-导购 2-微客 默认给0
            order.setDistributionUserId(0L);//分销用户ID (导购或微客)
//            order.setDistributionStoreId();//分销用户门店ID
//            order.setDistributionSettleTime();//分销佣金结算时间
//            order.setDistributionWithdrawTime();//分销佣金提现时间
            order.setDevelopingAmount(0L);//发展佣金
            order.setDevelopingStatus(0);//展佣金状态 0-待结算 1-已结算-待提现 2-已结算-已提现
            order.setDevelopingUserId(0L);//发展用户ID (导购)
            order.setDevelopingStoreId(0L);//发展用户门店ID
//            order.setDevelopingSettleTime();//发展佣金结算时间
//            order.setDevelopingWithdrawTime();//发展佣金提现时间
            order.setPlatformAmount(0L);//平台优惠金额
            order.setPlatformCommission(0L);//平台佣金


            order.setFreightAmount(mallSalesOrder.getExpressfee().longValue() * 100);//运费

            order.setAllCount(mallSalesOrder.getTotalqty());//订单数量


            order.setDeliveryTime(mallSalesOrder.getHassendtime()); //发货时间
            order.setFinallyTime(mallSalesOrder.getFinishtime());   //完成时间

            /**
             * todo 驿客没有结算的表。 怎么处理这两个字段
             *
             */
//            order.setIsSettled();   //是否结算
//            order.setSettledTime(); //结算时间

            //支付方式 PayType枚举类 驿客文档没有这个字段，暂时写死全部为微信APP支付
            /**
             * 驿客支付状态
             * 0未支付，1支付成功 2 支付失败
             * todo 支付成功 支付单号
             */
            if (mallSalesOrder.getPaystatus() == 1) {
                order.setIsPayed(1);
                order.setPayType(8);
                order.setPayTime(mallSalesOrder.getPaytime()); //支付时间
            } else {
                order.setIsPayed(0);
            }
            order.setOrderType(0);//订单类型 写死0


            /**
             * todo mall_sales_order_remark.Remark
             * mall_sales_order_remark表没有提供
             */
            MallSalesOrderRemark mallSalesOrderRemark = mallSalesOrderRemarkMapper.getByOrderId(order.getOrderId());
            if (mallSalesOrderRemark != null) {
                order.setShopRemarks(mallSalesOrderRemark.getRemark());//卖方备注
            }
            order.setRemarks(mallSalesOrder.getBuyerremark());//买方备注


            /**
             * 订单状态 1:待付款 2:待发货(待自提) 3:待收货(已发货) 5:成功 6:失败 7:待成团
             * 驿客订单状态 0已取消 1待付款 2 待发货 4 已发货 8交易成功 16 交易完成 20已关闭 1100待成团 1200定金支付成功
             *
             * 0对应我们的6
             * 1对应我们的1
             * 2对应我们的2
             * 4对应我们的3
             * 8对应我们的5
             * 16对应我们的5
             * 20对应我们的6
             * 1100对应我们的7
             */
            int orderStatus = 2;
            if (mallSalesOrder.getOrderstatus() == 2) {
                orderStatus = 2;
            } else if (mallSalesOrder.getOrderstatus() == 0) {
                orderStatus = 6;
            } else if (mallSalesOrder.getOrderstatus() == 1) {
                orderStatus = 1;
            } else if (mallSalesOrder.getOrderstatus() == 4) {
                orderStatus = 3;
            } else if (mallSalesOrder.getOrderstatus() == 8) {
                orderStatus = 5;
            } else if (mallSalesOrder.getOrderstatus() == 16) {
                orderStatus = 5;
            } else if (mallSalesOrder.getOrderstatus() == 20) {
                orderStatus = 6;
            } else {
                orderStatus = 7;
            }
            order.setStatus(orderStatus);
            order.setDeleteStatus(0); //用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除

            /**
             * mall_sales_order_status.CreateDate
             * mall_sales_order_status 没有提供
             * 取消时间是否可以创建时间 + 半小时
             */
            if(order.getStatus()==6){
                order.setCancelTime(mallSalesOrder.getCreatedate());  //取消时间 取创建时间
            }


            /**
             * 添加订单子表
             * 如果对应的 订单子表商品表中数据不存在，则跳过当前记录
             */
            orderItemTransfer(order, mallSalesOrder);
//            boolean flag = orderItemTransfer(order, mallSalesOrder);
//            if (!flag) {
//                continue;
//            }

            //如果收货人不为空，则添加收货地址表信息，设置收货地址id
            if (StrUtil.isNotEmpty(mallSalesOrder.getRecvconsignee())) {
                OrderAddr orderAddr = new OrderAddr();
//                orderAddr.setCreateTime(DateUtil.parseDate(DateUtil.format(mallSalesOrder.getCreatedate(),"yyyy-MM-dd HH:mm:ss")).toJdkDate());
//                orderAddr.setUpdateTime(DateUtil.parseDate(DateUtil.format(mallSalesOrder.getLastmodifieddate(),"yyyy-MM-dd HH:mm:ss")).toJdkDate());
                orderAddr.setCreateTime(mallSalesOrder.getCreatedate());
                orderAddr.setUpdateTime(mallSalesOrder.getLastmodifieddate());
                orderAddr.setConsignee(mallSalesOrder.getRecvconsignee());
                orderAddr.setProvince(mallSalesOrder.getRecvprovince());
                orderAddr.setCity(mallSalesOrder.getRecvcity());
                orderAddr.setArea(mallSalesOrder.getRecvcounty());
                orderAddr.setAddr(mallSalesOrder.getRecvaddress());
                orderAddr.setMobile(mallSalesOrder.getRecvmobile());
                orderAddrMapper.save(orderAddr);

                order.setOrderAddrId(orderAddr.getOrderAddrId());
            }

            if(order.getIsPayed()==1){
                PayInfo payInfo = new PayInfo();
                payInfo.setCreateTime(mallSalesOrder.getPaytime());
                payInfo.setUpdateTime(mallSalesOrder.getPaytime());
                payInfo.setUserId(order.getUserId());
                payInfo.setOrderIds(StrUtil.toString(order.getOrderId()));
                payInfo.setBizPayNo(mallSalesOrder.getTradeno());//外部订单流水号
                payInfo.setSysType(0);//系统类型 见SysTypeEnum
                payInfo.setPayEntry(0);//支付入口 0下单 1余额充值
                payInfo.setPayType(1);//支付方式 1 微信支付 2 支付宝
                payInfo.setPayStatus(1);//支付状态
                payInfo.setPayScore(0L);//支付积分
                payInfo.setPayAmount(order.getActualTotal());//支付金额
                payInfo.setVersion(1);//版本号
                ServerResponseEntity<Long> responseEntity = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_PAY);
                Long payId = responseEntity.getData();
                payInfo.setPayId(payId);
                payInfoMapper.save(payInfo);

                OrderSettlement orderSettlement = new OrderSettlement();
                orderSettlement.setCreateTime(mallSalesOrder.getPaytime());
                orderSettlement.setUpdateTime(mallSalesOrder.getPaytime());
                orderSettlement.setOrderId(order.getOrderId());
                orderSettlement.setPayId(payId);
                orderSettlement.setPayType(1);//小程序支付
//            orderSettlement.setIsClearing();//是否清算
                orderSettlement.setPayScore(0L);//支付积分
                orderSettlement.setPayAmount(order.getActualTotal());//支付金额
//            orderSettlement.setClearingTime();//清算时间
                orderSettlement.setIsPayed(1);//是否支付

                orderSettlementMapper.save(orderSettlement);
            }




            orders.add(order);
        }

        if (CollUtil.isNotEmpty(orders)) {
            orderMapper.saveBatch(orders);
        }
        log.info("第{}页数据插入执行结束。插入订单条数:{}", currentPage, orders.size());
    }

    /**
     * 创建支付记录
     *
     * @param order
     */
    private void savePayInfo(Order order) {

    }


    /**
     * orderItem 数据迁移
     */
    public boolean orderItemTransfer(Order order, MallSalesOrder mallSalesOrder) {
        /**
         * 首先查询 订单明细 mall_sales_order_dtl
         * order_item
         *
         */
        List<MallSalesOrderDtl> mallSalesOrderDtls = mallSalesOrderDtlMapper.getByOrderId(order.getOrderId());
        for (MallSalesOrderDtl mallSalesOrderDtl : mallSalesOrderDtls) {
//            OrderItem dborderItem = orderItemMapper.getByOrderItemId2(mallSalesOrderDtl.getId());
//            if(dborderItem!=null){
//                log.error("当前子订单id:{}已经存在，不继续插入当前子订单记录。",mallSalesOrderDtl.getId());
//                continue;
//            }

            OrderItem orderItem = new OrderItem();

            SkuVO sku = getSku(mallSalesOrderDtl);
            if(sku==null){
                orderItem.setSpuId(0L);//产品ID
                orderItem.setSpuName("");//产品名称
                orderItem.setSkuId(0L);//产品SkuID
                orderItem.setSkuName("");//产品sku名称
                orderItem.setPic("");//产品主图片路径
            }else{
                orderItem.setSpuId(sku.getSpuId());//产品ID
                orderItem.setSkuId(sku.getSkuId());//产品SkuID
                orderItem.setSkuName(sku.getSkuName());//产品sku名称

                SpuVO spu = getSpu(sku.getSpuId());
                if(spu!=null){
                    orderItem.setSpuName(spu.getName());//产品名称
                    orderItem.setPic(spu.getMainImgUrl());//产品主图片路径
                }else{
                    orderItem.setSpuId(0L);//产品ID
                    orderItem.setSpuName("");//产品名称
                }

            }
            orderItem.setOrderItemId(mallSalesOrderDtl.getId());//订单明细id
            orderItem.setCreateTime(mallSalesOrderDtl.getCreatedate());
            orderItem.setUpdateTime(mallSalesOrderDtl.getLastmodifieddate());

            orderItem.setOrderId(mallSalesOrderDtl.getOrderid());//订单id
            orderItem.setOrderNumber(order.getOrderNumber());
            orderItem.setShopId(order.getStoreId());

//            orderItem.setCategoryId();//分类id

//            orderItem.setSpuId(0L);//产品ID
//            orderItem.setSpuName("");//产品名称
//            orderItem.setSkuId(0L);//产品SkuID
//            orderItem.setSkuName("");//产品sku名称
//            orderItem.setPic("");//产品主图片路径


            orderItem.setUserId(order.getUserId());//用户id
            //todo 判断当前数据是否有发生退款 mall_sales_order_ref 表
//            orderItem.setFinalRefundId();//final_refund_id

            orderItem.setCount(mallSalesOrderDtl.getQuantity());//购物车产品个数


            orderItem.setIsComm(0);//是否以评价(0.未评价1.已评价)
//            orderItem.setCommTime();//评论时间
//            orderItem.setRefundStatus();//订单项退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）
//            orderItem.setBeDeliveredNum();//0全部发货 其他数量为剩余待发货数量
            orderItem.setDeliveryType(1);//单个orderItem的配送类型 1:快递 2:自提 3：无需快递 4:同城配送
//            orderItem.setShopCartTime();//加入购物车时间

            orderItem.setPrice(mallSalesOrderDtl.getPricesell().longValue() * 100);//产品价格
            orderItem.setSpuTotalAmount(mallSalesOrderDtl.getAmount().longValue() * 100);//商品总金额
            orderItem.setActualTotal(mallSalesOrderDtl.getAmount().longValue() * 100);//商品实际金额 = 商品总金额 - 分摊的优惠金额
            orderItem.setShareReduce(0L);//分摊的优惠金额(商家分摊 + 平台补贴)

            orderItem.setPlatformShareReduce(0L);
//            orderItem.setDistributionUserId();//推广员id
            orderItem.setDistributionAmount(0L);//推广员佣金
            orderItem.setDistributionParentAmount(0L);//上级推广员佣金

//            orderItem.setScoreAmount();//积分价格（单价）
            orderItem.setUseScore(0L);//使用积分
            orderItem.setGainScore(0L);//获得积分

            orderItem.setRate(0.0);//分账比例
            orderItem.setPlatformCommission(0L);//平台佣金(商品实际金额 * 分账比例)

            orderItem.setScoreAmount(0L);//积分抵扣金额
            orderItem.setMemberAmount(0L);//会员折扣金额
            orderItem.setPlatformCouponAmount(0L);//平台优惠券优惠金额

            orderItem.setShopCouponAmount(0L);//商家优惠券优惠金额

            orderItem.setDiscountAmount(0L);//满减优惠金额
            orderItem.setPlatformFreeFreightAmount(0L);//平台运费减免金额

            orderItem.setFreeFreightAmount(0L);//商家运费减免金额
            orderItem.setShopChangeFreeAmount(0L);//店铺改价优惠金额
            orderItemMapper.save(orderItem);
        }
        return true;
    }

    Map<String,SkuVO> skuMap = new HashMap<>();
    private SkuVO getSku(MallSalesOrderDtl mallSalesOrderDtl){
        if(skuMap.containsKey(mallSalesOrderDtl.getBarcode())){
            return skuMap.get(mallSalesOrderDtl.getBarcode());
        }

        ServerResponseEntity<SkuVO> skuVOServerResponse = skuFeignClient.insiderGetBySkuCode(mallSalesOrderDtl.getBarcode());
        if(!skuVOServerResponse.isSuccess()||skuVOServerResponse.getData()==null){
            skuMap.put(mallSalesOrderDtl.getBarcode(),null);
            return null;
        }
        SkuVO sku = skuVOServerResponse.getData();
        skuMap.put(mallSalesOrderDtl.getBarcode(),skuVOServerResponse.getData());
        return skuVOServerResponse.getData();
    }

    Map<Long,SpuVO> spuMap = new HashMap<>();
    private SpuVO getSpu(Long spuId){
        if(spuMap.containsKey(spuId)){
            return spuMap.get(spuId);
        }
        ServerResponseEntity<SpuVO> spuVOServerResponse = spuFeignClient.getDetailById(spuId);

        if(!spuVOServerResponse.isSuccess()||spuVOServerResponse.getData()==null){
            spuMap.put(spuId,null);
            return null;
        }
        SpuVO spu = spuVOServerResponse.getData();
        spuMap.put(spuId,spu);
        return spu;
    }


}
