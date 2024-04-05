package com.mall4j.cloud.user.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.constant.ShopType;
import com.mall4j.cloud.common.order.vo.ShopCartItemDiscountVO;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.user.constant.DiscountRangeEnum;
import com.mall4j.cloud.user.constant.FreeFeeTypeEnum;
import com.mall4j.cloud.user.constant.LevelTypeEnum;
import com.mall4j.cloud.user.constant.RightsTypeEnum;
import com.mall4j.cloud.user.service.UserLevelService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.vo.UserLevelVO;
import com.mall4j.cloud.user.vo.UserRightsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2021/5/12
 */
@Component
public class UserLevelOrderManager {

    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private UserService userService;

    public void calculateLevelDiscount(ShopCartOrderMergerVO shopCartOrderMerger) {
        // 积分订单不享受会员权益， 直接返回
        if (Objects.equals(shopCartOrderMerger.getOrderType(), OrderType.SCORE)) {
            return;
        }
        Long userId;
        if (null != shopCartOrderMerger.getUserId()){
            userId = shopCartOrderMerger.getUserId();
        } else {
            userId = AuthUserContext.get().getUserId();
        }
        // 缓存获取用户信息
        UserApiVO user = userService.getByUserId(userId);
        // 缓存获取用户对应的等级信息
        UserLevelVO userLevelInfo = userLevelService.getOneByTypeAndLevel(LevelTypeEnum.ORDINARY_USER.value(), user.getLevel());
        List<UserRightsVO> userRightsList = userLevelInfo.getUserRightsList();
        if (Objects.nonNull(user.getVipLevel())) {
            // 付费会员的权益
            UserLevelVO payLevelInfo = userLevelService.getOneByTypeAndLevel(LevelTypeEnum.PAY_USER.value(), user.getVipLevel());
            if (CollectionUtil.isNotEmpty(payLevelInfo.getUserRightsList())) {
                userRightsList.addAll(payLevelInfo.getUserRightsList());
            }
        }
        // 会员没有权益，不用计算
        if (CollectionUtil.isEmpty(userRightsList)) {
            return;
        }
        UserRightsVO discountRight = new UserRightsVO();
        UserRightsVO freeFeeRight = new UserRightsVO();
        // 循环查找最优惠的折扣以及包邮权益【范围下，选择折扣更低的】
        for (UserRightsVO userRightsVO : userRightsList) {
            // 会员折扣
            discountRight = getDiscountRightsVO(discountRight, userRightsVO);
            // 包邮
            freeFeeRight = getFreeFeeRightsVO(freeFeeRight, userRightsVO);
        }
        if (Objects.nonNull(discountRight.getRightsType())) {
            calculateMemberDiscount(discountRight, shopCartOrderMerger);
        }
        if (Objects.nonNull(freeFeeRight.getRightsType())) {
            calculateFreeFee(freeFeeRight, shopCartOrderMerger);
        }
    }

    private UserRightsVO getFreeFeeRightsVO(UserRightsVO freeFeeRight, UserRightsVO userRightsVO) {
        if (Objects.equals(userRightsVO.getRightsType(), RightsTypeEnum.FREE_FEE_TYPE.value())) {
            if (Objects.isNull(freeFeeRight.getRightsType())) {
                freeFeeRight = userRightsVO;
            } else if (Objects.nonNull(freeFeeRight.getRightsType()) && Objects.equals(userRightsVO.getFreeFeeType(), FreeFeeTypeEnum.ALL_PLATFORMS_FREE.value())) {
                freeFeeRight = userRightsVO;
            }
        }
        return freeFeeRight;
    }

    private UserRightsVO getDiscountRightsVO(UserRightsVO discountRight, UserRightsVO userRightsVO) {
        if (Objects.equals(userRightsVO.getRightsType(), RightsTypeEnum.MEMBER_DISCOUNT.value())) {
            if (Objects.isNull(discountRight.getRightsType())) {
                discountRight = userRightsVO;
            } else if (Objects.nonNull(discountRight.getRightsType()) && discountRight.getDiscount() > userRightsVO.getDiscount()) {
                discountRight = userRightsVO;
            }
        }
        return discountRight;
    }

