package com.mall4j.cloud.discount.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.discount.constant.DiscountRule;
import com.mall4j.cloud.api.discount.vo.DiscountSumItemVO;
import com.mall4j.cloud.api.discount.vo.DiscountSumVO;
import com.mall4j.cloud.common.order.vo.ChooseDiscountItemVO;
import com.mall4j.cloud.common.order.vo.DiscountItemOrderVO;
import com.mall4j.cloud.common.order.vo.DiscountOrderVO;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.util.BooleanUtil;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.discount.model.Discount;
import com.mall4j.cloud.discount.service.DiscountService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 满减活动金额计算
 *
 * @author FrozenWatermelon
 */
@Service
@Slf4j
public class DiscountManager {

    @Autowired
    private DiscountService discountService;
    @Autowired
    private MapperFacade mapperFacade;


    /**
     * 方法的作用
     * 1.计算优惠金额
     * 2. 并往productItems 设置满减优惠信息
     *
     * @param shopId
     * @param productItems
     * @return
     */
    public DiscountSumVO calculateDiscount(Long shopId, List<? extends ShopCartItemVO> productItems,boolean flag) {

        DiscountSumVO discountSum = new DiscountSumVO();
        discountSum.setTotalReduceAmount(0L);

        List<DiscountOrderVO> discounts = discountService.listDiscountsAndItemsByShopId(shopId);
        log.info("满减活动前参数:{}",JSONObject.toJSONString(discounts));
        if (!flag) {
            discounts.removeAll(discounts);
        }
        log.info("满减活后参数:{}",JSONObject.toJSONString(discounts));
        // 旧的商品数据，也就是第一次传入的商品数据
        List<ShopCartItemVO> oldProdItems = new ArrayList<>(productItems);

        // 以优惠活动id为key， 每个满减活动的活动信息 为value的map
        // {优惠活动id：每个满减活动的活动信息}
        Map<Long, DiscountSumItemVO> discountIdDiscountSumItemMap = new HashMap<>(productItems.size());
        //保存满减的金额
        Long amount = 0L;
        if (productItems.size() == 1 && productItems.get(0).getDiscountId() == 0) {
            // 立即提交订单, 选择满减最高的活动
            ShopCartItemVO productItemDto = productItems.get(0);
            for (DiscountOrderVO discount : discounts) {
                //判断商品是否参与该活动
                boolean hasDiscount = isHasDiscount(discount, productItemDto.getSpuId());
                if (!hasDiscount) {
                    continue;
                }
                //判断当前商品是否参加了会员日活动并且设置的为不参与满减
                if(productItemDto.getFriendlyDiscountFlag()==0){
                    continue;
                }
                //添加到活动列表
                DiscountOrderVO discountDto = new DiscountOrderVO();
                discountDto.setDiscountName(discount.getDiscountName());
                discountDto.setDiscountId(discount.getDiscountId());
                if (CollUtil.isEmpty(productItemDto.getDiscounts())) {
                    productItemDto.setDiscounts(new ArrayList<>());
                }
                productItemDto.getDiscounts().add(discountDto);
                //获取最高满减的活动
                DiscountSumItemVO discountSumItemDto = new DiscountSumItemVO();
                discountSumItemDto.setCount(productItemDto.getCount());
                if (productItemDto.getIsChecked() == 0) {
                    discountSumItemDto.setProdsPrice(0L);
                } else {
                    discountSumItemDto.setProdsPrice(productItemDto.getTotalAmount());
                }
                findDiscountItemAndGetReduceAmount(discount, discountSumItemDto);
                discountIdDiscountSumItemMap.put(discount.getDiscountId(), discountSumItemDto);
                //该活动的满减金额大于前一个，则把满减活动替换成当前的活动
                if (productItemDto.getDiscountId() == 0 || amount < discountSumItemDto.getReduceAmount()) {
                    productItemDto.setDiscountId(discount.getDiscountId());
                    discountSum.setTotalReduceAmount(discountSumItemDto.getReduceAmount());
                    amount = discountSumItemDto.getReduceAmount();
                }
            }
        } else {
            // 购物车
            for (DiscountOrderVO discount : discounts) {
                // 获取当前优惠的商品
                List<ShopCartItemVO> hasCurrentDiscountProds = mergeDiscountProd(oldProdItems, discount);

                DiscountSumItemVO discountSumItem = new DiscountSumItemVO();
                discountSumItem.setDiscount(0L);
                discountSumItem.setDiscountItemId(-1L);
                discountSumItem.setCount(0);
                discountSumItem.setProdsPrice(0L);
                discountSumItem.setNeedAmount(0L);
                discountSumItem.setReduceAmount(0L);

                // 计算参与该活动的所有商品的商品金额，商品数量
                for (ShopCartItemVO hasDiscountProd : hasCurrentDiscountProds) {
                    if (BooleanUtil.isTrue(hasDiscountProd.getIsChecked())) {
                        discountSumItem.setCount(discountSumItem.getCount() + hasDiscountProd.getCount());
                        discountSumItem.setProdsPrice(discountSumItem.getProdsPrice() + hasDiscountProd.getTotalAmount());
                    }
                }
                log.info("参与满减活动商品入参：discount:{},discountSumItem:{}",discount.toString(),discountSumItem.toString());
                findDiscountItemAndGetReduceAmount(discount, discountSumItem);

                log.info("discountIdDiscountSumItemMap数据：", JSONObject.toJSONString(discountIdDiscountSumItemMap));
                log.info("discountId:{},discountSumItemReduceAmount:{}",discount.getDiscountId(),discountSumItem.getReduceAmount());
                discountIdDiscountSumItemMap.put(discount.getDiscountId(), discountSumItem);

                discountSum.setTotalReduceAmount(discountSum.getTotalReduceAmount() + discountSumItem.getReduceAmount());
            }
        }
        discountSum.setDiscountIdDiscountSumItemMap(discountIdDiscountSumItemMap);
        return discountSum;
    }

