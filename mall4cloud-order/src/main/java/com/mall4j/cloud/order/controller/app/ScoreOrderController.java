package com.mall4j.cloud.order.controller.app;


import com.mall4j.cloud.api.delivery.dto.CalculateAndGetDeliverInfoDTO;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.order.manager.ConfirmOrderManager;
import com.mall4j.cloud.api.product.manager.ShopCartAdapter;
import com.mall4j.cloud.api.product.manager.ShopCartItemAdapter;
import com.mall4j.cloud.api.user.feign.UserLevelAndScoreOrderFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.dto.OrderDTO;
import com.mall4j.cloud.common.order.util.OrderLangUtil;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 积分商城接口
 *
 * @author YXF
 * @date 2021-08-24
 */
@RestController
@RequestMapping("/score_order" )
@Api(tags = "积分商品订单接口")
public class ScoreOrderController {

    @Autowired
    private ShopCartAdapter shopCartAdapter;
    @Autowired
    private ConfirmOrderManager confirmOrderManager;
    @Autowired
    private ShopCartItemAdapter shopCartItemAdapter;
    @Autowired
    private DeliveryFeignClient deliveryFeignClient;
    @Autowired
    private UserLevelAndScoreOrderFeignClient userLevelAndScoreOrderFeignClient;

    /**
     * 积分商品购买
     */
    @PostMapping("/confirm")
    @ApiOperation(value = "购买，生成订单信息", notes = "传入下单所需要的参数进行下单")
    public ServerResponseEntity<ShopCartOrderMergerVO> confirm(@Valid @RequestBody OrderDTO orderParam) {
        if (Objects.isNull(orderParam.getShopCartItem())) {
            throw new LuckException("shopCartItem不能为空");
        }
        Long userId = AuthUserContext.get().getUserId();
        // 将要返回给前端的完整的订单信息
        ShopCartOrderMergerVO shopCartOrderMerger = new ShopCartOrderMergerVO();
        // 积分商品只有快递发货
        shopCartOrderMerger.setDvyType(DeliveryType.DELIVERY.value());
        shopCartOrderMerger.setOrderType(OrderType.SCORE);

        List<ShopCartItemVO> shopCartItemsDb = shopCartItemAdapter.conversionShopCartItem(orderParam.getShopCartItem(), null, Constant.MAIN_SHOP);

        // 筛选过滤掉不同配送的商品
        List<ShopCartItemVO> shopCartItems = confirmOrderManager.filterShopItemsByType(shopCartOrderMerger,shopCartItemsDb);

        OrderLangUtil.shopCartItemList(shopCartItemsDb);

        // 购物车
        List<ShopCartVO> shopCarts = shopCartAdapter.conversionShopCart(shopCartItems);

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        // 积分商品没有优惠活动信息，不需要异步
        RequestContextHolder.setRequestAttributes(requestAttributes);
        ServerResponseEntity<UserDeliveryInfoVO> userDeliveryInfoResponseEntity = deliveryFeignClient.calculateAndGetDeliverInfo(new CalculateAndGetDeliverInfoDTO(orderParam.getAddrId(), orderParam.getDvyType(), orderParam.getStationId(), shopCartItems));

        if (!userDeliveryInfoResponseEntity.isSuccess()) {
            return ServerResponseEntity.transform(userDeliveryInfoResponseEntity);
        }
        // 当算完一遍店铺的各种满减活动时，重算一遍订单金额
        confirmOrderManager.recalculateAmountWhenFinishingCalculateShop(shopCartOrderMerger, shopCarts,userDeliveryInfoResponseEntity.getData());
        long orderShopReduce = shopCartOrderMerger.getOrderReduce();

        // ===============================================开始平台优惠的计算==================================================

        // 会员等级优惠
        ServerResponseEntity<ShopCartOrderMergerVO> calculateLevelDiscountResponseEntity = userLevelAndScoreOrderFeignClient.calculateLevelAndScoreDiscount(shopCartOrderMerger);
        if (!calculateLevelDiscountResponseEntity.isSuccess()) {
            return ServerResponseEntity.transform(calculateLevelDiscountResponseEntity);
        }
        shopCartOrderMerger = calculateLevelDiscountResponseEntity.getData();

        // 重新插入spu、sku
        Map<Long, ShopCartItemVO> shopCartItemMap = shopCartItemsDb.stream().collect(Collectors.toMap(ShopCartItemVO::getSkuId, s -> s));
        for (ShopCartOrderVO shopCartOrder : shopCartOrderMerger.getShopCartOrders()) {
            shopCartOrder.setShopName(Constant.PLATFORM_SHOP_NAME);
            for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartOrder.getShopCartItemDiscounts()) {
                for (ShopCartItemVO shopCartItem : shopCartItemDiscount.getShopCartItems()) {
                    ShopCartItemVO shopCartItemVO = shopCartItemMap.get(shopCartItem.getSkuId());
                    shopCartItem.setSkuLangList(shopCartItemVO.getSkuLangList());
                    shopCartItem.setSpuLangList(shopCartItemVO.getSpuLangList());
                }
            }
        }
        shopCartOrderMerger.setOrderShopReduce(orderShopReduce);
        // 缓存计算新
        confirmOrderManager.cacheCalculatedInfo(userId, shopCartOrderMerger);
        return ServerResponseEntity.success(shopCartOrderMerger);
    }
}
