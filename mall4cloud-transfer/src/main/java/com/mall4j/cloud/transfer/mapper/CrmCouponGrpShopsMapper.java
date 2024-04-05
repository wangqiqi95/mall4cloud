package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.CrmCouponGrpShops;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户_券_适用范围
 * 可以使用的门店
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:30
 */
public interface CrmCouponGrpShopsMapper {

    /**
     * 获取客户_券_适用范围
     * 可以使用的门店列表
     *
     * @return 客户_券_适用范围
     * 可以使用的门店列表
     */
    List<CrmCouponGrpShops> list();

    List<CrmCouponGrpShops> listByCouponId(@Param("couponId") String couponId);

    /**
     * 根据客户_券_适用范围
     * 可以使用的门店id获取客户_券_适用范围
     * 可以使用的门店
     *
     * @param id 客户_券_适用范围
     *           可以使用的门店id
     * @return 客户_券_适用范围
     * 可以使用的门店
     */
    CrmCouponGrpShops getById(@Param("id") Long id);

    /**
     * 保存客户_券_适用范围
     * 可以使用的门店
     *
     * @param crmCouponGrpShops 客户_券_适用范围
     *                          可以使用的门店
     */
    void save(@Param("crmCouponGrpShops") CrmCouponGrpShops crmCouponGrpShops);

    /**
     * 更新客户_券_适用范围
     * 可以使用的门店
     *
     * @param crmCouponGrpShops 客户_券_适用范围
     *                          可以使用的门店
     */
    void update(@Param("crmCouponGrpShops") CrmCouponGrpShops crmCouponGrpShops);

    /**
     * 根据客户_券_适用范围
     * 可以使用的门店id删除客户_券_适用范围
     * 可以使用的门店
     *
     * @param id
     */
    void deleteById(@Param("id") Long id);
}
