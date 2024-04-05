package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.dto.UserRightsDTO;
import com.mall4j.cloud.user.model.UserRights;
import com.mall4j.cloud.user.vo.UserRightsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户权益信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserRightsMapper {

	/**
	 * 获取用户权益信息列表
	 *
	 * @param userRightsDTO 搜索参数
	 * @return 用户权益信息列表
	 */
	List<UserRightsVO> list(@Param("userRights") UserRightsDTO userRightsDTO);

	/**
	 * 根据用户权益信息id获取用户权益信息
	 *
	 * @param rightsId 用户权益信息id
	 * @return 用户权益信息
	 */
	UserRightsVO getByRightsId(@Param("rightsId") Long rightsId);

	/**
	 * 保存用户权益信息
	 *
	 * @param userRights 用户权益信息
	 */
	void save(@Param("userRights") UserRights userRights);

	/**
	 * 更新用户权益信息
	 *
	 * @param userRights 用户权益信息
	 */
	void update(@Param("userRights") UserRights userRights);

	/**
	 * 根据用户权益信息id删除用户权益信息
	 *
	 * @param rightsId
	 */
	void deleteById(@Param("rightsId") Long rightsId);

	/**
	 * 根据用户权益id获取优惠券id列表
	 * @param rightsId 用户权益id
	 * @return 优惠券id列表
	 */
	List<Long> getCouponIdsByRightsId(@Param("rightsId") Long rightsId);

	/**
	 * 统计该权益名称使用的数量
	 * @param rightsName 权益名称
	 * @param rightsId 权益id
	 * @return 数量
	 */
	int countByRightsName(@Param("rightsName")String rightsName,@Param("rightsId")Long rightsId);

	/**
	 * 根据会员类型获取全部权益列表
	 * @param levelType 会员类型
	 * @return 权益列表
	 */
	List<UserRightsVO> listRightsByLevelType(@Param("levelType")Integer levelType);
}