    private void calculateFreeFee(UserRightsVO userRightsVO, ShopCartOrderMergerVO shopCartOrderMerger) {
        List<ShopCartOrderVO> shopCartOrders = shopCartOrderMerger.getShopCartOrders();
        long totalFreeTransfee = 0L;
        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
            // 是否为自营店 + 自营店包邮
            if (Objects.equals(FreeFeeTypeEnum.SELF_OPERATED_STORE_FREE.value(), userRightsVO.getFreeFeeType())
                    && !Objects.equals(shopCartOrder.getShopType(), ShopType.SELF_SHOP.value())) {
                continue;
            }
            // 运费
            // 注意：这里减免运费金额没有减少运费金额，如果需要改变这里记得改变下退款判断
            Long transfee = shopCartOrder.getTransfee();
            totalFreeTransfee += transfee;
            shopCartOrder.setLevelFreeTransfee(transfee);
            shopCartOrder.setActualTotal(shopCartOrder.getActualTotal() - transfee);
            long orderLevelReduce = Objects.isNull(shopCartOrder.getPlatformAmount()) ? transfee : shopCartOrder.getPlatformAmount() + transfee;
            shopCartOrder.setPlatformAmount(orderLevelReduce);
            shopCartOrder.setShopReduce(shopCartOrder.getShopReduce() + transfee);
        }
        shopCartOrderMerger.setFreeTransfee(totalFreeTransfee);

    }

    /**
     * 计算会员折扣
     */
    private void calculateMemberDiscount(UserRightsVO userRightsVO, ShopCartOrderMergerVO shopCartOrderMerger) {

        List<ShopCartOrderVO> shopCartOrders = shopCartOrderMerger.getShopCartOrders();

        // 进行折扣计算的总金额
        long totalFee = 0L;
        List<ShopCartItemVO> allShopCartItem = new ArrayList<>();
        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
            //如果可用范围为自营店不为当前店铺直接下一次循环
            if (Objects.equals(DiscountRangeEnum.SELF_OPERATED_STORE.value(), userRightsVO.getDiscountRange()) && !Objects.equals(shopCartOrder.getShopType(), ShopType.SELF_SHOP.value())) {
                continue;
            }
            List<ShopCartItemDiscountVO> shopCartItemDiscounts = shopCartOrder.getShopCartItemDiscounts();
            for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartItemDiscounts) {
                List<ShopCartItemVO> shopCartItems = shopCartItemDiscount.getShopCartItems();
                for (ShopCartItemVO shopCartItem : shopCartItems) {
                    totalFee = totalFee + shopCartItem.getActualTotal();
                    allShopCartItem.add(shopCartItem);
                }
            }
        }

        // 优惠金额 这个九五折是95 九折是90
        long totalReduce = PriceUtil.divideByBankerRounding(totalFee * (100 - userRightsVO.getDiscount()), 100);

        if (CollectionUtil.isEmpty(allShopCartItem)) {
            return;
        }
        // 订单项按金额从小到大
        allShopCartItem.sort(Comparator.comparingDouble(ShopCartItemVO::getActualTotal));

        long discountAmount = 0L;
        for (int i = 0; i < allShopCartItem.size(); i++) {
            ShopCartItemVO shopCartItem = allShopCartItem.get(i);
            long itemDiscount;
            // 最后一项
            if (i == allShopCartItem.size() - 1) {
                itemDiscount = totalReduce - discountAmount;
                // 如果分到最后一项的钱太多了，比订单项目金额都要多，订单项的分摊金额就当作订单项金额咯
                if (itemDiscount > shopCartItem.getActualTotal()) {
                    itemDiscount = shopCartItem.getActualTotal();
                }
            }
            // 非最后一项
            else {
                itemDiscount = PriceUtil.divideByBankerRounding(shopCartItem.getActualTotal() * (100 - userRightsVO.getDiscount()), 100);
                discountAmount += itemDiscount;
            }
            shopCartItem.setLevelReduce(itemDiscount);
            shopCartItem.setActualTotal(shopCartItem.getActualTotal() - shopCartItem.getLevelReduce());
            shopCartItem.setPlatformShareReduce(shopCartItem.getPlatformShareReduce() + shopCartItem.getLevelReduce());
        }

        Map<Long, List<ShopCartItemVO>> shopCartOrderMap = allShopCartItem.stream().collect(Collectors.groupingBy(ShopCartItemVO::getShopId));

        long totalLevelAmount = 0L;
        // 重算一遍分摊金额
        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
            //如果可用范围为自营店不为当前店铺直接下一次循环
            if (Objects.equals(DiscountRangeEnum.SELF_OPERATED_STORE.value(), userRightsVO.getDiscountRange()) && !Objects.equals(shopCartOrder.getShopType(), ShopType.SELF_SHOP.value())) {
                continue;
            }
            List<ShopCartItemVO> shopCartItems = shopCartOrderMap.get(shopCartOrder.getShopId());
            long levelReduce = 0;

            for (ShopCartItemVO shopCartItem : shopCartItems) {
                levelReduce += shopCartItem.getLevelReduce();
            }
            shopCartOrder.setLevelReduce(levelReduce);
            shopCartOrder.setActualTotal(shopCartOrder.getActualTotal() - levelReduce);
            shopCartOrder.setShopReduce(shopCartOrder.getShopReduce() + levelReduce);
            long orderLevelReduce = Objects.isNull(shopCartOrder.getPlatformAmount()) ? levelReduce : shopCartOrder.getPlatformAmount() + levelReduce;
            shopCartOrder.setPlatformAmount(orderLevelReduce);

            totalLevelAmount += levelReduce;
        }

        shopCartOrderMerger.setTotalLevelAmount(totalLevelAmount);
        shopCartOrderMerger.setActualTotal(shopCartOrderMerger.getActualTotal() - totalLevelAmount);
    }
}
