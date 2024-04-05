package com.mall4j.cloud.coupon.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.coupon.constant.CouponProdType;
import com.mall4j.cloud.api.coupon.constant.CouponType;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.order.vo.CouponOrderVO;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.util.PriceUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 协助优惠券进行选择
 *
 * @author FrozenWatermelon
 */
@Data
@Slf4j
public class ChooseCouponHelper {
    /**
     * 购物项
     */
    private final List<ShopCartItemVO> shopCartItems;
    /**
     * 含有的优惠券列表
     */
    private final List<CouponOrderVO> shopCoupons;
    /**
     * 选中的优惠券id
     * 券码
     */
    private final List<String> couponIds;

    /**
     * 用户是否选中优惠券
     */
    private final Integer userChangeCoupon;
    /**
     * 选中的优惠券
     */
    private CouponOrderVO chooseCoupon;

    /**
     * 可用优惠券的商品实际金额（商品实际金额 - 商品分摊金额）
     */
    private Long prodCanUseCouponActualTotal;

    /**
     * 优惠金额
     */
    private Long couponReduce;

    /**
     * 优惠金额
     */
    private Long chooseCouponProdAmount;

    /**
     * 优惠券在订单中的优惠金额
     */
    private Long couponInOrderAmount;

    private Long totalTransfee;


    /**
     * 优惠金额关联店铺map
     */
    private final Map<Long, Long> shopReduceMap = new HashMap<>(12);

    public ChooseCouponHelper(List<ShopCartItemVO> shopCartItems, List<CouponOrderVO> shopCoupons, List<String> couponIds, Integer userChangeCoupon, Long totalTransfee) {
        this.shopCartItems = shopCartItems;
        this.shopCoupons = shopCoupons;
        this.couponIds = couponIds;
        this.userChangeCoupon = userChangeCoupon;
        this.totalTransfee = totalTransfee;
    }

    public CouponOrderVO getChooseCoupon() {
        return chooseCoupon;
    }


    public Long getCouponReduce() {
        return this.couponReduce;
    }

    public Map<Long, Long> getShopReduceMap() {
        return shopReduceMap;
    }

