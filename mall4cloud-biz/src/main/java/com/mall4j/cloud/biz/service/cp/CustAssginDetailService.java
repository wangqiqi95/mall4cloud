package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.model.cp.CpCustAssginDetail;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;


/**
 * 离职客户分配表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface CustAssginDetailService {

	/**
	 * 分页获取离职客户分配表列表
	 * @param pageDTO 分页参数
	 * @return 离职客户分配表列表分页数据
	 */
	PageVO<CpCustAssginDetail> page(PageDTO pageDTO);

	/**
	 * 根据离职客户分配表id获取离职客户分配表
	 *
	 * @param id 离职客户分配表id
	 * @return 离职客户分配表
	 */
	CpCustAssginDetail getById(Long id);

	/**
	 * 保存离职客户分配表
	 * @param custAssginDetail 离职客户分配表
	 */
	void save(CpCustAssginDetail custAssginDetail);

	/**
	 * 更新离职客户分配表
	 * @param custAssginDetail 离职客户分配表
	 */
	void update(CpCustAssginDetail custAssginDetail);

	/**
	 * 根据离职客户分配表id删除离职客户分配表
	 * @param id 离职客户分配表id
	 */
	void deleteById(Long id);
}
