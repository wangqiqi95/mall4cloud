package com.mall4j.cloud.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.platform.vo.TzTagDetailVO;
import com.mall4j.cloud.platform.model.TzTag;
import com.mall4j.cloud.platform.model.TzTagStaff;
import com.mall4j.cloud.platform.vo.TzTagStaffVO;
import com.mall4j.cloud.platform.vo.TzTagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签关联员工
 *
 * @author gmq
 * @date 2022-09-13 12:01:45
 */
public interface TzTagStaffMapper extends BaseMapper<TzTagStaff> {

	/**
	 * 获取标签关联员工列表
	 * @return 标签关联员工列表
	 */
	List<TzTagStaffVO> listByTagId(@Param("tagId")Long tagId);

	/**
	 * 获取员工标签
	 * @param staffId
	 * @return
	 */
	List<TzTagDetailVO> listTagAndStoreByStaffId(@Param("staffId")Long staffId);
}