    public ChooseCouponHelper invoke() {
        log.info("执行优惠金额计算，当前对象参数：{}", JSONObject.toJSONString(this));
        // 没有平台优惠券直接返回
        if (CollectionUtil.isEmpty(shopCartItems)) {
            return this;
        }


        // 用户选中的关于该店铺的优惠券，一家店只能选择一张优惠券
        chooseCoupon = null;


        // 可用优惠券的商品实际金额（商品实际金额 - 商品分摊金额）
        prodCanUseCouponActualTotal = 0L;
        Date now = new Date();
        for (CouponOrderVO shopCoupon : shopCoupons) {

            if (shopCoupon.getStartTime().getTime() > now.getTime() && shopCoupon.getEndTime().getTime() < now.getTime()) {
                continue;
            }
            //优惠券类型判断 设置商品计算金额


            // 该优惠券关联的商品id
            List<Long> prodIds = shopCoupon.getSpuIds();
            // 可用优惠券的商品实际金额（商品实际金额 - 商品分摊金额）
            prodCanUseCouponActualTotal = 0L;
            // 聚合 用户选中的关于该店铺的优惠券
            log.info("couponIds = {} , shopCoupon = {}", JSONUtil.toJsonStr(couponIds), JSONUtil.toJsonStr(shopCoupon));
            if (CollectionUtil.isNotEmpty(couponIds)) {
                for (String couponId : couponIds) {
                    if (Objects.equals(couponId, shopCoupon.getCouponCode())) {
                        chooseCoupon = shopCoupon;
                        break;
                    }
                }
            }
//            if(chooseCoupon==null){
//                continue;
//            }
            log.info("shopCartItems = {}", JSONUtil.toJsonStr(shopCartItems));
            for (ShopCartItemVO shopCartItem : shopCartItems) {
//                shopCartItem.setIsShareReduce(0);
                // 该商品是否在该优惠券可用
                boolean isCouponsProd = isCouponsProd(shopCartItem.getSpuId(), prodIds, shopCoupon.getSuitableProdType(),shopCartItem.getPriceCode(),shopCoupon.getPriceCodes());

                //适用商品分类 当前优惠券设置了指定分类可用，，
                //当优惠券的适用分类为不限制。 或者优惠券为限制分类可用并且 商品满足优惠券分类条件
                boolean isCategory = false;
                if(shopCoupon.getCategoryScopeType() ==1 || (shopCoupon.getCategoryScopeType()==2 && shopCoupon.getCategorys().contains(shopCartItem.getCategory()))){
                    isCategory = true;
                }
                // 如果虚拟门店活动设置了指定券可用，并且包含当前券。
                boolean invateStoreCoupon = false;
                if(shopCartItem.isInvateStorePriceFlag() && shopCartItem.getFriendlyCouponUseFlag()==3
                        && CollectionUtil.isNotEmpty(shopCartItem.getInvateStoreCouponids()) && chooseCoupon !=null
                        && shopCartItem.getInvateStoreCouponids().contains(chooseCoupon.getCouponId())){
                    invateStoreCoupon = true;
                }

                //判断当前优惠券是否有原折扣限制。判断当前商品是否符合原折扣限制
                boolean discountLimitFlag = true;
                if(shopCoupon.getDisNoles() == 1 ){
                    BigDecimal disNolesValue = shopCoupon.getDisNolesValue().divide(BigDecimal.TEN,2,BigDecimal.ROUND_DOWN);
                    BigDecimal realDiscount = BigDecimal.valueOf(shopCartItem.getPriceFee()).
                            divide(BigDecimal.valueOf(shopCartItem.getMarketPriceFee()),2,BigDecimal.ROUND_DOWN);
                    if(realDiscount.doubleValue() < disNolesValue.doubleValue()){
                        discountLimitFlag = false;
                    }
                }

                //增加判断，当前优惠券被选中并且当前商品可以使用当前优惠券 并且当前商品没有参加会员日活动或者参加会员日活动设置的为可以使用优惠券
                if (discountLimitFlag && isCategory && isCouponsProd && chooseCoupon!=null && Objects.equals(shopCoupon.getCouponCode(), chooseCoupon.getCouponCode())
                        && (shopCartItem.getFriendlyCouponUseFlag() ==1 || invateStoreCoupon)) {
//                if (isCouponsProd && Objects.equals(shopCoupon.getCouponId(), chooseCoupon.getCouponId())) {


                    shopCartItem.setIsShareReduce(1);
                    if (Objects.nonNull(chooseCoupon) ) {
                        if (Objects.equals(chooseCoupon.getKind(), Short.valueOf("1")) || Objects.equals(chooseCoupon.getPriceType(), Short.valueOf("1"))) {
                            prodCanUseCouponActualTotal = prodCanUseCouponActualTotal + shopCartItem.getActualTotal();
                        } else {
                            log.info("正价折扣开始");
//                            shopCartItem.setSkuCouponBeforePriceFee(shopCartItem.getSkuPriceFee());
                            shopCartItem.setPriceFee(shopCartItem.getMarketPriceFee());
                            shopCartItem.setSkuPriceFee(shopCartItem.getMarketPriceFee());
                            //商品项总额调整
                            log.info("shopCartItem = actualTotal:{} marketTotalPrice:{} totalAmount:{}", shopCartItem.getActualTotal() ,
                                    shopCartItem.getMarketTotalPrice() ,
                                    shopCartItem.getTotalAmount());
                            shopCartItem.setActualTotal(shopCartItem.getActualTotal() + shopCartItem.getMarketTotalPrice() - shopCartItem.getTotalAmount());
                            shopCartItem.setTotalAmount(shopCartItem.getMarketTotalPrice());
                            prodCanUseCouponActualTotal = prodCanUseCouponActualTotal + shopCartItem.getMarketTotalPrice();
                        }
                        chooseCouponProdAmount = prodCanUseCouponActualTotal;
                        log.info("prodCanUseCouponActualTotal = {}", prodCanUseCouponActualTotal);
                    }
                }
            }
            Date date = new Date();
            // 返回优惠券默认全部可用
            // 将优惠券标记为可用状态
            shopCoupon.setCanUse(true);


            // 如果用户没有选择优惠券，系统默认选择一张可用优惠券
//                if (chooseCoupon == null && !Objects.equals(userChangeCoupon, 1)) {
//                    chooseCoupon = shopCoupon;
//                    chooseCouponProdAmount = prodCanUseCouponActualTotal;
//                }

        }
//        for (CouponOrderVO shopCoupon : shopCoupons) {
//            // 该优惠券关联的商品id
//            List<Long> prodIds = shopCoupon.getSpuIds();
//            if (Objects.nonNull(chooseCoupon) && !Objects.equals(shopCoupon.getCouponUserId(), chooseCoupon.getCouponUserId())) {
//                continue;
//            }
//            for (ShopCartItemVO shopCartItem : shopCartItems) {
//                shopCartItem.setIsShareReduce(0);
//                // 该商品是否在该优惠券可用
//                boolean isCouponsProd = isCouponsProd(shopCartItem.getSpuId(), prodIds, shopCoupon.getSuitableProdType());
//                if (isCouponsProd) {
//                    shopCartItem.setIsShareReduce(1);
//                }
//            }
//        }
        prodCanUseCouponActualTotal = chooseCouponProdAmount;



        if (chooseCoupon != null) {
            chooseCoupon.setChoose(true);
            int isShareReduceCount = shopCartItems.stream().filter(s-> s.getIsShareReduce()==1).collect(Collectors.toList()).size();
            if(isShareReduceCount==0){
                Assert.faild("当前下单商品不满足此优惠券的使用条件，请选择其它商品下单。");
            }
            // 计算优惠券优惠金额
            calculateCouponReduce();
            // 设置分摊优惠金额
            setShareReduce();
        }
        return this;
    }

