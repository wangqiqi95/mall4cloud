package com.mall4j.cloud.user.manager;

import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.common.constant.ConfigNameConstant;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.vo.ShopCartItemDiscountVO;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.BooleanUtil;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.service.UserExtensionService;
import com.mall4j.cloud.user.vo.ScoreCompleteConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户订单积分抵扣优惠
 * @author FrozenWatermelon
 * @date 2021/05/10
 */
@Component
public class UserScoreOrderManager {

    @Autowired
    private ConfigFeignClient configFeignClient;
    @Autowired
    private UserExtensionService userExtensionService;


    public void calculateScoreDiscount(ShopCartOrderMergerVO shopCartOrderMerger) {

        Long userId;
        if (null != shopCartOrderMerger.getUserId()){
            userId = shopCartOrderMerger.getUserId();
        } else {
            userId = AuthUserContext.get().getUserId();
        }
        UserExtension userExtension = userExtensionService.getByUserId(userId);
        // 积分订单处理
        if (Objects.equals(shopCartOrderMerger.getOrderType(), OrderType.SCORE)) {
            // 积分订单不需要处理积分抵扣， 直接返回
            shopCartOrderMerger.setUserHasScore(userExtension.getScore());
            return;
        }
        // 积分成长值设置
        String scoreConfigValue = configFeignClient.getConfig(ConfigNameConstant.SCORE_CONFIG).getData();
        ScoreCompleteConfigVO scoreParam = Json.parseObject(scoreConfigValue, ScoreCompleteConfigVO.class);
        // 如果未开启积分抵扣，那么直接返回
        if(scoreParam.getShoppingScoreSwitch() != null && !scoreParam.getShoppingScoreSwitch()){
            return;
        }

        // 用户可用积分
        Long userHasScore = userExtension.getScore();
        shopCartOrderMerger.setUserHasScore(userHasScore);

        // 用户使用积分
        Long usableScore = shopCartOrderMerger.getUsableScore();
        // 如果没有说明使用多少积分或要求使用的积分大于用户已有的积分，则自动使用全部用户已有积分
        if (usableScore == null || usableScore > userHasScore) {
            usableScore = userHasScore;
        }

        // 购物积分抵现比例(x积分抵扣1元）
        Long shoppingUseScoreRatio = Objects.isNull(scoreParam.getShoppingUseScoreRatio()) ? 0L : scoreParam.getShoppingUseScoreRatio();
        if(shoppingUseScoreRatio == 0){
            return;
        }
        shopCartOrderMerger.setShoppingUseScoreRatio(shoppingUseScoreRatio);

        // 用户使用的可抵现金额(分) 用户有99积分，但是有一种情况，平台规定满10积分才能兑换成1分钱
        // 那么不能四舍五入的任务可以换10分，而是向下取整认为是可以使用9分
        long userOffsetCashAmount = usableScore * 100 / shoppingUseScoreRatio;

        // 用上面计算结果向下取整的值，乘原来的比例，算可用多少积分
        usableScore = userOffsetCashAmount * shoppingUseScoreRatio / 100;
        shopCartOrderMerger.setUsableScore(usableScore);



        // 平台使用积分分类比例上限
        double useRatioLimit = Objects.isNull(scoreParam.getUseRatioLimit()) ? 0.0 : scoreParam.getUseRatioLimit();

        // 最大可抵现金额 = ((订单实际金额 - 订单最小金额) * 使用积分分类比例上限)
        long totalActualAmount = shopCartOrderMerger.getActualTotal() - shopCartOrderMerger.getTotalTransfee() - Constant.MIN_ORDER_AMOUNT;
        Long orderMaxCanOffsetCashAmount = PriceUtil.divideByBankerRounding((long)Arith.mul(totalActualAmount, useRatioLimit), 100);

        // 积分在订单中占优惠的比例(这个比例是百分比，比如10.5%，这里就是10.5)，对会员等级折扣来说，打折x
        double scoreAmountInOrder;

        long actualTotalReduce;
        // 用户选择的可抵现金额大于订单最大可使用订单可使用的
        if (userOffsetCashAmount >= orderMaxCanOffsetCashAmount) {
            // 使用积分比例，就是优惠比例
            scoreAmountInOrder = useRatioLimit;
            // 实际上使用积分优惠，优惠的金额(积分抵扣金额)
            actualTotalReduce = PriceUtil.divideByBankerRounding((long)Arith.mul(totalActualAmount, scoreAmountInOrder) , 100);
        }
        // 用户的积分比较少的时候
        else {
            scoreAmountInOrder = Arith.div(Arith.mul(userOffsetCashAmount, useRatioLimit), orderMaxCanOffsetCashAmount);
            actualTotalReduce = userOffsetCashAmount;
        }


        // 最多可以使用多少积分
        shopCartOrderMerger.setMaxUsableScore(orderMaxCanOffsetCashAmount * shoppingUseScoreRatio / 100);

        // 用户选择不使用积分，直接返回
        if (!BooleanUtil.isTrue(shopCartOrderMerger.getIsScorePay())) {
            return;
        }
        // 再次校验分摊金额
        verifyAmount(shopCartOrderMerger, scoreAmountInOrder, actualTotalReduce,shoppingUseScoreRatio);
    }

