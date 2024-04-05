package com.mall4j.cloud.api.product.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.feign.ShopCartFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.DeliveryModeBO;
import com.mall4j.cloud.common.order.dto.ShopCartItemDTO;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuAndSkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.BooleanUtil;
import com.mall4j.cloud.common.util.Json;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 购物车适配器
 *
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@Component
@Slf4j
public class ShopCartItemAdapter {

    @Autowired
    private SpuFeignClient spuFeignClient;

    @Autowired
    private ShopCartFeignClient shopCartFeignClient;

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StoreFeignClient storeFeignClient;

    /**
     * 获取购物项组装信息
     *
     * @param shopCartItemParam 购物项参数
     * @param storeId
     * @return 购物项组装信息
     */
    public List<ShopCartItemVO> getShopCartItems(ShopCartItemDTO shopCartItemParam, Long storeId,boolean flag) {
        List<ShopCartItemVO> shopCartItems;
        // 当立即购买时，没有提交的订单是没有购物车信息的
        if (shopCartItemParam != null) {
            shopCartItems = conversionShopCartItem(shopCartItemParam, null, storeId);
        }
        // 从购物车提交订单
        else {
            ServerResponseEntity<List<ShopCartItemVO>> checkedShopCartItemsResponse = shopCartFeignClient.getCheckedShopCartItems(storeId);
            if (!checkedShopCartItemsResponse.isSuccess()) {
                throw new LuckException(checkedShopCartItemsResponse.getMsg());
            }
            shopCartItems = checkedShopCartItemsResponse.getData();
        }

        // 请选择您需要的商品加入购物车
        if (CollectionUtil.isEmpty(shopCartItems)) {
            throw new LuckException(ResponseEnum.SHOP_CART_NOT_EXIST);
        }
        if (!flag) {
            log.info("商品价格：{}",shopCartItems);
            shopCartItems.forEach(item->{
                item.setSkuPriceFee(item.getMarketPriceFee());
                item.setPriceFee(item.getMarketPriceFee());
                item.setTotalPriceFee(item.getMarketTotalPrice());
                item.setTotalPrice(item.getMarketTotalPrice());
            });
            log.info("商品价格：{}",shopCartItems);
        }


        // 返回购物车选择的商品信息
        return shopCartItems;
    }


