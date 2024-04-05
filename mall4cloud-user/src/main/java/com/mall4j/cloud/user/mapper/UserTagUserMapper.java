package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.user.model.UserTagUser;
import com.mall4j.cloud.user.vo.UserTagUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户和标签关联表
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserTagUserMapper {

	/**
	 * 获取用户和标签关联表列表
	 * @return 用户和标签关联表列表
	 */
	List<UserTagUserVO> list();

	/**
	 * 根据用户和标签关联表id获取用户和标签关联表
	 *
	 * @param userTagUserId 用户和标签关联表id
	 * @return 用户和标签关联表
	 */
	UserTagUser getByUserTagUserId(@Param("userTagUserId") Long userTagUserId);

	/**
	 * 保存用户和标签关联表
	 * @param userTagUser 用户和标签关联表
	 */
	void save(@Param("userTagUser") UserTagUser userTagUser);

	/**
	 * 更新用户和标签关联表
	 * @param userTagUser 用户和标签关联表
	 */
	void update(@Param("userTagUser") UserTagUser userTagUser);

	/**
	 * 根据用户和标签关联表id删除用户和标签关联表
	 * @param userTagUserId
	 */
	void deleteById(@Param("userTagUserId") Long userTagUserId);

	/**
	 * 获取用户的标签
	 * @param userId 用户id
	 * @return 用户标签列表
	 */
    List<UserTagApiVO> getUserTagsByUserId(@Param("userId") Long userId);

	/**
	 * 删除标签和用户的关联关系
	 * @param userTagId 标签id
	 * @return 影响行数
	 */
    int removeByUserTagId(@Param("userTagId") Long userTagId);

	/**
	 * 批量保存用户关联表信息
	 * @param userTagUsers 用户关联标签集合
	 * @return 影响行数
	 */
	int saveBatch(@Param("userTagUsers") List<UserTagUser> userTagUsers);

	/**
	 * 查询多个用户的标签
	 * @param userIds 用户id集合
	 * @return 管理标签集合
	 */
	List<UserTagUserVO> listByUserIds(@Param("userIds") List<Long> userIds);

	/**
	 * 移除标签通过用户id和标签关联id
	 * @param userId 用户id
	 * @param userTagId 用户标签关联id
	 * @return 结果
	 */
	int removeByUserIdAndTagId(@Param("userId") Long userId, @Param("userTagId") Long userTagId);

	/**
	 * 通过标签获取标签下的用户信息
	 * @param userTagIds 标签id集合
	 * @return 用户列表
	 */
    List<UserApiVO> getUserByTagIds(@Param("userTagIds") List<Long> userTagIds);

	/**
	 * 根据标签id删除用户与标签关联关系
	 * @param tagId
	 * @return
	 */
	int deleteByTagId(@Param("tagId") Long tagId);

    List<UserTagUser> listByTag(@Param("tagId")Long tagId);

	List<UserApiVO> listUserByStaffAndTagId(@Param("staffId") Long staffId, @Param("userTagId") Long userTagId);

	void deleteByUserAndTagId(@Param("userIds") List<Long> userIds, @Param("tagId") Long tagId);

	List<UserTagApiVO> listUserTagsByUserId(@Param("staffTagIds") List<Long> staffTagIds, @Param("userId") Long userId);

	UserTagUser getByTagAndUser(@Param("tagId") Long tagId, @Param("userId") Long userId);

	void deleteByUserId(@Param("userId") Long userId);

	void deleteByTagAndUserId(@Param("tagIds") List<Long> tagIds, @Param("userId") Long userId);
}
