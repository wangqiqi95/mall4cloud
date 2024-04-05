package com.mall4j.cloud.user.service;

import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserAdminDTO;
import com.mall4j.cloud.user.model.UserTagUser;
import com.mall4j.cloud.user.vo.UserTagUserVO;

import java.util.List;

/**
 * 用户和标签关联表
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserTagUserService {

	/**
	 * 分页获取用户和标签关联表列表
	 * @param pageDTO 分页参数
	 * @return 用户和标签关联表列表分页数据
	 */
	PageVO<UserTagUserVO> page(PageDTO pageDTO);

	/**
	 * 根据用户和标签关联表id获取用户和标签关联表
	 *
	 * @param userTagUserId 用户和标签关联表id
	 * @return 用户和标签关联表
	 */
	UserTagUser getByUserTagUserId(Long userTagUserId);

	/**
	 * 保存用户和标签关联表
	 * @param userTagUser 用户和标签关联表
	 */
	void save(UserTagUser userTagUser);

	/**
	 * 更新用户和标签关联表
	 * @param userTagUser 用户和标签关联表
	 */
	void update(UserTagUser userTagUser);

	/**
	 * 根据用户和标签关联表id删除用户和标签关联表
	 * @param userTagUserId 用户标签关联自增id
	 */
	void deleteById(Long userTagUserId);

	/**
	 * 通过用户id获取到用户的标签
	 * @param userId 用户id
	 * @return 标签列表
	 */
    List<UserTagApiVO> getUserTagsByUserId(Long userId);

	/**
	 * 通过用户id获取到用户的标签
	 * @param userId 用户id
	 * @return 标签列表
	 */
	List<UserTagApiVO> listUserTagsByUserId(List<Long> staffTagIds, Long userId);


	/**
	 * 删除标签和用户的关联关系
	 * @param userTagId 标签id
	 */
    void removeByUserTagId(Long userTagId);

	/**
	 * 批量保存用户关联表信息
	 * @param userTagUsers 用户关联标签集合
	 * @return 影响行数
	 */
	int saveBatch(List<UserTagUser> userTagUsers);

	/**
	 * 批量修改会员标签
	 * @param userAdminDTO 用户以及标签参数
	 * @return 是否修改成功 true是 false否
	 */
    boolean batchUpdateUserTag(UserAdminDTO userAdminDTO);

	/**
	 * 移除标签通过用户id和标签关联id
	 * @param userId 用户id
	 * @param userTagId 用户标签关联id
	 * @return 结果
	 */
	boolean removeByUserIdAndTagId(Long userId, Long userTagId);

	/**
	 * 通过标签获取标签下的用户信息
	 * @param userTagIds 标签id集合
	 * @return 用户列表
	 */
	List<UserApiVO> getUserByTagIds(List<Long> userTagIds);

	/**
	 * 通过标签获取导购标签下的用户信息
	 * @param userTagId 标签id
	 * @return 用户列表
	 */
	List<UserApiVO> listUserByStaffAndTagId(Long staffId, Long userTagId);


	/**
	 * 根据用户标签id删除用户与标签关联关系
	 * @param tagId
	 */
    void deleteByTagId(Long tagId);

    void deleteByUserId(Long userId);

    List<UserTagUser> listByTag(Long tagId);

	void deleteByUserAndTagId(List<Long> userIds, Long tagId);

	void deleteByTagAndUserId(List<Long> tagIds, Long userId);

	UserTagUser getByTagAndUser(Long tagId, Long userId);

}
