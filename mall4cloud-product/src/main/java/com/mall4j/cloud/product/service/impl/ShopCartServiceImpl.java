package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.group.feign.TimeDiscountFeignClient;
import com.mall4j.cloud.api.group.feign.dto.TimeDiscountActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.product.dto.SpuSkuPriceDTO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.product.util.ProductLangUtil;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.CacheManagerUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.shopcart.ChangeShopCartItemDTO;
import com.mall4j.cloud.product.dto.shopcart.CheckShopCartItemDTO;
import com.mall4j.cloud.product.mapper.ShopCartItemMapper;
import com.mall4j.cloud.product.model.ShopCartItem;
import com.mall4j.cloud.product.service.ShopCartService;
import com.mall4j.cloud.product.service.SpuSkuPricingPriceService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 购物车
 *
 * @author FrozenWatermelon
 * @date 2020-11-20 15:47:32
 */
@Slf4j
@Service
public class ShopCartServiceImpl implements ShopCartService {

    @Autowired
    private ShopCartItemMapper shopCartItemMapper;

    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;

    @Autowired
    private CacheManagerUtil cacheManagerUtil;

    @Autowired
    private TimeDiscountFeignClient timeDiscountFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private SpuSkuPricingPriceService spuSkuPricingPriceService;
    @Autowired
    private StoreFeignClient storeFeignClient;

    @Override
    @CacheEvict(cacheNames = CacheNames.SHOP_CART_ITEM_COUNT, key = "#userId")
    public void deleteShopCartItemsByShopCartItemIds(Long userId, List<Long> shopCartItemIds) {
        if (CollectionUtil.isEmpty(shopCartItemIds)) {
            return;
        }
        shopCartItemMapper.deleteShopCartItemsByShopCartItemIds(userId, shopCartItemIds);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SHOP_CART_ITEM_COUNT, key = "#userId")
    public void addShopCartItem(Long userId, ChangeShopCartItemDTO param, Long priceFee, Long categoryId) {
        ShopCartItem shopCartItem = new ShopCartItem();
        shopCartItem.setCount(param.getCount());
        shopCartItem.setSpuId(param.getSpuId());
        shopCartItem.setShopId(param.getShopId());
        shopCartItem.setUserId(userId);
        shopCartItem.setSkuId(param.getSkuId());
        shopCartItem.setCategoryId(categoryId);
        shopCartItem.setDistributionUserId(param.getDistributionUserId());
        shopCartItem.setDiscountId(0L);
        shopCartItem.setIsChecked(1);
        shopCartItem.setPriceFee(priceFee);
        shopCartItemMapper.save(shopCartItem);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SHOP_CART_ITEM_COUNT, key = "#userId")
    public void updateShopCartItem(Long userId, ShopCartItem shopCartItem) {
        shopCartItemMapper.update(shopCartItem);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SHOP_CART_ITEM_COUNT, key = "#userId")
    public void deleteAllShopCartItems(Long userId) {
        shopCartItemMapper.deleteAllShopCartItems(userId);
    }

