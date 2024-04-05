package com.mall4j.cloud.user.service;

import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserTagStaffDTO;
import com.mall4j.cloud.user.model.UserTagStaff;
import com.mall4j.cloud.user.vo.StaffTagVo;

import java.util.List;

/**
 * 导购标签关系信息
 *
 * @author ZengFanChang
 * @date 2022-02-13 22:25:55
 */
public interface UserTagStaffService {

	/**
	 * 分页获取导购标签关系信息列表
	 * @param pageDTO 分页参数
	 * @return 导购标签关系信息列表分页数据
	 */
	PageVO<UserTagStaff> page(PageDTO pageDTO);

	/**
	 * 根据导购标签关系信息id获取导购标签关系信息
	 *
	 * @param id 导购标签关系信息id
	 * @return 导购标签关系信息
	 */
	UserTagStaff getById(Long id);

	/**
	 * 保存导购标签关系信息
	 * @param userTagStaff 导购标签关系信息
	 */
	void save(UserTagStaff userTagStaff);

	/**
	 * 更新导购标签关系信息
	 * @param userTagStaff 导购标签关系信息
	 */
	void update(UserTagStaff userTagStaff);

	/**
	 * 根据导购标签关系信息id删除导购标签关系信息
	 * @param id 导购标签关系信息id
	 */
	void deleteById(Long id);

	List<UserTagStaff> listByStaff(Long staffId);

	List<StaffTagVo> listSysTagByStaff(Long staffId);

	UserTagStaff getStaffTagByStaffAndTag(Long staffId, Long tagId);

	List<StaffTagVo> listStaffTag(UserTagStaffDTO dto);

	List<UserApiVO> getUserByTag(UserTagStaffDTO dto);

	void addStaffTag(UserTagStaffDTO dto);

	void deleteStaffTag(UserTagStaffDTO dto);

	void saveStaffTagUser(UserTagStaffDTO dto);

	List<UserTagApiVO> listStaffUserTag(UserTagStaffDTO dto);

	List<StaffTagVo> listStaffTagByUser(UserTagStaffDTO dto);

	void saveUserTag(UserTagStaffDTO dto);

}
