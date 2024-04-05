package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.CpStaffCodeTimeDTO;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeTime;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 渠道活码人员时间段
 *
 * @author gmq
 * @date 2023-10-25 16:39:38
 */
public interface CpStaffCodeTimeMapper extends BaseMapper<CpStaffCodeTime> {

	/**
	 * 获取渠道活码人员时间段列表
	 * @return 渠道活码人员时间段列表
	 */
	List<CpStaffCodeTime> list();

	/**
	 * 根据渠道活码人员时间段id获取渠道活码人员时间段
	 *
	 * @param id 渠道活码人员时间段id
	 * @return 渠道活码人员时间段
	 */
	CpStaffCodeTime getById(@Param("id") Long id);

	List<CpStaffCodeTimeDTO> selectStaffTimesBySourceId(@Param("sourceId") Long sourceId);

	void deleteBySourceId(@Param("sourceId") Long sourceId);
}
