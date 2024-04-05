package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.CrmCouponGrp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户_券_券库信息
 * <p>
 * 维护某一类型券的库公用信息
 * 类型:代金券、折扣券、礼品券、邀请
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:29
 */
public interface CrmCouponGrpMapper {

    /**
     * 获取客户_券_券库信息
     * <p>
     * 维护某一类型券的库公用信息
     * 类型:代金券、折扣券、礼品券、邀请列表
     *
     * @return 客户_券_券库信息
     * <p>
     * 维护某一类型券的库公用信息
     * 类型:代金券、折扣券、礼品券、邀请列表
     */
    List<CrmCouponGrp> list();

    /**
     * 根据客户_券_券库信息
     * <p>
     * 维护某一类型券的库公用信息
     * 类型:代金券、折扣券、礼品券、邀请id获取客户_券_券库信息
     * <p>
     * 维护某一类型券的库公用信息
     * 类型:代金券、折扣券、礼品券、邀请
     *
     * @param id 客户_券_券库信息
     *           <p>
     *           维护某一类型券的库公用信息
     *           类型:代金券、折扣券、礼品券、邀请id
     * @return 客户_券_券库信息
     * <p>
     * 维护某一类型券的库公用信息
     * 类型:代金券、折扣券、礼品券、邀请
     */
    CrmCouponGrp getById(@Param("id") Long id);

    /**
     * 保存客户_券_券库信息
     * <p>
     * 维护某一类型券的库公用信息
     * 类型:代金券、折扣券、礼品券、邀请
     *
     * @param crmCouponGrp 客户_券_券库信息
     *                     <p>
     *                     维护某一类型券的库公用信息
     *                     类型:代金券、折扣券、礼品券、邀请
     */
    void save(@Param("crmCouponGrp") CrmCouponGrp crmCouponGrp);

    /**
     * 更新客户_券_券库信息
     * <p>
     * 维护某一类型券的库公用信息
     * 类型:代金券、折扣券、礼品券、邀请
     *
     * @param crmCouponGrp 客户_券_券库信息
     *                     <p>
     *                     维护某一类型券的库公用信息
     *                     类型:代金券、折扣券、礼品券、邀请
     */
    void update(@Param("crmCouponGrp") CrmCouponGrp crmCouponGrp);

    /**
     * 根据客户_券_券库信息
     * <p>
     * 维护某一类型券的库公用信息
     * 类型:代金券、折扣券、礼品券、邀请id删除客户_券_券库信息
     * <p>
     * 维护某一类型券的库公用信息
     * 类型:代金券、折扣券、礼品券、邀请
     *
     * @param id
     */
    void deleteById(@Param("id") Long id);
}
