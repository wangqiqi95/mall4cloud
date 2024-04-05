package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallSalesOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商城_订单_官网订单信息
 * <p>
 * 未付款，成交，发货
 * <p>
 * 关联单号，如果是退货
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public interface MallSalesOrderMapper {

    /**
     * 获取商城_订单_官网订单信息
     * <p>
     * 未付款，成交，发货
     * <p>
     * 关联单号，如果是退货列表
     *
     * @return 商城_订单_官网订单信息
     * <p>
     * 未付款，成交，发货
     * <p>
     * 关联单号，如果是退货列表
     */
    List<MallSalesOrder> list();

    /**
     * 根据商城_订单_官网订单信息
     * <p>
     * 未付款，成交，发货
     * <p>
     * 关联单号，如果是退货id获取商城_订单_官网订单信息
     * <p>
     * 未付款，成交，发货
     * <p>
     * 关联单号，如果是退货
     *
     * @param id 商城_订单_官网订单信息
     *           <p>
     *           未付款，成交，发货
     *           <p>
     *           关联单号，如果是退货id
     * @return 商城_订单_官网订单信息
     * <p>
     * 未付款，成交，发货
     * <p>
     * 关联单号，如果是退货
     */
    MallSalesOrder getById(@Param("id") Long id);

    /**
     * 保存商城_订单_官网订单信息
     * <p>
     * 未付款，成交，发货
     * <p>
     * 关联单号，如果是退货
     *
     * @param mallSalesOrder 商城_订单_官网订单信息
     *                       <p>
     *                       未付款，成交，发货
     *                       <p>
     *                       关联单号，如果是退货
     */
    void save(@Param("mallSalesOrder") MallSalesOrder mallSalesOrder);

    /**
     * 更新商城_订单_官网订单信息
     * <p>
     * 未付款，成交，发货
     * <p>
     * 关联单号，如果是退货
     *
     * @param mallSalesOrder 商城_订单_官网订单信息
     *                       <p>
     *                       未付款，成交，发货
     *                       <p>
     *                       关联单号，如果是退货
     */
    void update(@Param("mallSalesOrder") MallSalesOrder mallSalesOrder);

    /**
     * 根据商城_订单_官网订单信息
     * <p>
     * 未付款，成交，发货
     * <p>
     * 关联单号，如果是退货id删除商城_订单_官网订单信息
     * <p>
     * 未付款，成交，发货
     * <p>
     * 关联单号，如果是退货
     *
     * @param id
     */
    void deleteById(@Param("id") Long id);
}
