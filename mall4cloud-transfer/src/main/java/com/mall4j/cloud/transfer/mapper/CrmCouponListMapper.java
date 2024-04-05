package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.CrmCouponList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户_券_券明细
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:30
 */
public interface CrmCouponListMapper {

	/**
	 * 获取客户_券_券明细列表
	 * @return 客户_券_券明细列表
	 */
	List<CrmCouponList> list();

    List<CrmCouponList> listByTableName(@Param("table") String name);

	/**
	 * 根据客户_券_券明细id获取客户_券_券明细
	 *
	 * @param id 客户_券_券明细id
	 * @return 客户_券_券明细
	 */
	CrmCouponList getById(@Param("id") Long id);

	/**
	 * 保存客户_券_券明细
	 * @param crmCouponList 客户_券_券明细
	 */
	void save(@Param("crmCouponList") CrmCouponList crmCouponList);

	/**
	 * 更新客户_券_券明细
	 * @param crmCouponList 客户_券_券明细
	 */
	void update(@Param("crmCouponList") CrmCouponList crmCouponList);

	/**
	 * 根据客户_券_券明细id删除客户_券_券明细
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