    public ChooseDiscountItemVO getChooseDiscountItemVO(DiscountSumVO discountSum, Long discountId) {

        ChooseDiscountItemVO chooseDiscountItemVO = null;
        // 参与活动
        if (discountId != -1 && discountId != 0) {
            Map<Long, DiscountSumItemVO> discountIdDiscountSumItemMap = discountSum.getDiscountIdDiscountSumItemMap();
            DiscountSumItemVO discountSumItemVO = discountIdDiscountSumItemMap.get(discountId);
            if (discountSumItemVO == null) {
                return null;
            }

            Discount discount = discountService.getDiscountByDiscountId(discountId);
            if (Objects.nonNull(discount)) {
                chooseDiscountItemVO = mapperFacade.map(discount, ChooseDiscountItemVO.class);

                chooseDiscountItemVO.setProdsPrice(discountSumItemVO.getProdsPrice());
                chooseDiscountItemVO.setCount(discountSumItemVO.getCount());
                chooseDiscountItemVO.setNeedAmount(discountSumItemVO.getNeedAmount());
                chooseDiscountItemVO.setDiscountItemId(discountSumItemVO.getDiscountItemId());
                chooseDiscountItemVO.setDiscount(discountSumItemVO.getDiscount());
                chooseDiscountItemVO.setReduceAmount(discountSumItemVO.getReduceAmount());
            }
        }
        return chooseDiscountItemVO;
    }

    /**
     * 1. 找出当前优惠活动属于那个优惠项
     * 2. 计算满减
     *
     * @param discount 优惠活动
     * @return 优惠金额
     */
    private DiscountSumItemVO findDiscountItemAndGetReduceAmount(DiscountOrderVO discount, DiscountSumItemVO discountSumItemVO) {
        DiscountSumItemVO returnDiscountSumItemVO = new DiscountSumItemVO();
        // 枚举DiscountRule(0 满钱减钱 1满件减钱 2 满钱打折 3满件打折)
        Integer discountRule = discount.getDiscountRule();
        // 减免类型 0按满足最高层级减一次 1每满一次减一次
        Integer discountType = discount.getDiscountType();

        List<DiscountItemOrderVO> discountItems = discount.getDiscountItems();

        double prodCount = discountSumItemVO.getCount();
        Long prodsPrice = discountSumItemVO.getProdsPrice();

        Long totalReduceAmount = 0L;
        Long discountItemId = -1L;
        //正序排序。
//        discountItems = discountItems.stream().sorted(Comparator.comparing(DiscountItemOrderVO::getNeedAmount)).collect(Collectors.toList());
        for (DiscountItemOrderVO discountItem : discountItems) {
            // 优惠（元/折）
            Long reduceAmount = discountItem.getDiscount();

            discountItemId = discountItem.getDiscountItemId();
            Long needAmount = discountItem.getNeedAmount();
            discountSumItemVO.setNeedAmount(needAmount);
            discountSumItemVO.setDiscount(reduceAmount);
            // 当规则为满钱减钱时
            if (Objects.equals(discountRule, DiscountRule.M2M.value()) && prodsPrice >= needAmount) {
                totalReduceAmount = getTotalReduceAmountBySubMoney(discountType, prodsPrice, reduceAmount, needAmount);
                BeanUtils.copyProperties(discountSumItemVO, returnDiscountSumItemVO);
                break;
            }
            // 当规则为满件打折时 9.5折就是95、9.5元就是950，此时数据库存的是件数*100，还需除以100进行判断
            needAmount = PriceUtil.toDecimalPrice(needAmount).longValue();
            log.info("计算needAmount:{}",needAmount);
            if (Objects.equals(discountRule, DiscountRule.P2D.value()) && prodCount >= needAmount) {
                // （商品金额 * （1000 - 折扣比例）） / 1000 ，原本是正常除以10，因为比例乘以了100所以此处也是除以1000
                totalReduceAmount = PriceUtil.divideByBankerRounding(prodsPrice * (1000 - reduceAmount), 1000);
                //对返回参数进行赋值
                BeanUtils.copyProperties(discountSumItemVO, returnDiscountSumItemVO);
                break;
            }
        }
        // 如果是每满一次减一次，则需要判断上限 不为满钱减钱或者满足最高层级减一次情况下，需要判断上限
        if (discountType != 0 || !Objects.equals(discount.getDiscountRule(), 0)) {
            totalReduceAmount = Math.min(discount.getMaxReduceAmount(), totalReduceAmount);
        }
        if (Objects.nonNull(returnDiscountSumItemVO.getDiscount())) {
            BeanUtils.copyProperties(returnDiscountSumItemVO, discountSumItemVO);
        }
        discountSumItemVO.setReduceAmount(totalReduceAmount);
        discountSumItemVO.setDiscountItemId(discountItemId);
        return discountSumItemVO;
    }

