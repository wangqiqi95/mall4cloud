package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.UserLevel;
import com.mall4j.cloud.user.model.UserLevelRights;
import com.mall4j.cloud.user.vo.UserLevelRightsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 等级-权益关联信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserLevelRightsMapper {

	/**
	 * 批量保存等级-权益关联信息
	 *
	 * @param userLevelId 会员等级Id
	 * @param rightIds    权益id
	 */
	void saveBatch(@Param("userLevelId") Long userLevelId, @Param("rightIds") List<Long> rightIds);

	/**
	 * 更新等级-权益关联信息
	 *
	 * @param userLevelRights 等级-权益关联信息
	 */
	void update(@Param("userLevelRights") UserLevelRights userLevelRights);

	/**
	 * 根据等级id删除等级-权益关联信息
	 *
	 * @param userLevelId 会员等级Id
	 */
	void deleteByUserLevelId(@Param("userLevelId") Long userLevelId);

	/**
	 * 批量删除等级-权益关联信息
	 *
	 * @param userLevelId   会员等级Id
	 * @param userRightsIds 权益Id列表
	 */
    void deleteBatch(@Param("userLevelId") Long userLevelId, @Param("userRightsIds") List<Long> userRightsIds);

	/**
	 * 根据权益id删除关联
	 *
	 * @param rightId 权益id
	 */
	void deleteByRightId(@Param("rightId") Long rightId);

	/**
	 * 根据用户登记id，获取权益列表
	 *
	 * @param userLevelId
	 * @return
	 */
    List<UserLevelRightsVO> listByUserLevelId(@Param("userLevelId") Long userLevelId);

	/**
	 * 根据权益id查出拥有此权益的会员等级id列表
	 * @param rightId 权益id
	 * @return 会员等级id列表
	 */
	List<UserLevel> listUserLevelIdByRightId(@Param("rightId") Long rightId);
}
