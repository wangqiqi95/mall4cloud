package com.mall4j.cloud.discount.feign;

import com.google.common.collect.Lists;
import com.mall4j.cloud.api.discount.constant.DiscountRule;
import com.mall4j.cloud.api.discount.feign.DiscountFeignClient;
import com.mall4j.cloud.api.discount.vo.DiscountSumVO;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.product.vo.SpuDiscountAppVO;
import com.mall4j.cloud.common.product.vo.search.SpuDiscountItemAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.BooleanUtil;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.discount.manager.DiscountManager;
import com.mall4j.cloud.discount.service.DiscountService;
import com.mall4j.cloud.discount.vo.DiscountVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2020/12/14
 */
@RestController
public class DiscountFeignController implements DiscountFeignClient {

    @Autowired
    private DiscountManager discountManager;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private MapperFacade mapperFacade;


    @Override
    public ServerResponseEntity<List<ShopCartVO>> calculateDiscountAndMakeUpShopCart(List<ShopCartVO> shopCarts) {

        List<ShopCartVO> newShopCart = new ArrayList<>();
        for (ShopCartVO shopCart : shopCarts) {
            newShopCart.add(reBuildShopCart(shopCart, shopCart.getShopCartItemDiscounts().get(0).getShopCartItems(),true));
        }

        return ServerResponseEntity.success(newShopCart);
    }

    @Override
    public ServerResponseEntity<List<ShopCartVO>> calculateDiscountAndMakeUpShopCartByStoreId(Long storeId, ShopCartFlagVO shopCartFlagVO) {
        List<ShopCartVO> newShopCart = new ArrayList<>();
        for (ShopCartVO shopCart : shopCartFlagVO.getShopCarts()) {
            newShopCart.add(reBuildShopCart(storeId,shopCart, shopCart.getShopCartItemDiscounts().get(0).getShopCartItems(),shopCartFlagVO.getFlag()));
        }

        return ServerResponseEntity.success(newShopCart);
    }

    @Override
    public ServerResponseEntity<ShopCartWithAmountVO> calculateDiscountAndMakeUpShopCartAndAmount(ShopCartWithAmountVO shopCartWithAmountVO) {
        List<ShopCartVO> shopCarts = shopCartWithAmountVO.getShopCarts();
        Map<Long, ShopTransFeeVO> shopIdWithShopTransFee = null;
        if (Objects.nonNull(shopCartWithAmountVO.getUserDeliveryInfo())) {
            shopIdWithShopTransFee = shopCartWithAmountVO.getUserDeliveryInfo().getShopIdWithShopTransFee();
        }
        ServerResponseEntity<List<ShopCartVO>> calculateDiscountAndMakeUpShopCartResponse = calculateDiscountAndMakeUpShopCart(shopCarts);
        if (!calculateDiscountAndMakeUpShopCartResponse.isSuccess()) {
            return ServerResponseEntity.transform(calculateDiscountAndMakeUpShopCartResponse);
        }

        shopCarts = calculateDiscountAndMakeUpShopCartResponse.getData();

        long total = 0L;
        int count = 0;
        long reduce = 0L;
        long totalTransfee = 0L;
        long totalFreeTransfee = 0L;
        ShopCartWithAmountVO shopCartWithAmount = new ShopCartWithAmountVO();
        for (ShopCartVO shopCart : shopCarts) {
            List<ShopCartItemDiscountVO> shopCartItemDiscounts = shopCart.getShopCartItemDiscounts();

            for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartItemDiscounts) {
                List<ShopCartItemVO> shopCartItems = shopCartItemDiscount.getShopCartItems();
                ChooseDiscountItemVO chooseDiscountItemVO = shopCartItemDiscount.getChooseDiscountItem();
                if (Objects.nonNull(chooseDiscountItemVO)) {
                    Long needPiece = chooseDiscountItemVO.getNeedAmount();
                    // 当规则为满件打折时 9.5折就是95、9.5元就是950，此时数据库存的是件数*100，还需除以100进行判断
                    if (Objects.equals(DiscountRule.P2D.value(), chooseDiscountItemVO.getDiscountRule())) {
                        needPiece = PriceUtil.toDecimalPrice(needPiece).longValue();
                    }

                    // 如果满足优惠活动
                    if (Objects.equals(DiscountRule.M2M.value(), chooseDiscountItemVO.getDiscountRule()) && chooseDiscountItemVO.getNeedAmount() <= chooseDiscountItemVO.getProdsPrice()) {
                        //满钱减钱
                        reduce += chooseDiscountItemVO.getReduceAmount();
                    } else if (Objects.equals(DiscountRule.P2D.value(), chooseDiscountItemVO.getDiscountRule()) && needPiece <= chooseDiscountItemVO.getCount()) {
                        //满件打折
                        reduce += chooseDiscountItemVO.getReduceAmount();
                    }
                }

                for (ShopCartItemVO shopCartItem : shopCartItems) {
                    if (!BooleanUtil.isTrue(shopCartItem.getIsChecked())) {
                        continue;
                    }
                    count += shopCartItem.getCount();
                    total += shopCartItem.getTotalAmount();
                }
            }

            // 要计算运费
            if (shopIdWithShopTransFee != null && shopIdWithShopTransFee.containsKey(shopCart.getShopId())) {
                ShopTransFeeVO shopTransFeeVO = shopIdWithShopTransFee.get(shopCart.getShopId());

                // 店铺的实付 = 购物车实付 + 运费
                shopCart.setActualTotal(shopCart.getActualTotal() + shopTransFeeVO.getTransfee());
                // 店铺免运费金额
                shopCart.setFreeTransfee(shopTransFeeVO.getFreeTransfee());
                //店铺优惠金额 = 优惠金额  + 减免运费
                shopCart.setShopReduce(shopCart.getShopReduce() + shopTransFeeVO.getFreeTransfee());

                // 运费
                shopCart.setTransfee(shopTransFeeVO.getTransfee());
            } else {
                shopCart.setFreeTransfee(0L);
                shopCart.setTransfee(0L);
            }
            totalFreeTransfee += shopCart.getFreeTransfee();
            totalTransfee += shopCart.getTransfee();
        }