    /**
     * 再次校验分摊金额
     * @param shopCartOrderMerger 购物车信息
     * @param scoreAmountInOrder 积分在订单中占优惠的比例
     * @param actualTotalReduce  实际上使用积分优惠
     * @param shoppingUseScoreRatio 购物积分抵现比例(x积分抵扣1元）
     */
    private void verifyAmount(ShopCartOrderMergerVO shopCartOrderMerger, double scoreAmountInOrder, long actualTotalReduce, Long shoppingUseScoreRatio) {
        List<ShopCartOrderVO> shopCartOrders = shopCartOrderMerger.getShopCartOrders();
        List<ShopCartItemVO> allShopCartItem = new ArrayList<>();
        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
            List<ShopCartItemDiscountVO> shopCartItemDiscounts = shopCartOrder.getShopCartItemDiscounts();
            for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartItemDiscounts) {
                List<ShopCartItemVO> shopCartItems = shopCartItemDiscount.getShopCartItems();
                allShopCartItem.addAll(shopCartItems);
            }
        }

        // 订单项按金额从小到大
        allShopCartItem.sort(Comparator.comparingDouble(ShopCartItemVO::getActualTotal));

        long discountAmount = 0L;
        for (int i = 0; i < allShopCartItem.size(); i++) {
            ShopCartItemVO shopCartItem = allShopCartItem.get(i);
            long itemDiscount;
            // 最后一项
            if (i == allShopCartItem.size() - 1) {
                itemDiscount = actualTotalReduce - discountAmount;
                // 如果分到最后一项的钱太多了，比订单项目金额都要多，订单项的分摊金额就当作订单项金额咯
                if (itemDiscount > shopCartItem.getActualTotal()) {
                    itemDiscount = shopCartItem.getActualTotal();
                }
            }
            // 非最后一项
            else {
                itemDiscount = PriceUtil.divideByBankerRounding((long) Arith.mul(shopCartItem.getActualTotal(), scoreAmountInOrder), 100);

            }
            Long useScore = PriceUtil.divideByBankerRounding((long) Arith.mul(shoppingUseScoreRatio, itemDiscount), 100);
            // 非最后一项 这里金额重新计算了一遍，请勿随意修改顺序
            if (i != allShopCartItem.size() - 1){
                discountAmount += PriceUtil.divideByBankerRounding(useScore * 100 , shoppingUseScoreRatio);
            }


            shopCartItem.setScoreReduce(PriceUtil.divideByBankerRounding(useScore * 100 , shoppingUseScoreRatio));
            shopCartItem.setScorePrice(useScore);
            shopCartItem.setActualTotal(shopCartItem.getActualTotal() - shopCartItem.getScoreReduce());
            shopCartItem.setPlatformShareReduce(shopCartItem.getPlatformShareReduce() + shopCartItem.getScoreReduce());
        }
        Map<Long, List<ShopCartItemVO>> shopCartOrderMap = allShopCartItem.stream().collect(Collectors.groupingBy(ShopCartItemVO::getShopId));
        long totalScoreAmount = 0L;
        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
            List<ShopCartItemVO> shopCartItems = shopCartOrderMap.get(shopCartOrder.getShopId());
            long scoreReduce = 0;

            for (ShopCartItemVO shopCartItem : shopCartItems) {
                scoreReduce += shopCartItem.getScoreReduce();
            }
            shopCartOrder.setScoreReduce(scoreReduce);
            shopCartOrder.setActualTotal(shopCartOrder.getActualTotal() - scoreReduce);
            long orderScoreReduce = Objects.isNull(shopCartOrder.getPlatformAmount()) ? scoreReduce :shopCartOrder.getPlatformAmount() + scoreReduce;
            shopCartOrder.setPlatformAmount(orderScoreReduce);
            shopCartOrder.setShopReduce(shopCartOrder.getShopReduce() + scoreReduce);
            totalScoreAmount += scoreReduce;
        }
        shopCartOrderMerger.setTotalScoreAmount(totalScoreAmount);
        shopCartOrderMerger.setOrderReduce(shopCartOrderMerger.getOrderReduce() + totalScoreAmount);
        shopCartOrderMerger.setActualTotal(shopCartOrderMerger.getActualTotal() - totalScoreAmount);
    }
}