    /**
     * 判断某个商品是否在此优惠券中
     *
     * @param prodId         在该店铺中的商品商品id
     * @param couponProdIds  优惠券关联的商品id
     * @param couponProdType 优惠券适用商品类型
     * @param priceCode      在该店铺中的商品priceCode
     * @param priceCodes     优惠券关联的商品priceCode集合
     * @return 商品是否在此优惠券中
     */
    private boolean isCouponsProd(Long prodId, List<Long> couponProdIds, Integer couponProdType,String priceCode,List<String> priceCodes) {

        if (CouponProdType.ALL.value().equals(couponProdType)) {
            return true;
        }
        if (CouponProdType.PROD_IN.value().equals(couponProdType)) {
            if (CollectionUtil.isEmpty(couponProdIds)) {
                return false;
            }
            if (couponProdIds.contains(prodId)) {
                return true;
            }
            return false;
        }
        if (CouponProdType.PROD_NOT_IN.value().equals(couponProdType)) {
            if (CollectionUtil.isEmpty(couponProdIds)) {
                return true;
            }
            if (couponProdIds.contains(prodId)) {
                return false;
            }
            return true;
        }
        if (CouponProdType.PRICECODE_IN.value().equals(couponProdType)) {
            if (CollectionUtil.isEmpty(priceCodes)) {
                return false;
            }
            if (priceCodes.contains(priceCode)) {
                return true;
            }
            return false;
        }
        if (CouponProdType.PRICECODE_NOT_IN.value().equals(couponProdType)) {
            if (CollectionUtil.isEmpty(priceCodes)) {
                return true;
            }
            if (priceCodes.contains(priceCode)) {
                return false;
            }
            return true;
        }

        return false;
    }


