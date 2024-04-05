package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.StaffCodePageDTO;
import com.mall4j.cloud.biz.model.cp.StaffCode;
import com.mall4j.cloud.biz.vo.cp.StaffCodeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工活码
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
public interface StaffCodeMapper {

	/**
	 * 获取员工活码列表
	 * @param request 查询条件
	 * @return 获取员工活码列表
	 */
	List<StaffCodeVO> list(@Param("et") StaffCodePageDTO request);

	/**
	 * 根据员工活码id获取员工活码
	 *
	 * @param id 员工活码id
	 * @return 员工活码表
	 */
	StaffCode getById(@Param("id") Long id);

	/**
	 * 保存员工活码表
	 * @param staffCode 员工活码表
	 */
	void save(@Param("et") StaffCode staffCode);

	/**
	 * 更新员工活码表
	 * @param staffCode 员工活码表
	 */
	void update(@Param("et") StaffCode staffCode);

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
	StaffCode selectByStaffIdAndState(@Param("staffId")Long staffId, @Param("state")String state);

	/**
	 * 查询活码的配置
	 * @param staffId
	 * @return
	 */
    List<StaffCode> selectByStaffId(@Param("staffId")Long staffId);
}
