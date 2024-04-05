package com.mall4j.cloud.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.platform.dto.StaffQiWeiQueryDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.platform.dto.ImportStaffWeChatDto;
import com.mall4j.cloud.platform.model.Staff;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
public interface StaffMapper extends BaseMapper<Staff> {

	/**
	 * 获取员工信息列表
	 * @param staffQueryDTO 查询参数
	 * @return 员工信息列表
	 */
	List<Staff> listByStaffQueryDTO(@Param("staffQueryDTO") StaffQueryDTO staffQueryDTO);

	/**
	 * 根据员工信息id获取员工信息
	 *
	 * @param id 员工信息id
	 * @return 员工信息
	 */
	Staff getById(@Param("id") Long id);

	/**
	 * 通过企微用户id查询员工
	 *
	 * @param qiWeiUserId
	 * @return
	 */
	Staff getByQiWeiUserId(@Param("qiWeiUserId") String qiWeiUserId);

	Staff getBySysUserId(@Param("sysUserId") String sysUserId);

	/**
	 * 通过手机号查询员工
	 *
	 * @param mobile
	 * @return
	 */
	List<Staff> getByMobile(@Param("mobile") String mobile);

	/**
	 * 通过员工编码查询员工
	 *
	 * @param staffCode
	 * @return
	 */
	Staff getByStaffCode(@Param("staffCode") String staffCode);

	/**
	 * 保存员工信息
	 * @param staff 员工信息
	 */
	void save(@Param("staff") Staff staff);

	void saveBatch(@Param("staffs") List<Staff> staffs);

	/**
	 * 更新员工信息
	 * @param staff 员工信息
	 */
	void update(@Param("staff") Staff staff);

	int updateBatchWeChatNoByStaffNo(@Param("staffs") List<ImportStaffWeChatDto> staffs);

	Integer countByStaffNum(@Param("staffQueryDTO") StaffQueryDTO staffQueryDTO);

	List<Staff> getStaffListByMobiles(@Param("mobiles") List<String> mobiles);

	List<Staff> getStaffListByStaffNos(@Param("staffNos") List<String> staffNos);

    List<StaffVO> listStaffByStatus(@Param("staffQueryDTO") StaffQueryDTO staffQueryDTO);

    List<StaffVO> getByIds(@Param("ids") List<Long> ids);

	List<Staff> getStaffByWeChatNo(@Param("weChatNos") List<String> weChatNos);
}