    private Long getTotalReduceAmountBySubMoney(Integer discountType, Long prodsPriceOrCount, Long reduceAmount, Long needAmount) {
        Long totalReduceAmount;
        if (discountType == 0) {
            // 当商品价格大于最高层级时
            totalReduceAmount = reduceAmount;
        } else {
            // 查看满足多少次条件(向下取整)
            long multi = prodsPriceOrCount / needAmount;
            totalReduceAmount = reduceAmount * multi;
        }
        return totalReduceAmount;
    }

    /**
     * 合并当前优惠的商品
     * 注：如果oldProdItems商品有该优惠，则将其抽取出来，并且在oldProdItems中移除该商品
     *
     * @param oldProdItems 商品项
     * @param discount     活动
     * @return
     */
    private List<ShopCartItemVO> mergeDiscountProd(List<ShopCartItemVO> oldProdItems, DiscountOrderVO discount) {

        // 拥有当前优惠活动的商品
        List<ShopCartItemVO> hasCurrentDiscountProds = new ArrayList<>();

        for (ShopCartItemVO productItem : oldProdItems) {
            // 查看商品是否包含优惠活动
            boolean hasDiscount = isHasDiscount(discount, productItem.getSpuId());

//            if(productItem.getIsChecked() == 0){
//                hasDiscount = false;
//            }
            // 购物车商品数据中插入满减活动列表
            //这里做个修改，只有商品包含这个满减活动时，才将该活动添加到商品的满减活动列表中
            if(hasDiscount){
                DiscountOrderVO discountOrderVO = new DiscountOrderVO();
                discountOrderVO.setDiscountId(discount.getDiscountId());
                discountOrderVO.setDiscountName(discount.getDiscountName());
                if (CollectionUtil.isEmpty(productItem.getDiscounts())) {
                    productItem.setDiscounts(new ArrayList<>());
                }
                productItem.getDiscounts().add(discountOrderVO);
            }


            // 商品选择了这个活动
            boolean prodChooseDiscount = Objects.equals(productItem.getDiscountId(), discount.getDiscountId());

            //会员日商品设置优先级最高
            //判断当前商品是否参加了会员日活动并且设置的为不参与满减
            if(productItem.getFriendlyDiscountFlag()==0){
                //  将往productItems 设置满减优惠信息为【-1】
                productItem.setDiscountId(-1L);
                continue;
            }

            // 如果商品不包含该优惠活动
            // 或用户选择不参与该活动，将活动清除
            if (!hasDiscount && prodChooseDiscount) {
                //  将往productItems 设置满减优惠信息为【-1】
                productItem.setDiscountId(-1L);
            }
            // 如果 商品参与了该活动，将会分为几个步骤
            // 判断商品是否在这个活动中（如果用户没有为商品主动选择优惠活动，或选择的优惠活动与该满减的优惠活动一样 则视为商品在活动中）
            else if (hasDiscount && prodChooseDiscount) {

                // 2. 将往productItems 设置满减优惠信息为【当前活动】
                productItem.setDiscountId(discount.getDiscountId());

                // 3. 将该活动的商品统一起来
                hasCurrentDiscountProds.add(productItem);
            }
            // 如果商品参与该活动
            // 且用户没有主动放弃该活动
            else if (hasDiscount && productItem.getDiscountId() == 0) {

                // 2. 将往productItems 设置满减优惠信息为【当前活动】
                productItem.setDiscountId(discount.getDiscountId());

                // 3. 将该活动的商品统一起来
                hasCurrentDiscountProds.add(productItem);
            }
        }

        return hasCurrentDiscountProds;
    }

    /**
     * 查看商品是否包含优惠活动
     *
     * @param discount 当前优惠活动信息
     * @param spuId    当前商品id
     * @return
     */
    private boolean isHasDiscount(DiscountOrderVO discount, Long spuId) {
        boolean hasDiscount = false;
        List<Long> spuIds = discount.getSpuIds();
        // 所有商品都含有该优惠
        if (Objects.equals(discount.getSuitableSpuType(), 0)) {
            hasDiscount = true;
        } else if (Objects.equals(discount.getSuitableSpuType(), 1)) {
            // 指定商品含有该优惠
            for (Long spuIdVO : spuIds) {
                if (Objects.equals(spuIdVO, spuId)) {
                    hasDiscount = true;
                    break;
                }
            }
        }
        return hasDiscount;
    }


}
