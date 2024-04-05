package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.CustGroupAssgin;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 客群分配表 
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface CustGroupAssginMapper {

	/**
	 * 获取客群分配表 列表
	 * @return 客群分配表 列表
	 */
	List<CustGroupAssgin> list();

	/**
	 * 根据客群分配表 id获取客群分配表 
	 *
	 * @param id 客群分配表 id
	 * @return 客群分配表 
	 */
	CustGroupAssgin getById(@Param("id") Long id);

	/**
	 * 保存客群分配表 
	 * @param custGroupAssgin 客群分配表 
	 */
	void save(@Param("custGroupAssgin") CustGroupAssgin custGroupAssgin);

	/**
	 * 更新客群分配表 
	 * @param custGroupAssgin 客群分配表 
	 */
	void update(@Param("custGroupAssgin") CustGroupAssgin custGroupAssgin);

	/**
	 * 根据客群分配表 id删除客群分配表 
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