        shopCartWithAmount.setCount(count);
        shopCartWithAmount.setTotalMoney(total);
        shopCartWithAmount.setSubtractMoney(reduce + totalFreeTransfee);
        shopCartWithAmount.setFinalMoney(total + totalTransfee - reduce - totalFreeTransfee);
        shopCartWithAmount.setFreeTransfee(totalFreeTransfee);
        shopCartWithAmount.setFreightAmount(totalTransfee);
        shopCartWithAmount.setShopCarts(shopCarts);


        return ServerResponseEntity.success(shopCartWithAmount);
    }

    @Override
    public ServerResponseEntity<List<SpuDiscountAppVO>> spuDiscountList(Long shopId, Long spuId) {
        List<DiscountVO> discountList = discountService.spuDiscountList(shopId, spuId);
        List<SpuDiscountAppVO> spuDiscountList = new ArrayList<>();
        for (DiscountVO discountVO : discountList) {
            SpuDiscountAppVO spuDiscount = new SpuDiscountAppVO();
            spuDiscount.setDiscountId(discountVO.getDiscountId());
            spuDiscount.setDiscountName(discountVO.getDiscountName());
            spuDiscount.setDiscountRule(discountVO.getDiscountRule());
            spuDiscount.setDiscountType(discountVO.getDiscountType());
            spuDiscount.setMaxReduceAmount(discountVO.getMaxReduceAmount());
            spuDiscount.setDiscountItemList(mapperFacade.mapAsList(discountVO.getDiscountItemList(), SpuDiscountItemAppVO.class));
            spuDiscountList.add(spuDiscount);
        }
        return ServerResponseEntity.success(spuDiscountList);
    }

    @Override
    public ServerResponseEntity<Void> handleSpuOffline(List<Long> spuIds, List<Long> shopIds) {
        discountService.handleSpuOffline(spuIds, shopIds);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> hystrixCommandTest() {
        try {
            Thread.sleep(4000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ServerResponseEntity.success();
    }

    private ShopCartVO reBuildShopCart(Long storeId, ShopCartVO shopCart, List<ShopCartItemVO> shopCartItems, boolean flag) {
        // 购物车项顺序
        List<Long> cartOrderArr = shopCartItems.stream().map(ShopCartItemVO::getCartItemId).collect(Collectors.toList());
        // 订单项金额从小到大排序
        shopCartItems = shopCartItems.stream().sorted(Comparator.comparingDouble(ShopCartItemVO::getActualTotal)).collect(Collectors.toList());

        shopCart.setShopReduce(0L);
        shopCart.setDiscountReduce(0L);
        // 1.获取总的优惠金额
        // 2.将商品活动进行保存
        DiscountSumVO discountSum = discountManager.calculateDiscount(storeId, shopCartItems,flag);

        List<ShopCartItemDiscountVO> shopCartItemDiscountVOList = Lists.newArrayList();

        // 对数据经行组装
        // 通过活动项id划分,获取有活动的商品项
        Map<Long, List<ShopCartItemVO>> hasDiscountShopCartItemMap = shopCartItems.stream().collect(Collectors.groupingBy(ShopCartItemVO::getDiscountId));

        long reduce = 0L;
        long total = 0L;
        long actualTotal = 0L;
        int totalCount = 0;
        for (Long discountId : hasDiscountShopCartItemMap.keySet()) {
            // 获取该列表
            List<ShopCartItemVO> shopCartItemvos = hasDiscountShopCartItemMap.get(discountId);
            ChooseDiscountItemVO chooseDiscountItemVO = discountManager.getChooseDiscountItemVO(discountSum, discountId);


            long discountAmount = 0L;


            // 分摊优惠大于订单项金额的数量
            long shareReduceBiggerThenShopItemAmount = 0;
            // 计算分摊优惠金额
            for (int index = 0; index < shopCartItemvos.size(); index++) {

                ShopCartItemVO shopCartItem = shopCartItemvos.get(index);
                total += shopCartItem.getTotalAmount();
                totalCount += shopCartItem.getCount();

                if (!BooleanUtil.isTrue(shopCartItem.getIsChecked())) {
                    continue;
                }

                long shareReduce = 0L;
                if (Objects.isNull(chooseDiscountItemVO)) {
                    shopCartItem.setShareReduce(shareReduce);
                    shopCartItem.setActualTotal(shopCartItem.getActualTotal() - shareReduce);
                    actualTotal += shopCartItem.getActualTotal();
                    continue;
                }

                if (index + 1 == shopCartItems.size()) {
                    shareReduce = chooseDiscountItemVO.getReduceAmount() - discountAmount;
                    // 如果分到最后一项的钱太多了，比订单项目金额都要多，订单项的分摊金额就当作订单项金额咯
                    if (shareReduce > shopCartItem.getActualTotal()) {
                        shareReduceBiggerThenShopItemAmount = shareReduce - shopCartItem.getActualTotal();
                        shareReduce = shopCartItem.getActualTotal();
                        // 给得太多了，减少一点，以免变成负数
                        chooseDiscountItemVO.setReduceAmount(chooseDiscountItemVO.getReduceAmount() - shareReduceBiggerThenShopItemAmount);
                    }
                } else {
                    // 分摊金额 = 该优惠活动优惠金额 * (商品金额 / 参与该活动的商品总金额)
                    shareReduce = PriceUtil.divideByBankerRounding(chooseDiscountItemVO.getReduceAmount() * shopCartItem.getTotalAmount(), chooseDiscountItemVO.getProdsPrice());
                }
                // 第一个优惠就是满减
                shopCartItem.setShareReduce(shareReduce);
                shopCartItem.setDiscountAmount(shareReduce);
                shopCartItem.setActualTotal(shopCartItem.getActualTotal() - shareReduce);
                actualTotal += shopCartItem.getActualTotal();
                discountAmount += shareReduce;

            }

            // 不要把这行放到上面，因为上面会重新计算该值
            if (chooseDiscountItemVO != null) {
                reduce += chooseDiscountItemVO.getReduceAmount();
            }
            ShopCartItemDiscountVO shopCartItemDiscountVO = new ShopCartItemDiscountVO();
            // 把购物车项的商品顺序还原
            shopCartItemvos.sort(Comparator.comparingInt(prev -> cartOrderArr.indexOf(prev.getCartItemId())));

            shopCartItemDiscountVO.setShopCartItems(shopCartItemvos);
            shopCartItemDiscountVO.setChooseDiscountItem(chooseDiscountItemVO);
            shopCartItemDiscountVOList.add(shopCartItemDiscountVO);
        }

        // 倒序排序，将拥有优惠活动的商品放到最上面
        Collections.reverse(shopCartItemDiscountVOList);

        shopCart.setShopCartItemDiscounts(shopCartItemDiscountVOList);
        shopCart.setTotal(total);
        shopCart.setTotalCount(totalCount);
        shopCart.setActualTotal(actualTotal);
        shopCart.setDiscountReduce(reduce);
        // 最开始的店铺满减
        shopCart.setShopReduce(reduce);
        return shopCart;
    }

    private ShopCartVO reBuildShopCart(ShopCartVO shopCart, List<ShopCartItemVO> shopCartItems,boolean flag) {
        return reBuildShopCart(shopCart.getShopId(),shopCart,shopCartItems,flag);

    }


}