    /**
     * 计算优惠金额
     */
    private void calculateCouponReduce() {
        couponReduce = 0L;
        couponInOrderAmount = 0L;
        log.info("计算优惠金额，当前对象参数：{}", JSONObject.toJSONString(this));
        // 代金券
        if (!Objects.equals(chooseCoupon.getKind(), Short.valueOf("1"))) {
            log.info("chooseCoupon = {}", JSONUtil.toJsonStr(chooseCoupon));
            if (CouponType.CNM.value().equals(chooseCoupon.getCouponType())) {
                couponInOrderAmount = chooseCoupon.getReduceAmount();
            }
            // 折扣券
            //增加是否根据吊牌价抵扣
            if (CouponType.CND.value().equals(chooseCoupon.getCouponType())) {

                Long maxDeductionAmount = chooseCoupon.getMaxDeductionAmount();
                couponInOrderAmount = prodCanUseCouponActualTotal - PriceUtil.divideByBankerRounding((long) (prodCanUseCouponActualTotal * chooseCoupon.getCouponDiscount()), 10);
                //如果折扣券优惠金额 大于折扣券的最大可抵扣金额，重置优惠金额为最大可抵扣金额。
                if(maxDeductionAmount!=null && maxDeductionAmount>0 && maxDeductionAmount < couponInOrderAmount){
                    couponInOrderAmount = maxDeductionAmount;
                }
                chooseCoupon.setReduceAmount(couponInOrderAmount);
            }
            couponReduce = couponReduce + couponInOrderAmount;
        } else {
            couponReduce = chooseCoupon.getReduceAmount();
        }
        log.info("计算优惠金额结束，当前对象参数：{}", JSONObject.toJSONString(this));

        //计算购物车中参与分摊的商品的总支付金额
        Long actualTotal = shopCartItems.stream().filter(s-> s.getIsShareReduce()==1).mapToLong(ShopCartItemVO::getActualTotal).sum();
        log.info("参与分摊的商品的支付金额为：{},couponInOrderAmount为：{}",actualTotal,couponInOrderAmount);
        /**
         * 是否支持付款方式共用 0否1是 如果为是
         */
        if(chooseCoupon.getIssharePaytype()==1){
//            if( actualTotal > couponInOrderAmount ){
//                Assert.faild("订单金额超过优惠券抵扣金额！");
//            }
        }

        /**
         *  是否支持付款方式共用 0否1是 如果为否
         *  订单支付金额大于优惠券抵扣金额
         */
        if(chooseCoupon.getIssharePaytype()==0){
            if( actualTotal > couponInOrderAmount ){
                Assert.faild("当前优惠券不允许全额抵扣，请选择其它优惠券下单！");
            }
        }

    }

