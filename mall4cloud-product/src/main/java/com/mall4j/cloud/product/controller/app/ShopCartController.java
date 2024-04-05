package com.mall4j.cloud.product.controller.app;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.delivery.dto.CalculateAndGetDeliverInfoDTO;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.discount.feign.DiscountFeignClient;
import com.mall4j.cloud.api.multishop.bo.EsShopDetailBO;
import com.mall4j.cloud.api.multishop.constant.ShopStatus;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.product.manager.ShopCartAdapter;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.order.vo.ShopCartVO;
import com.mall4j.cloud.common.order.vo.ShopCartWithAmountVO;
import com.mall4j.cloud.common.order.vo.UserDeliveryInfoVO;
import com.mall4j.cloud.common.product.vo.SkuLangVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuLangVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LangUtil;
import com.mall4j.cloud.product.dto.shopcart.AddShopCartItemsDTO;
import com.mall4j.cloud.product.dto.shopcart.ChangeShopCartItemDTO;
import com.mall4j.cloud.product.dto.shopcart.CheckShopCartItemDTO;
import com.mall4j.cloud.product.model.ShopCartItem;
import com.mall4j.cloud.product.model.SkuStock;
import com.mall4j.cloud.product.service.ShopCartService;
import com.mall4j.cloud.product.service.SkuService;
import com.mall4j.cloud.product.service.SkuStockService;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.vo.ShopCartAmountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 购物车
 *
 * @author FrozenWatermelon
 * @date 2020-11-20 15:47:32
 */
@RestController
@RequestMapping("/shop_cart")
@Api(tags = "app-购物车")
@Slf4j
public class ShopCartController {

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private SpuService spuService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private DeliveryFeignClient deliveryFeignClient;
    @Autowired
    private ThreadPoolExecutor prodThreadPoolExecutor;
    @Autowired
    private DiscountFeignClient discountFeignClient;

    @Autowired
    private ShopCartAdapter shopCartAdapter;

    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;

    @Autowired
    private SkuStockService skuStockService;

    /**
     * 获取用户购物车信息
     *
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取用户购物车信息", notes = "获取用户购物车信息")
    public ServerResponseEntity<ShopCartWithAmountVO> info(@RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId) throws ExecutionException, InterruptedException {
        // 拿到购物车的所有item
        List<ShopCartItemVO> shopCartItems = shopCartService.getShopCartItems(storeId);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 筛选下选中的商品用于计算运费
        List<ShopCartItemVO> filterShopCartItems = shopCartItems.stream().filter(shopCartItemVO -> shopCartItemVO.getIsChecked() == 1).collect(Collectors.toList());
        // 异步计算运费，运费暂时和优惠券没啥关联，可以与优惠券异步计算，获取用户地址，自提信息
        CompletableFuture<ServerResponseEntity<UserDeliveryInfoVO>> deliveryFuture = CompletableFuture.supplyAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            // TODO 这边目前只有快递配送一种方式，所以先写死快递配送
            return deliveryFeignClient.calculateAndGetDeliverInfo(new CalculateAndGetDeliverInfoDTO(0L, DeliveryType.DELIVERY.value(), null, filterShopCartItems));
        }, prodThreadPoolExecutor);
        // 运费用异步计算，最后要等运费出结果
        ServerResponseEntity<UserDeliveryInfoVO> userDeliveryInfoResponseEntity = deliveryFuture.get();
        if (!userDeliveryInfoResponseEntity.isSuccess()) {
            return ServerResponseEntity.transform(userDeliveryInfoResponseEntity);
        }
        List<ShopCartVO> shopCarts = shopCartAdapter.conversionShopCart(shopCartItems);
        ShopCartWithAmountVO shopCartWithAmountVO = new ShopCartWithAmountVO();
        shopCartWithAmountVO.setShopCarts(shopCarts);
        shopCartWithAmountVO.setUserDeliveryInfo(userDeliveryInfoResponseEntity.getData());
        return discountFeignClient.calculateDiscountAndMakeUpShopCartAndAmount(shopCartWithAmountVO);
    }

    /**
     * 获取用户购物车信息
     *
     * @return
     */
    @GetMapping("/amount_info")
    @ApiOperation(value = "获取用户购物车金额信息", notes = "获取用户购物车金额信息")
    public ServerResponseEntity<ShopCartAmountVO> amountInfo(@RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId) {
        // 拿到购物车的所有item
        List<ShopCartItemVO> shopCartItems = shopCartService.getShopCartItems(storeId);
        List<ShopCartVO> shopCarts = shopCartAdapter.conversionShopCart(shopCartItems);
        ShopCartWithAmountVO shopCartWithAmountVO = new ShopCartWithAmountVO();
        shopCartWithAmountVO.setShopCarts(shopCarts);
        ServerResponseEntity<ShopCartWithAmountVO> shopCartWithAmountResponse = discountFeignClient.calculateDiscountAndMakeUpShopCartAndAmount(shopCartWithAmountVO);

        if (!shopCartWithAmountResponse.isSuccess()) {
            return ServerResponseEntity.transform(shopCartWithAmountResponse);
        }

        return ServerResponseEntity.success(mapperFacade.map(shopCartWithAmountResponse.getData(), ShopCartAmountVO.class));
    }


