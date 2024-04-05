package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.CpCustAssginDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 离职客户分配表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface CustAssginDetailMapper {

	/**
	 * 获取离职客户分配表列表
	 * @return 离职客户分配表列表
	 */
	List<CpCustAssginDetail> list();

	/**
	 * 根据离职客户分配表id获取离职客户分配表
	 *
	 * @param id 离职客户分配表id
	 * @return 离职客户分配表
	 */
	CpCustAssginDetail getById(@Param("id") Long id);

	/**
	 * 保存离职客户分配表
	 * @param custAssginDetail 离职客户分配表
	 */
	void save(@Param("custAssginDetail") CpCustAssginDetail custAssginDetail);

	/**
	 * 更新离职客户分配表
	 * @param custAssginDetail 离职客户分配表
	 */
	void update(@Param("custAssginDetail") CpCustAssginDetail custAssginDetail);

	/**
	 * 根据离职客户分配表id删除离职客户分配表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
