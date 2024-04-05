package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.dto.UserLabelDTO;
import com.mall4j.cloud.user.model.UserLabel;
import com.mall4j.cloud.user.vo.UserLabelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 导购标签信息
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
public interface UserLabelMapper {

	/**
	 * 获取导购标签信息列表
	 * @return 导购标签信息列表
	 */
	List<UserLabelVO> list(@Param("userLabelDTO") UserLabelDTO userLabelDTO);

	/**
	 * 根据导购标签信息id获取导购标签信息
	 *
	 * @param id 导购标签信息id
	 * @return 导购标签信息
	 */
	UserLabel getById(@Param("id") Long id);

	/**
	 * 保存导购标签信息
	 * @param userLabel 导购标签信息
	 */
	void save(@Param("userLabel") UserLabel userLabel);

	/**
	 * 更新导购标签信息
	 * @param userLabel 导购标签信息
	 */
	void update(@Param("userLabel") UserLabel userLabel);

	/**
	 * 根据导购标签信息id删除导购标签信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