    @DeleteMapping("/delete_item")
    @ApiOperation(value = "删除用户购物车物品", notes = "通过购物车id删除用户购物车物品")
    public ServerResponseEntity<Void> deleteItem(@RequestBody List<Long> shopCartItemIds) {
        Long userId = AuthUserContext.get().getUserId();
        shopCartService.deleteShopCartItemsByShopCartItemIds(userId, shopCartItemIds);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/delete_all")
    @ApiOperation(value = "清空用户购物车所有物品", notes = "清空用户购物车所有物品")
    public ServerResponseEntity<String> deleteAll() {
        Long userId = AuthUserContext.get().getUserId();
        shopCartService.deleteAllShopCartItems(userId);
        // 删除成功
        return ServerResponseEntity.success();
    }

    @PostMapping("/check_items")
    @ApiOperation(value = "勾选购物车")
    public ServerResponseEntity<Void> checkItems(@Valid @RequestBody List<CheckShopCartItemDTO> params) {
        if (CollectionUtil.isEmpty(params)) {
            return ServerResponseEntity.success();
        }
        Long userId = AuthUserContext.get().getUserId();
        shopCartService.checkShopCartItems(userId, params);
        return ServerResponseEntity.success();
    }


    @PostMapping("/change_item")
    @ApiOperation(value = "添加、修改用户购物车物品", notes = "通过商品id(prodId)、skuId、店铺Id(shopId),添加/修改用户购物车商品，并传入改变的商品个数(count)，" +
            "当count为正值时，增加商品数量，当count为负值时，将减去商品的数量，当最终count值小于0时，会将商品从购物车里面删除")
    public ServerResponseEntity<Void> addItem(@RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId, @Valid @RequestBody ChangeShopCartItemDTO param) {
        //校验总店库存
        Long skuId = param.getSkuId();


        // 不用校验库存是否充足！！！
        Long userId = AuthUserContext.get().getUserId();
        List<ShopCartItemVO> shopCartItems = shopCartService.getShopCartItems(storeId);
        log.info("[会员购物车数据] - {}", JSONObject.toJSONString(shopCartItems));
        SkuStock one = null;
        ShopCartItemVO shopCartItemVO1 = null;
        try {
            one = skuStockService.getOne(new LambdaQueryWrapper<SkuStock>().eq(SkuStock::getSkuId, skuId), false);
            if (CollectionUtil.isNotEmpty(shopCartItems)) {
                List<ShopCartItemVO> shopCartItem = shopCartItems.stream().filter(shopCartItemVO -> shopCartItemVO.getSkuId().equals(param.getSkuId())).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(shopCartItem)) {
                    shopCartItemVO1 = shopCartItem.get(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LuckException("库存校验失败");
        }
        SpuVO spu = spuService.getBySpuId(param.getSpuId());
//        SkuVO sku = skuService.getSkuBySkuId(skuId);
        SkuVO sku = skuService.getSkuBySkuIdAndStoreId(param.getSpuId(),skuId,storeId);
        int count = 0;
        if (Objects.nonNull(shopCartItemVO1)) {
            count = shopCartItemVO1.getCount() + param.getCount();
        } else {
            count = param.getCount();
        }
        if (count > one.getStock()) {
            throw new LuckException("商品库存不足");
        }


        // 当商品状态不正常时，不能添加到购物车
        if (Objects.isNull(spu) || Objects.isNull(sku) || !Objects.equals(spu.getStatus(), StatusEnum.ENABLE.value()) || !Objects.equals(sku.getStatus(), StatusEnum.ENABLE.value()) || !Objects.equals(sku.getSpuId(), spu.getSpuId())) {
            // 当返回商品不存在时，前端应该将商品从购物车界面移除
            return ServerResponseEntity.fail(ResponseEnum.SPU_NOT_EXIST);
        }
        // 保存shopId，不要让前端传过来
        param.setShopId(spu.getShopId());


        Integer oldCount = 0;
        Long oldShopCartItemId = null;
        for (ShopCartItemVO shopCartItemVo : shopCartItems) {
            if (Objects.equals(skuId, shopCartItemVo.getSkuId())) {
                // 旧数量
                oldCount = shopCartItemVo.getCount();
                oldShopCartItemId = shopCartItemVo.getCartItemId();
                ShopCartItem shopCartItem = new ShopCartItem();
                shopCartItem.setUserId(userId);
                shopCartItem.setCount(param.getCount() + shopCartItemVo.getCount());
                shopCartItem.setCartItemId(shopCartItemVo.getCartItemId());
                shopCartItem.setIsChecked(shopCartItemVo.getIsChecked());
                shopCartItem.setDiscountId(param.getDiscountId());
                // 如果有个旧的sku，就说明是在切换sku
                if (Objects.nonNull(param.getOldSkuId())) {
                    continue;
                }
                // 防止购物车变成负数，从购物车删除
                if (shopCartItem.getCount() <= 0) {
                    shopCartService.deleteShopCartItemsByShopCartItemIds(userId, Collections.singletonList(shopCartItem.getCartItemId()));
                    return ServerResponseEntity.success();
                }
                shopCartService.updateShopCartItem(userId, shopCartItem);
                return ServerResponseEntity.success();
            }
        }

        if (Objects.nonNull(param.getOldSkuId())) {
            for (ShopCartItemVO oldShopCartItem : shopCartItems) {
                // 旧sku
                if (Objects.equals(param.getOldSkuId(), oldShopCartItem.getSkuId())) {

                    ShopCartItem shopCartItem = new ShopCartItem();
                    shopCartItem.setUserId(userId);
                    shopCartItem.setCartItemId(oldShopCartItem.getCartItemId());
                    // 如果以前就存在这个商品，还要把以前的商品数量累加
                    shopCartItem.setCount(param.getCount() + oldCount);
                    shopCartItem.setSkuId(skuId);

                    if (oldShopCartItemId != null) {
                        // 删除旧的购物项
                        shopCartService.deleteShopCartItemsByShopCartItemIds(userId, Collections.singletonList(oldShopCartItemId));
                    }
                    // 更新购物车
                    shopCartService.updateShopCartItem(userId, shopCartItem);

                    return ServerResponseEntity.success();
                }
            }
        }

        // 所有都正常时
        shopCartService.addShopCartItem(userId, param, sku.getPriceFee(), spu.getCategoryId());
        // 添加成功
        return ServerResponseEntity.success();
    }

    @PostMapping("/add_items")
    @ApiOperation(value = "批量添加用户购物车物品", notes = "通过一个对象集合，对象包含商品id(prodId)、skuId、店铺Id(shopId),添加用户购物车商品")
    public ServerResponseEntity<String> addItems(@RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId, @Valid @RequestBody AddShopCartItemsDTO shopCartItemsDTO) {
        StringBuilder errorMsg = new StringBuilder();
        Long userId = AuthUserContext.get().getUserId();
        HashMap<Long, EsShopDetailBO> shopDetailMap = new HashMap<>(16);
        for (ChangeShopCartItemDTO param : shopCartItemsDTO.getShopCartItemList()) {
            // 不用校验库存是否充足！！！
            List<ShopCartItemVO> shopCartItems = shopCartService.getShopCartItems(storeId);

            SpuVO spu = spuService.getBySpuId(param.getSpuId());
            SkuVO sku = skuService.getSkuBySkuId(param.getSkuId());
            if (Objects.isNull(shopDetailMap.get(param.getShopId()))) {
                ServerResponseEntity<EsShopDetailBO> shopDetailRes = shopDetailFeignClient.getShopByShopId(param.getShopId());
                if (shopDetailRes.isFail()) {
                    throw new LuckException(shopDetailRes.getMsg());
                }
                shopDetailMap.put(param.getShopId(), shopDetailRes.getData());
            }
            EsShopDetailBO esShopDetailBO = shopDetailMap.get(param.getShopId());
            // 当商品状态不存在或店铺不存在时不能添加到购物车
            if (Objects.isNull(spu) || Objects.isNull(sku) || !Objects.equals(sku.getSpuId(), spu.getSpuId()) || Objects.isNull(esShopDetailBO)) {
                continue;
            }

            Map<Integer, String> spuMap = spu.getSpuLangList().stream().filter(spuLangVO -> StrUtil.isNotBlank(spuLangVO.getSpuName())).collect(Collectors.toMap(SpuLangVO::getLang, SpuLangVO::getSpuName));
            spu.setName(LangUtil.getLangValue(spuMap));
            Map<Integer, String> skuMap = sku.getSkuLangList().stream().filter(skuLangVO -> StrUtil.isNotBlank(skuLangVO.getSkuName())).collect(Collectors.toMap(SkuLangVO::getLang, SkuLangVO::getSkuName));
            sku.setSkuName(LangUtil.getLangValue(skuMap));

            // 当商品状态不正常或店铺已停业时，不能添加到购物车
            if (!Objects.equals(spu.getStatus(), StatusEnum.ENABLE.value()) || !Objects.equals(sku.getStatus(), StatusEnum.ENABLE.value()) || Objects.equals(esShopDetailBO.getShopStatus(), ShopStatus.STOP.value())) {
                // 当返回商品不存在时，记录哪些不能加入购物车
                errorMsg.append(spu.getName()).append(sku.getSkuName()).append(StrUtil.COMMA);
                continue;
            }
            loadAddItemsData(userId, param, shopCartItems, spu, sku);
        }

        if (StrUtil.isNotBlank(errorMsg)) {
            errorMsg.deleteCharAt(errorMsg.length() - 1);
            errorMsg.append("商品已失效无法加入购物车，其余已加入购物车");
            return ServerResponseEntity.success(errorMsg.toString());
        }
        // 添加成功
        return ServerResponseEntity.success("操作成功");
    }

    /**
     * 加载加购项数据
     *
     * @param userId        用户id
     * @param param         加购数据
     * @param shopCartItems 购物车列表
     * @param spu           商品信息
     * @param sku           商品规格信息
     */
    private void loadAddItemsData(Long userId, ChangeShopCartItemDTO param, List<ShopCartItemVO> shopCartItems, SpuVO spu, SkuVO sku) {
        boolean isContinue = false;

        // 保存shopId，不要让前端传过来
        param.setShopId(spu.getShopId());

        Integer oldCount = 0;
        Long oldShopCartItemId = null;
        for (ShopCartItemVO shopCartItemVo : shopCartItems) {
            if (Objects.equals(param.getSkuId(), shopCartItemVo.getSkuId())) {
                // 旧数量
                oldCount = shopCartItemVo.getCount();
                oldShopCartItemId = shopCartItemVo.getCartItemId();
                ShopCartItem shopCartItem = new ShopCartItem();
                shopCartItem.setUserId(userId);
                shopCartItem.setCount(param.getCount() + shopCartItemVo.getCount());
                shopCartItem.setCartItemId(shopCartItemVo.getCartItemId());
                shopCartItem.setIsChecked(shopCartItemVo.getIsChecked());
                shopCartItem.setDiscountId(param.getDiscountId());
                // 如果有个旧的sku，就说明是在切换sku
                if (Objects.nonNull(param.getOldSkuId())) {
                    continue;
                }
                // 防止购物车变成负数，从购物车删除
                if (shopCartItem.getCount() <= 0) {
                    shopCartService.deleteShopCartItemsByShopCartItemIds(userId, Collections.singletonList(shopCartItem.getCartItemId()));
                    isContinue = true;
                    break;
                }
                shopCartService.updateShopCartItem(userId, shopCartItem);
                isContinue = true;
                break;
            }
        }

        if (isContinue) {
            return;
        }

        if (Objects.nonNull(param.getOldSkuId())) {
            for (ShopCartItemVO oldShopCartItem : shopCartItems) {
                // 旧sku
                if (Objects.equals(param.getOldSkuId(), oldShopCartItem.getSkuId())) {

                    ShopCartItem shopCartItem = new ShopCartItem();
                    shopCartItem.setUserId(userId);
                    shopCartItem.setCartItemId(oldShopCartItem.getCartItemId());
                    // 如果以前就存在这个商品，还要把以前的商品数量累加
                    shopCartItem.setCount(param.getCount() + oldCount);
                    shopCartItem.setSkuId(param.getSkuId());

                    if (oldShopCartItemId != null) {
                        // 删除旧的购物项
                        shopCartService.deleteShopCartItemsByShopCartItemIds(userId, Collections.singletonList(oldShopCartItemId));
                    }
                    // 更新购物车
                    shopCartService.updateShopCartItem(userId, shopCartItem);
                    isContinue = true;
                    break;
                }
            }
        }

        if (isContinue) {
            return;
        }

        // 所有都正常时
        shopCartService.addShopCartItem(userId, param, sku.getPriceFee(), spu.getCategoryId());
    }

    @GetMapping("/ma/prod_count")
    @ApiOperation(value = "获取购物车商品数量", notes = "获取购物车商品数量")
    public ServerResponseEntity<Integer> prodCount() {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (userInfoInTokenBO == null) {
            return ServerResponseEntity.fail(ResponseEnum.CLEAN_TOKEN);
        }
        return ServerResponseEntity.success(shopCartService.getShopCartItemCount(userInfoInTokenBO.getUserId()));
    }

    @GetMapping("/expiry_prod_list")
    @ApiOperation(value = "获取购物车失效商品信息", notes = "获取购物车失效商品列表")
    public ServerResponseEntity<List<ShopCartItemVO>> expiryProdList(@RequestParam(value = "storeId", required = false, defaultValue = "1") Long storeId) {
        return ServerResponseEntity.success(shopCartService.getShopCartExpiryItems(storeId));
    }

}
