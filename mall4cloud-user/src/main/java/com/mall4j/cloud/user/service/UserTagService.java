package com.mall4j.cloud.user.service;

import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserTagDTO;
import com.mall4j.cloud.user.model.UserTag;
import com.mall4j.cloud.user.vo.UserTagVO;

import java.util.List;

/**
 * 客户标签
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserTagService {

	/**
	 * 分页获取客户标签列表
	 * @param pageDTO 分页参数
	 * @return 客户标签列表分页数据
	 */
	PageVO<UserTagVO> page(PageDTO pageDTO);

	/**
	 * 根据客户标签id获取客户标签
	 *
	 * @param userTagId 客户标签id
	 * @return 客户标签
	 */
	UserTag getByUserTagId(Long userTagId);

	/**
	 * 保存客户标签
	 * @param userTag 客户标签
	 */
	Long save(UserTag userTag);

	/**
	 * 更新客户标签
	 * @param userTag 客户标签
	 */
	void update(UserTag userTag);

	/**
	 * 根据客户标签id删除客户标签
	 * @param userTagId
	 */
	void deleteById(Long userTagId);

	/**
	 * 获取所有标签列表
	 * @return  标签列表
	 */
    List<UserTagVO> list();

	/**
	 * 刷新条件标签
	 * @param userTagId 标签id
	 * @return 标签详情
	 */
	UserTagVO refreshConditionTag(Long userTagId);

	/**
	 * 统计已存在的数量
	 * @param userTag 条件参数
	 * @return 数量
	 */
    int count(UserTag userTag);

	/**
	 * 添加标签信息
	 * @param userTagDTO 标签信息
	 * @return 布尔值
	 */
	boolean addUserTag(UserTagDTO userTagDTO);

	/**
	 * 修改标签信息
	 * @param userTagDTO 标签信息
	 * @return 布尔值
	 */
	boolean updateUserTag(UserTagDTO userTagDTO);

	/**
	 * 分页获取标签
	 * @param pageDTO 分页参数
	 * @param userTagDTO 条件查询参数
	 * @return 标签分页数据
	 */
	PageVO<UserTagVO> getPage(PageDTO pageDTO, UserTagDTO userTagDTO);

	/**
	 * 通过标签id集合获取标签集合
	 * @param tagIds 标签id
	 * @return 标签列表
	 */
	List<UserTagApiVO> getUserTagList(List<Long> tagIds);



	/**
	 * 通过标签id集合获取标签集合
	 * @param tagIds 标签id
	 * @return 标签列表
	 */
	List<UserTagApiVO> getStaffUserTagList(String tagName, List<Long> tagIds);

	List<UserTagApiVO> listUserTagByType(Integer tagType);

}