    /**
     * 将参数转换成组装好的购物项
     *
     * @param shopCartItemParamList 购物项参数
     * @param activityPriceFee      活动价
     * @return 组装好的购物项
     */
    public List<ShopCartItemVO> staffConversionShopCartItem(List<ShopCartItemDTO> shopCartItemParamList, Long activityPriceFee,boolean flag,Long storeId) {
        if (CollectionUtil.isEmpty(shopCartItemParamList)) {
            throw new LuckException(ResponseEnum.SHOP_CART_NOT_EXIST);
        }
        List<ShopCartItemVO> shopCartItemList = new ArrayList<>();
        shopCartItemParamList.forEach(shopCartItemParam -> {
            ServerResponseEntity<SpuAndSkuVO> spuAndSkuResponse = spuFeignClient.getSpuAndSkuById(shopCartItemParam.getSpuId(), shopCartItemParam.getSkuId(), storeId);
            if (!spuAndSkuResponse.isSuccess()) {
                throw new LuckException(spuAndSkuResponse.getMsg());
            }
            SkuVO sku = spuAndSkuResponse.getData().getSku();
            SpuVO spu = spuAndSkuResponse.getData().getSpu();

            // 拿到购物车的所有item
            ShopCartItemVO shopCartItem = new ShopCartItemVO();

            shopCartItem.setStyleCode(spu.getStyleCode());
            shopCartItem.setCategory(spu.getStyleCode());
            shopCartItem.setPriceCode(sku.getPriceCode());

            shopCartItem.setCartItemId(0L);
            shopCartItem.setSkuId(sku.getSkuId());
            shopCartItem.setCount(shopCartItemParam.getCount());
            shopCartItem.setSpuId(spu.getSpuId());
            shopCartItem.setIsCompose(spu.getIsCompose());
            shopCartItem.setCategoryId(spu.getCategoryId());
            shopCartItem.setScoreFee(sku.getScoreFee());
            if (Objects.equals(spu.getSpuType(), SpuType.SCORE.value())) {
                shopCartItem.setScorePrice(sku.getScoreFee() * shopCartItem.getCount());
            } else {
                shopCartItem.setScorePrice(Constant.ZERO_LONG);
            }
            shopCartItem.setDiscountId(0L);
            shopCartItem.setIsChecked(1);
//            shopCartItem.setSkuLangList(mapperFacade.mapAsList(sku.getSkuLangList(), OrderSkuLangVO.class));
//            shopCartItem.setSpuLangList(mapperFacade.mapAsList(spu.getSpuLangList(), OrderSpuLangVO.class));
            shopCartItem.setSkuName(sku.getSkuName());
            shopCartItem.setSpuName(spu.getName());
            shopCartItem.setPriceCode(sku.getPriceCode());
            shopCartItem.setChannelName(sku.getChannelName());
            shopCartItem.setImgUrl(BooleanUtil.isTrue(spu.getHasSkuImg()) ? sku.getImgUrl() : spu.getMainImgUrl());
            shopCartItem.setSkuPriceFee(sku.getPriceFee());
            shopCartItem.setStoreSkuStock(sku.getStoreSkuStock());
            shopCartItem.setStoreProtectPrice(sku.getStoreProtectPrice());
            shopCartItem.setSkuProtectPrice(sku.getSkuProtectPrice());
            shopCartItem.setMarketPriceFee(sku.getMarketPriceFee());
            shopCartItem.setPriceFee(sku.getPriceFee());
            shopCartItem.setTotalAmount(shopCartItem.getCount() * shopCartItem.getSkuPriceFee());
            shopCartItem.setMarketTotalPrice(shopCartItem.getCount() * sku.getMarketPriceFee());
            // 初始化商品实际金额
            shopCartItem.setActualTotal(shopCartItem.getTotalAmount());
            shopCartItem.setShareReduce(0L);
            // 如果活动价大于0则使用活动价格
            if (activityPriceFee != null && activityPriceFee > 0L) {
                // 商品实际金额,优惠金额放入店铺金额
                shopCartItem.setSkuPriceFee(activityPriceFee);
                shopCartItem.setActualTotal(shopCartItem.getCount() * activityPriceFee);
                long shareReduce = shopCartItem.getTotalAmount() - shopCartItem.getActualTotal() < 0 ? 0 : shopCartItem.getTotalAmount() - shopCartItem.getActualTotal();
                shopCartItem.setShareReduce(shareReduce);
            }

            shopCartItem.setDistributionUserId(shopCartItemParam.getDistributionUserId());
            shopCartItem.setCreateTime(new Date());
            // 物流配送信息
            shopCartItem.setDeliveryMode(spu.getDeliveryMode());
            shopCartItem.setDeliveryTemplateId(spu.getDeliveryTemplateId());

            DeliveryModeBO deliveryModeBO = Json.parseObject(shopCartItem.getDeliveryMode(), DeliveryModeBO.class);

            shopCartItem.setDeliveryModeBO(deliveryModeBO);
            shopCartItem.setShopId(spu.getShopId());
            shopCartItem.setWeight(sku.getWeight());
            shopCartItem.setVolume(sku.getVolume());
            shopCartItemList.add(shopCartItem);
        });
        if (!flag) {
            log.info("商品价格：{}",shopCartItemList);
            shopCartItemList.forEach(item->{
                item.setSkuPriceFee(item.getMarketPriceFee());
                item.setPriceFee(item.getMarketPriceFee());
                item.setTotalPriceFee(item.getMarketTotalPrice());
                item.setTotalPrice(item.getMarketTotalPrice());
            });
            log.info("商品价格：{}",shopCartItemList);
        }
        return shopCartItemList;
    }


