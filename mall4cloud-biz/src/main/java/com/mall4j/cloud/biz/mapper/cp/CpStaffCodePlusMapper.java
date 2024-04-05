package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.StaffCodePagePlusDTO;
import com.mall4j.cloud.biz.model.cp.CpStaffCodePlus;
import com.mall4j.cloud.biz.vo.cp.StaffCodePlusVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工活码
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface CpStaffCodePlusMapper extends BaseMapper<CpStaffCodePlus> {

	/**
	 * 获取员工活码列表
	 * @param request 查询条件
	 * @return 获取员工活码列表
	 */
	List<StaffCodePlusVO> list(@Param("et") StaffCodePagePlusDTO request);

	/**
	 * 根据员工活码id获取员工活码
	 *
	 * @param id 员工活码id
	 * @return 员工活码表
	 */
	CpStaffCodePlus getById(@Param("id") Long id);

	/**
	 * 根据员工活码id删除员工活码表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 查询活码的配置
	 * @param staffId
	 * @param state
	 * @return
	 */
	CpStaffCodePlus selectByStaffIdAndState(@Param("staffId")Long staffId, @Param("state")String state);

	/**
	 * 查询活码的配置
	 * @param staffId
	 * @return
	 */
    List<CpStaffCodePlus> selectByStaffId(@Param("staffId")Long staffId);
}
