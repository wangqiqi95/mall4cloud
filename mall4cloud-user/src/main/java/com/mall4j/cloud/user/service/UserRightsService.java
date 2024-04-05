package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserRightsDTO;
import com.mall4j.cloud.user.vo.UserRightsVO;

import java.util.List;

/**
 * 用户权益信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserRightsService {

	/**
	 * 分页获取用户权益信息列表
	 * @param pageDTO 分页参数
	 * @param userRightsDTO
	 * @return 用户权益信息列表分页数据
	 */
	PageVO<UserRightsVO> page(PageDTO pageDTO, UserRightsDTO userRightsDTO);


	/**
	 * 根据用户权益信息id获取用户权益信息
	 *
	 * @param rightsId 用户权益信息id
	 * @return 用户权益信息
	 */
	UserRightsVO getByRightsId(Long rightsId);

	/**
	 * 保存用户权益信息
	 * @param userRights 用户权益信息
	 */
	void save(UserRightsDTO userRights);

	/**
	 * 更新用户权益信息
	 * @param userRights 用户权益信息
	 */
	void update(UserRightsDTO userRights);

	/**
	 * 根据用户权益信息id删除用户权益信息
	 * @param rightsId
	 */
	void deleteById(Long rightsId);

	/**
	 * 获取权益列表
	 * @param userRights
	 * @return
	 */
	List<UserRightsVO> list(UserRightsDTO userRights);

	/**
	 * 删除权益缓存
	 * @param rightsId 权益id
	 */
	void removeRightsCache(Long rightsId);

	/**
	 * 删除去重后的权益列表缓存
	 * @param levelType
	 */
	void removeRightsByLevelTypeCache(Integer levelType);

	/**
	 * 根据会员类型获取全部权益列表
	 * @param levelType 会员类型
	 * @return 权益列表
	 */
	List<UserRightsVO> listRightsByLevelType(Integer levelType);

	/**
	 * 根据会员类型获取去重后的权益列表
	 * @param levelType
	 * @param userId
	 * @return
	 */
	List<UserRightsVO> listRightsByLevelType(Integer levelType, Long userId);

	/**
	 * 取消过期优惠券的绑定
	 * @param couponIds
	 */
    void cancelBindingCoupons(List<Long> couponIds);
}