    /**
     * 设置分摊优惠金额
     */
    private void setShareReduce() {
//        @ApiModelProperty(value = "优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）")
//        private Short kind;
//
//        @ApiModelProperty(value = "价格类型（0：吊牌价/1：实付金额）")
//        private Short priceType;
        log.info("执行分摊逻辑，当前对象参数：{}", JSONObject.toJSONString(this));
        if (!Objects.equals(chooseCoupon.getKind(), Short.valueOf("1"))) {
            long sumReduce = 0L;

            long calculationAmount = couponInOrderAmount;
            //过滤掉不参加分摊的订单，只便利参加优惠券分摊的订单记录来计算订单分摊金额。
//            List<ShopCartItemVO> shareShopCartItems = shopCartItems.stream().filter(shopCartItemVO ->shopCartItemVO.getIsShareReduce()==1).collect(Collectors.toList());
            for (int i = 0; i < shopCartItems.size(); i++) {
                ShopCartItemVO shopCartItem = shopCartItems.get(i);
                long shareReduce;
                if (shopCartItem.getIsShareReduce() == 1) {
//                    shareReduce = calculationAmount * PriceUtil.divideByBankerRoundingThan(shopCartItem.getActualTotal(), prodCanUseCouponActualTotal, true);
                    //修复换算比率太小导致的bug
                    shareReduce = new BigDecimal(shopCartItem.getActualTotal()).divide(new BigDecimal(prodCanUseCouponActualTotal), 8, RoundingMode.HALF_EVEN)
                            .multiply(new BigDecimal(calculationAmount)).setScale(2,RoundingMode.HALF_EVEN).longValue();
                } else {
                    shareReduce = 0L;
                }
                // 分摊的优惠金额 不能大于商品金额
                long minShareReduce = Math.min(shareReduce, shopCartItem.getTotalAmount());
                //如果是最后一项，直接将剩余的优惠金额赋值给他
//                if (i + 1 == shopCartItems.size()) {
//                    minShareReduce = Math.min(couponReduce - sumReduce, shopCartItem.getTotalAmount());
//                }
                if(checkIsShareReduceLastCartItem(i)){
                    minShareReduce = Math.min(couponReduce - sumReduce, shopCartItem.getTotalAmount());
                }

                //如果当前款商品分摊后抵扣到0时，转换为一分钱 确保当前商品正常的支付以及退款流程
                if(shopCartItem.getActualTotal() == minShareReduce){
                    minShareReduce = shopCartItem.getActualTotal() - 1l;
                }

                if (Objects.equals(chooseCoupon.getShopId(), 0L)) {
                    // 平台分摊的每一个购物项优惠金额
                    shopCartItem.setPlatformShareReduce(shopCartItem.getPlatformShareReduce() + minShareReduce);
                    shopCartItem.setPlatformCouponAmount(minShareReduce);
                } else {
                    // 商家分摊的每一个购物项优惠金额
                    shopCartItem.setShareReduce(shopCartItem.getShareReduce() + minShareReduce);
                    shopCartItem.setShopCouponAmount(minShareReduce);
                }

                shopCartItem.setActualTotal(shopCartItem.getActualTotal() - minShareReduce);
                sumReduce = minShareReduce + sumReduce;

                // 将对应店铺优惠券优惠的金额放入map中
                if (shopReduceMap.containsKey(shopCartItem.getShopId())) {
                    Long shopReduce = shopReduceMap.get(shopCartItem.getShopId());
                    shopReduceMap.put(shopCartItem.getShopId(), shopReduce + minShareReduce);
                } else {
                    shopReduceMap.put(shopCartItem.getShopId(), minShareReduce);
                }
            }
            // 满减重新赋值
            couponReduce = sumReduce;
        } else {
            long sumReduce = Math.min(totalTransfee, couponReduce);
            totalTransfee = totalTransfee - sumReduce;
            couponReduce = sumReduce;
        }
        log.info("执行分摊逻辑结束，当前对象参数：{}", JSONObject.toJSONString(this));
    }

    /**
     * 检查是否是允许分摊优惠金额的最后一项
     */
    private Boolean checkIsShareReduceLastCartItem(int index){
        //如果当前商品已经是商品列表的的最后一件商品了，并且参与分摊 直接返回true 做减法计算分摊金额
        if (index + 1 == shopCartItems.size() && shopCartItems.get(index).getIsShareReduce()==1) {
            return true;
        }
        //如果当前商品已经是商品列表的的最后一件商品了，并且不参与分摊 直接返回false 不计算分摊金额
        if (index + 1 == shopCartItems.size() && shopCartItems.get(index).getIsShareReduce()==0) {
            return false;
        }
        //遍历剩下的商品，查询是否还有参与优惠券分摊的商品，如果有，直接返回false
        for (int i = index+1; i < shopCartItems.size(); i++) {
            ShopCartItemVO shopCartItemVO = shopCartItems.get(i);
            if(shopCartItemVO.getIsShareReduce()==1){
                return false;
            }
        }
    
        //走完上面的逻辑要再判断当前商品是否可以分摊金额
        if(shopCartItems.get(index).getIsShareReduce()==0) {
            return false;
        }
    
        //如果遍历完剩下的商品，也没有包含参加优惠券分摊的商品跳出当前判断方法，说明当前商品就是最后一件参与分摊的商品，直接返会true
        return true;
    }

//    private Long calShareReduceAmount(Long actualTotal){
//        //实际分摊金额
//        BigDecimal bactualTotal = new BigDecimal(actualTotal);
//        BigDecimal bprodCanUseCouponActualTotal = new BigDecimal(prodCanUseCouponActualTotal);
//        BigDecimal bcouponInOrderAmount = new BigDecimal(couponInOrderAmount);
//
//        bactualTotal.divide(bprodCanUseCouponActualTotal,6,ro)
//
//         BigDecimal(a1).divide(new BigDecimal(a2), 2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100))；
//    }
}
