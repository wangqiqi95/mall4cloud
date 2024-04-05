package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.model.cp.CustGroupAssgin;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;


/**
 * 客群分配表 
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface CustGroupAssginService {

	/**
	 * 分页获取客群分配表 列表
	 * @param pageDTO 分页参数
	 * @return 客群分配表 列表分页数据
	 */
	PageVO<CustGroupAssgin> page(PageDTO pageDTO);

	/**
	 * 根据客群分配表 id获取客群分配表 
	 *
	 * @param id 客群分配表 id
	 * @return 客群分配表 
	 */
	CustGroupAssgin getById(Long id);

	/**
	 * 保存客群分配表 
	 * @param custGroupAssgin 客群分配表 
	 */
	void save(CustGroupAssgin custGroupAssgin);

	/**
	 * 更新客群分配表 
	 * @param custGroupAssgin 客群分配表 
	 */
	void update(CustGroupAssgin custGroupAssgin);

	/**
	 * 根据客群分配表 id删除客群分配表 
	 * @param id 客群分配表 id
	 */
	void deleteById(Long id);
}