    @Override
    public List<ShopCartItemVO> getShopCartItems(Long storeId) {
        Long userId = AuthUserContext.get().getUserId();
        List<ShopCartItemVO> shopCartItems = shopCartItemMapper.getShopCartItems(userId, false, null, I18nMessage.getLang(),storeId);

        //增加价取价逻辑(小程序配置的活动价)
        List<SpuSkuPriceDTO> skuPriceDTOS = mapperFacade.mapAsList(shopCartItems,SpuSkuPriceDTO.class);
        List<SkuTimeDiscountActivityVO> activityVOList=spuSkuPricingPriceService.getStoreSpuAndSkuPrice(storeId,skuPriceDTOS);
        if(activityVOList.size()>0){
            Map<Long, SkuTimeDiscountActivityVO> timeDisCountPriceMap = activityVOList.stream().collect(Collectors.toMap(SkuTimeDiscountActivityVO::getSkuId, timeDiscountActivityVO -> timeDiscountActivityVO));
            shopCartItems.forEach(shopCartItemVO -> {
                SkuTimeDiscountActivityVO skuPrice = timeDisCountPriceMap.get(shopCartItemVO.getSkuId());
                if (skuPrice != null) {
                    shopCartItemVO.setSkuPriceFee(skuPrice.getPrice());
                    shopCartItemVO.setFriendlyCouponUseFlag(skuPrice.getFriendlyCouponUseFlag());
                    shopCartItemVO.setFriendlyDiscountFlag(skuPrice.getFriendlyDiscountFlag());
                    shopCartItemVO.setFriendlyFlag(skuPrice.isMemberPriceFlag()==true?1:0);
                    shopCartItemVO.setInvateStorePriceFlag(skuPrice.isInvateStorePriceFlag());
                    shopCartItemVO.setInvateStoreActivityId(skuPrice.getActivityId());
                }
            });
        }

        for (ShopCartItemVO shopCartItem : shopCartItems) {
            shopCartItem.setTotalAmount(shopCartItem.getCount() * shopCartItem.getSkuPriceFee());
            shopCartItem.setActualTotal(shopCartItem.getTotalAmount());
//            OrderLangUtil.shopCartItemLang(shopCartItem);
            shopCartItem.setSpuLangList(null);
            shopCartItem.setSkuLangList(null);
        }
        return shopCartItems;
    }

    @Override
    public List<ShopCartItemVO> getShopCartExpiryItems(Long storeId) {
        Long userId = AuthUserContext.get().getUserId();
        List<ShopCartItemVO> shopCartItems = shopCartItemMapper.getShopCartItems(userId, true, null, I18nMessage.getLang(), storeId);
//        ProductLangUtil.shopCartItemList(shopCartItems);
        for (ShopCartItemVO shopCartItem : shopCartItems) {
            shopCartItem.setTotalAmount(shopCartItem.getCount() * shopCartItem.getSkuPriceFee());
            shopCartItem.setActualTotal(shopCartItem.getTotalAmount());
        }
        return shopCartItems;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SHOP_CART_ITEM_COUNT, key = "#userId")
    public Integer getShopCartItemCount(Long userId) {
        return shopCartItemMapper.getShopCartItemCount(userId);
    }

    @Override
    public List<ShopCartItemVO> getCheckedShopCartItems(Long storeId) {
        Long userId = AuthUserContext.get().getUserId();
        List<ShopCartItemVO> shopCartItemVOS=shopCartItemMapper.getShopCartItems(userId, false, true, I18nMessage.getLang(), storeId);

        if(CollectionUtil.isNotEmpty(shopCartItemVOS)){
            ServerResponseEntity<Boolean> inviteStoreResponse=storeFeignClient.isInviteStore(storeId);
            if(inviteStoreResponse.isSuccess() && Objects.nonNull(inviteStoreResponse.getData()) && inviteStoreResponse.getData()){
                shopCartItemVOS.stream().forEach(itemVo ->{
                    if(itemVo.getSkuProtectPrice()<=0){
                        itemVo.setPriceFee(itemVo.getMarketPriceFee());//虚拟门店没有保护价默认取吊牌价
                        itemVo.setSkuPriceFee(itemVo.getMarketPriceFee());//虚拟门店没有保护价默认取吊牌价
                    }
                });
            }
        }
        return shopCartItemVOS;
    }

    @Override
    public void removeShopCartItemCache(Long spuId) {
        List<String> userIds = shopCartItemMapper.listUserIdBySpuId(spuId);
        if (CollectionUtil.isEmpty(userIds)) {
            return;
        }
        for (String userId : userIds) {
            cacheManagerUtil.evictCache(CacheNames.SHOP_CART_ITEM_COUNT, userId);
        }
    }

    @Override
    public void checkShopCartItems(Long userId, List<CheckShopCartItemDTO> params) {
        shopCartItemMapper.checkShopCartItems(userId, params);
    }

    @Override
    public void updateIsClosedByShopIds(List<Long> shopIds, Integer isClosed) {
        if (shopIds == null || shopIds.size() == 0) {
            return;
        }
        shopCartItemMapper.updateIsCloseByShopIds(shopIds, isClosed);
    }

}
