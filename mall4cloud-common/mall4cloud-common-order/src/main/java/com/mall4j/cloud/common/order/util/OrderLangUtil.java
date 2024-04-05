package com.mall4j.cloud.common.order.util;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.util.LangUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单语言工具类
 * @author YXF
 * @date 2021/05/19
 */
public class OrderLangUtil {

    /**
     * 订单项数据国际化
     * @param orderItemList 订单项
     */
    public static void orderItemVOList(List<OrderItemVO> orderItemList) {
        for (OrderItemVO orderItemVO : orderItemList) {
            handleOrderItemVO(orderItemVO);
        }
    }

    /**
     * 退款订单项数据国际化
     * @param orderItemList 订单项
     */
    public static void orderRefundVOList(List<RefundOrderItemVO> orderItemList) {
        for (RefundOrderItemVO orderItemVO : orderItemList) {
            handleOrderItem(orderItemVO);
        }
    }


    /**
     * 购物车数据国际化
     * @param shopCartItem 订单项
     */
    public static void shopCartItemLang(ShopCartItemVO shopCartItem) {
        shopCartItem.setSpuName(getSpuName(shopCartItem.getSpuLangList()));
        shopCartItem.setSkuName(getSkuName(shopCartItem.getSkuLangList()));
    }


    /**
     * 购物车数据国际化
     * @param shopCartItems 订单项
     */
    public static void shopCartItemList(List<ShopCartItemVO> shopCartItems) {
        for (ShopCartItemVO shopCartItem : shopCartItems) {
//            shopCartItemLang(shopCartItem);
        }
    }

    /**
     * es订单项数据国际化
     * @param orderItems 订单项
     */
    public static void esOrderList(List<EsOrderItemVO> orderItems) {
        for (EsOrderItemVO orderItem : orderItems) {
            handleEsOrderItem(orderItem);
        }
    }


    /**
     * 处理退款订单项信息
     * @param orderItem 订单项国际化信息列表
     */
    public static void handleOrderItem(RefundOrderItemVO orderItem) {
        OrderItemLangVO orderItemLangVO = handleOrderItemLangVO(orderItem.getOrderItemLangList());
        if (Objects.isNull(orderItemLangVO)) {
            return;
        }
        orderItem.setSpuName(orderItemLangVO.getSpuName());
        orderItem.setSkuName(orderItemLangVO.getSkuName());
        orderItem.setOrderItemLangList(null);
    }

    /**
     * 处理订单项信息
     * @param orderItem 订单项国际化信息列表
     */
    private static void handleOrderItemVO(OrderItemVO orderItem) {
        OrderItemLangVO orderItemLangVO = handleOrderItemLangVO(orderItem.getOrderItemLangList());
        orderItem.setSpuName(orderItemLangVO.getSpuName());
        orderItem.setSkuName(orderItemLangVO.getSkuName());
        orderItem.setOrderItemLangList(null);
    }


    /**
     * 处理es订单项信息
     * @param orderItem 订单项国际化信息列表
     */
    private static void handleEsOrderItem(EsOrderItemVO orderItem) {
//        OrderItemLangVO orderItemLangVO = handleOrderItemLangVO(orderItem.getOrderItemLangList());
//        orderItem.setSpuName(orderItemLangVO.getSpuName());
//        orderItem.setSkuName(orderItemLangVO.getSkuName());
//        orderItem.setOrderItemLangList(null);
    }

    /**
     * 处理订单项语言信息
     * @param orderItemLangList 订单项国际化信息列表
     */
    public static OrderItemLangVO handleOrderItemLangVO(List<OrderItemLangVO> orderItemLangList) {
        Map<Integer, OrderItemLangVO> spuNameMap = orderItemLangList.stream().collect(Collectors.toMap(OrderItemLangVO::getLang, o -> o));
        OrderItemLangVO orderItemLangVO = spuNameMap.get(I18nMessage.getLang());
        if (Objects.isNull(orderItemLangVO)) {
            orderItemLangVO = spuNameMap.get(Constant.DEFAULT_LANG);
        }
        return orderItemLangVO;
    }

    /**
     * 获取spu名称
     * @param orderSpuLangList spu国际化信息列表
     */
    public static String getSpuName(List<OrderSpuLangVO> orderSpuLangList) {
        Map<Integer, String> spuMap = orderSpuLangList.stream()
                .filter(orderSpuLangVO -> StrUtil.isNotBlank(orderSpuLangVO.getSpuName()))
                .collect(Collectors.toMap(OrderSpuLangVO::getLang, OrderSpuLangVO::getSpuName));
        return LangUtil.getLangValue(spuMap);
    }

    /**
     * 获取sku名称
     * @param orderSkuLangList sku国际化信息列表
     */
    public static String getSkuName(List<OrderSkuLangVO> orderSkuLangList) {
        Map<Integer, String> spuMap = orderSkuLangList.stream()
                .filter(orderSkuLangVO -> StrUtil.isNotBlank(orderSkuLangVO.getSkuName()))
                .collect(Collectors.toMap(OrderSkuLangVO::getLang, OrderSkuLangVO::getSkuName));
        return LangUtil.getLangValue(spuMap);
    }
}
