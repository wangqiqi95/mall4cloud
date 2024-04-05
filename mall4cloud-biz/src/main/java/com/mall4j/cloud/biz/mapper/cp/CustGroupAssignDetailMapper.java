package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.CustGroupAssignDetailPageDTO;
import com.mall4j.cloud.biz.model.cp.CustGroupAssignDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客群分配明细表 
 *
 * @author hwy
 * @date 2022-02-10 18:25:57
 */
public interface CustGroupAssignDetailMapper {

	/**
	 * 获取客群分配明细表 列表
	 * @return 客群分配明细表 列表
	 * @param request 查询条件
	 */
	List<CustGroupAssignDetail> list(@Param("et")CustGroupAssignDetailPageDTO request);

	/**
	 * 根据客群分配明细表 id获取客群分配明细表 
	 *
	 * @param id 客群分配明细表 id
	 * @return 客群分配明细表 
	 */
	CustGroupAssignDetail getById(@Param("id") Long id);

	/**
	 * 保存客群分配明细表 
	 * @param custGroupAssignDetail 客群分配明细表 
	 */
	void save(@Param("et") CustGroupAssignDetail custGroupAssignDetail);

	/**
	 * 更新客群分配明细表 
	 * @param custGroupAssignDetail 客群分配明细表 
	 */
	void update(@Param("custGroupAssignDetail") CustGroupAssignDetail custGroupAssignDetail);

	/**
	 * 根据客群分配明细表 id删除客群分配明细表 
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 查询群信息同步到微信
	 * @return
	 */
    List<CustGroupAssignDetail> getGroupSycnList();
}
