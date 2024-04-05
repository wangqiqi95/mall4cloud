package com.mall4j.cloud.discount.controller.app;

import com.mall4j.cloud.common.order.vo.DiscountOrderVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.discount.constant.SuitableProdTypeEnum;
import com.mall4j.cloud.discount.service.DiscountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 满减满折优惠
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
@RestController("appSpuDiscountController")
@RequestMapping("/ua/discount")
@Api(tags = "商品的满减满折优惠")
public class SpuDiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping("/get_discount_list")
    @ApiOperation(value = "获取店铺、商品对应的促销活动", notes = "通过商品id获取商品所有促销活动")
    public ServerResponseEntity<List<DiscountOrderVO>> getDiscountBySpuId(Long spuId, Long shopId) {
        List<DiscountOrderVO> discountList = discountService.listDiscountsAndItemsByShopId(shopId);
//        discountList.addAll(discountService.listDiscountsAndItemsByShopId(Constant.PLATFORM_SHOP_ID));
        Iterator<DiscountOrderVO> iterator = discountList.iterator();
        while (iterator.hasNext()) {
            DiscountOrderVO discount = iterator.next();
            if (Objects.equals(discount.getSuitableSpuType(), SuitableProdTypeEnum.ALL_SPU.value()) || discount.getSpuIds().contains(spuId)) {
                continue;
            }
            iterator.remove();
        }
        return ServerResponseEntity.success(discountList);
    }
}
