package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeRef;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工活码关联表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface CpStaffCodeRefMapper extends BaseMapper<CpStaffCodeRef> {

	/**
	 * 根据员工活码id删除员工活码表
	 * @param codeId
	 */
	void deleteByCodeId(@Param("codeId") Long codeId);

	/**
	 * 根据活码id查询关联的员工
	 * @param codeId
	 * @return
	 */
	List<CpStaffCodeRef> listByCodeId(@Param("codeId") Long codeId);

	List<CpStaffCodeRef> listByCode(@Param("codeId") Long codeId,@Param("type") Integer type);

	CpStaffCodeRef getStaffCodeRefByStaffId(@Param("staffId") Long staffId);
}
