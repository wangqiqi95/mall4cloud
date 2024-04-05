package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.CrmCouponGrpSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户_券_券库信息_SKU绑定(针对SKU)
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:30
 */
public interface CrmCouponGrpSkuMapper {

    /**
     * 获取客户_券_券库信息_SKU绑定(针对SKU)列表
     *
     * @return 客户_券_券库信息_SKU绑定(针对SKU)列表
     */
    List<CrmCouponGrpSku> list();

    List<CrmCouponGrpSku> listByCouponId(@Param("couponId")String couponId);

    /**
     * 根据客户_券_券库信息_SKU绑定(针对SKU)id获取客户_券_券库信息_SKU绑定(针对SKU)
     *
     * @param id 客户_券_券库信息_SKU绑定(针对SKU)id
     * @return 客户_券_券库信息_SKU绑定(针对SKU)
     */
    CrmCouponGrpSku getById(@Param("id") Long id);

    /**
     * 保存客户_券_券库信息_SKU绑定(针对SKU)
     *
     * @param crmCouponGrpSku 客户_券_券库信息_SKU绑定(针对SKU)
     */
    void save(@Param("crmCouponGrpSku") CrmCouponGrpSku crmCouponGrpSku);

    /**
     * 更新客户_券_券库信息_SKU绑定(针对SKU)
     *
     * @param crmCouponGrpSku 客户_券_券库信息_SKU绑定(针对SKU)
     */
    void update(@Param("crmCouponGrpSku") CrmCouponGrpSku crmCouponGrpSku);

    /**
     * 根据客户_券_券库信息_SKU绑定(针对SKU)id删除客户_券_券库信息_SKU绑定(针对SKU)
     *
     * @param id
     */
    void deleteById(@Param("id") Long id);
}
