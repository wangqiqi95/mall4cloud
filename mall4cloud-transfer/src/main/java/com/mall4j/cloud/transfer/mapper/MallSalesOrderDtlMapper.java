package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallSalesOrderDtl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商城_订单_官网订单信息_商品明细
 * <p>
 * 佣金按商品计算
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public interface MallSalesOrderDtlMapper {

    /**
     * 获取商城_订单_官网订单信息_商品明细
     * <p>
     * 佣金按商品计算列表
     *
     * @return 商城_订单_官网订单信息_商品明细
     * <p>
     * 佣金按商品计算列表
     */
    List<MallSalesOrderDtl> list();

    /**
     * 根据商城_订单_官网订单信息_商品明细
     * <p>
     * 佣金按商品计算id获取商城_订单_官网订单信息_商品明细
     * <p>
     * 佣金按商品计算
     *
     * @param id 商城_订单_官网订单信息_商品明细
     *           <p>
     *           佣金按商品计算id
     * @return 商城_订单_官网订单信息_商品明细
     * <p>
     * 佣金按商品计算
     */
    MallSalesOrderDtl getById(@Param("id") Long id);

    MallSalesOrderDtl getById2(@Param("id") Long id);

    List<MallSalesOrderDtl> getByOrderId(@Param("orderId") Long orderId);

    List<MallSalesOrderDtl> getByCouponId(@Param("couponId") Long couponId);

    /**
     * 保存商城_订单_官网订单信息_商品明细
     * <p>
     * 佣金按商品计算
     *
     * @param mallSalesOrderDtl 商城_订单_官网订单信息_商品明细
     *                          <p>
     *                          佣金按商品计算
     */
    void save(@Param("mallSalesOrderDtl") MallSalesOrderDtl mallSalesOrderDtl);

    /**
     * 更新商城_订单_官网订单信息_商品明细
     * <p>
     * 佣金按商品计算
     *
     * @param mallSalesOrderDtl 商城_订单_官网订单信息_商品明细
     *                          <p>
     *                          佣金按商品计算
     */
    void update(@Param("mallSalesOrderDtl") MallSalesOrderDtl mallSalesOrderDtl);

    /**
     * 根据商城_订单_官网订单信息_商品明细
     * <p>
     * 佣金按商品计算id删除商城_订单_官网订单信息_商品明细
     * <p>
     * 佣金按商品计算
     *
     * @param id
     */
    void deleteById(@Param("id") Long id);
}
