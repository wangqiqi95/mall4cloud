package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserLabelDTO;
import com.mall4j.cloud.user.model.UserLabel;
import com.mall4j.cloud.user.vo.UserLabelVO;

/**
 * 导购标签信息
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
public interface UserLabelService {

	/**
	 * 分页获取导购标签信息列表
	 * @param pageDTO 分页参数
	 * @return 导购标签信息列表分页数据
	 */
	PageVO<UserLabelVO> page(PageDTO pageDTO, UserLabelDTO userLabelDTO);

	/**
	 * 根据导购标签信息id获取导购标签信息
	 *
	 * @param id 导购标签信息id
	 * @return 导购标签信息
	 */
	UserLabel getById(Long id);

	/**
	 * 保存导购标签信息
	 * @param userLabel 导购标签信息
	 */
	void save(UserLabel userLabel);

	/**
	 * 更新导购标签信息
	 * @param userLabel 导购标签信息
	 */
	void update(UserLabel userLabel);

	/**
	 * 根据导购标签信息id删除导购标签信息
	 * @param id 导购标签信息id
	 */
	void deleteById(Long id);

	void addUser(UserLabelDTO userLabelDTO);

}
