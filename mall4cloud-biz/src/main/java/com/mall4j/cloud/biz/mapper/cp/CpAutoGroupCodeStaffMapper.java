package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.CpAutoGroupCodeStaffSelectDTO;
import com.mall4j.cloud.biz.model.cp.CpAutoGroupCodeStaff;
import com.mall4j.cloud.biz.vo.cp.CpAutoGroupCodeStaffVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2023-10-27 16:59:44
 */
public interface CpAutoGroupCodeStaffMapper extends BaseMapper<CpAutoGroupCodeStaff> {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<CpAutoGroupCodeStaffVO> list(@Param("dto") CpAutoGroupCodeStaffSelectDTO dto);

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return 
	 */
	CpAutoGroupCodeStaff getById(@Param("id") Long id);

	/**
	 * 保存
	 * @param cpAutoGroupCodeStaff 
	 */
	void save(@Param("cpAutoGroupCodeStaff") CpAutoGroupCodeStaff cpAutoGroupCodeStaff);

	/**
	 * 更新
	 * @param cpAutoGroupCodeStaff 
	 */
	void update(@Param("cpAutoGroupCodeStaff") CpAutoGroupCodeStaff cpAutoGroupCodeStaff);

	/**
	 * 根据id删除
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void deleteByCodeId(@Param("codeId") Long codeId);
}