    /**
     * 将参数转换成组装好的购物项
     *
     * @param shopCartItemParam 购物项参数
     * @param activityPriceFee  活动价
     * @param storeId
     * @return 组装好的购物项
     */
    public List<ShopCartItemVO> conversionShopCartItem(ShopCartItemDTO shopCartItemParam, Long activityPriceFee, Long storeId) {
        ServerResponseEntity<SpuAndSkuVO> spuAndSkuResponse = spuFeignClient.getSpuAndSkuById(shopCartItemParam.getSpuId(), shopCartItemParam.getSkuId(), storeId);
        if (!spuAndSkuResponse.isSuccess()) {
            throw new LuckException(spuAndSkuResponse.getMsg());
        }
        SkuVO sku = spuAndSkuResponse.getData().getSku();
        SpuVO spu = spuAndSkuResponse.getData().getSpu();

        // 拿到购物车的所有item
        ShopCartItemVO shopCartItem = new ShopCartItemVO();
        shopCartItem.setCartItemId(0L);
        shopCartItem.setSkuId(sku.getSkuId());
        shopCartItem.setCount(shopCartItemParam.getCount());
        shopCartItem.setSpuId(spu.getSpuId());
        shopCartItem.setIsCompose(spu.getIsCompose());
        shopCartItem.setCategoryId(spu.getCategoryId());
        shopCartItem.setScoreFee(sku.getScoreFee());

        shopCartItem.setPriceCode(sku.getPriceCode());
        shopCartItem.setChannelName(sku.getChannelName());
        shopCartItem.setStoreSkuStock(sku.getStoreSkuStock());
        shopCartItem.setStoreProtectPrice(sku.getStoreProtectPrice());
        shopCartItem.setSkuProtectPrice(sku.getSkuProtectPrice());
        shopCartItem.setStyleCode(spu.getStyleCode());
        shopCartItem.setCategory(spu.getStyleCode());
        shopCartItem.setPriceCode(sku.getPriceCode());

        if (Objects.equals(spu.getSpuType(), SpuType.SCORE.value())) {
            shopCartItem.setScorePrice(sku.getScoreFee() * shopCartItem.getCount());
        } else {
            shopCartItem.setScorePrice(Constant.ZERO_LONG);
        }
        shopCartItem.setDiscountId(0L);
        shopCartItem.setIsChecked(1);
        shopCartItem.setSpuName(spu.getName());
        shopCartItem.setSkuName(sku.getSkuName());

//        shopCartItem.setSkuLangList(mapperFacade.mapAsList(sku.getSkuLangList(), OrderSkuLangVO.class));
//        shopCartItem.setSpuLangList(mapperFacade.mapAsList(spu.getSpuLangList(), OrderSpuLangVO.class));

        shopCartItem.setImgUrl(BooleanUtil.isTrue(spu.getHasSkuImg()) ? sku.getImgUrl() : spu.getMainImgUrl());
        shopCartItem.setPriceFee(sku.getPriceFee());
        shopCartItem.setSkuPriceFee(sku.getPriceFee());
        shopCartItem.setTotalAmount(shopCartItem.getCount() * shopCartItem.getSkuPriceFee());
        // 初始化商品实际金额
        shopCartItem.setActualTotal(shopCartItem.getTotalAmount());
        shopCartItem.setMarketPriceFee(sku.getMarketPriceFee());
        shopCartItem.setMarketTotalPrice(shopCartItem.getCount() * sku.getMarketPriceFee());
        log.info("conversionShopCartItem1  shopCartItem = {}", JSONObject.toJSONString(shopCartItem));
        shopCartItem.setShareReduce(0L);

        if(isInviteStore(storeId)){//虚拟门店
            //有保护价不参与活动价(无库存取价逻辑)
            if(sku.getSkuProtectPrice()>0){
                activityPriceFee=0L;
            }
        }else{
            //剔除门店无库存(取官店价格)、门店有库存且有保护价(取保护价) -> sku,不参与活动价
            if(sku.getStoreSkuStock()<=0 || (sku.getStoreProtectPrice()>0 && sku.getStoreSkuStock()>0)){
                activityPriceFee=0L;
            }
        }

        // 如果活动价大于0则使用活动价格
        if (activityPriceFee != null && activityPriceFee > 0L) {
            // 商品实际金额,优惠金额放入店铺金额
            shopCartItem.setSkuPriceFee(activityPriceFee);
            shopCartItem.setActualTotal(shopCartItem.getCount() * activityPriceFee);
            long shareReduce = shopCartItem.getTotalAmount() - shopCartItem.getActualTotal() < 0 ? 0 : shopCartItem.getTotalAmount() - shopCartItem.getActualTotal();
            shopCartItem.setShareReduce(shareReduce);
        }
        log.info("conversionShopCartItem2  shopCartItem = {}", JSONObject.toJSONString(shopCartItem));

        shopCartItem.setDistributionUserId(shopCartItemParam.getDistributionUserId());
        shopCartItem.setCreateTime(new Date());
        // 物流配送信息
        shopCartItem.setDeliveryMode(spu.getDeliveryMode());
        shopCartItem.setDeliveryTemplateId(spu.getDeliveryTemplateId());

        DeliveryModeBO deliveryModeBO = Json.parseObject(shopCartItem.getDeliveryMode(), DeliveryModeBO.class);

        shopCartItem.setDeliveryModeBO(deliveryModeBO);
        shopCartItem.setShopId(spu.getShopId());
        shopCartItem.setWeight(sku.getWeight());
        shopCartItem.setVolume(sku.getVolume());
        return Collections.singletonList(shopCartItem);
    }

    private boolean isInviteStore(Long storeId){
        ServerResponseEntity<Boolean> response=storeFeignClient.isInviteStore(storeId);
        if(response.isSuccess() && Objects.nonNull(response.getData()) && response.getData()){
            return true;
        }
        return false;
    }

}
