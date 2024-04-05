package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.SelectCpPhoneTaskRelDTO;
import com.mall4j.cloud.biz.model.cp.CpPhoneTaskStaff;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskStaffVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 引流手机号任务关联员工
 *
 * @author gmq
 * @date 2023-10-30 17:13:37
 */
public interface CpPhoneTaskStaffMapper extends BaseMapper<CpPhoneTaskStaff> {

	/**
	 * 获取引流手机号任务关联员工列表
	 * @return 引流手机号任务关联员工列表
	 */
	List<CpPhoneTaskStaffVO> list(@Param("dto") SelectCpPhoneTaskRelDTO dto);

	/**
	 * 根据引流手机号任务关联员工id获取引流手机号任务关联员工
	 *
	 * @param id 引流手机号任务关联员工id
	 * @return 引流手机号任务关联员工
	 */
	CpPhoneTaskStaff getById(@Param("id") Long id);

	/**
	 * 根据引流手机号任务关联员工id删除引流手机号任务关联员工
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 移动端首页待办事项统计
	 * @param staffId
	 * @return
	 */
	Integer waitMatterCountByStaffId(@Param("staffId") Long staffId);
}
