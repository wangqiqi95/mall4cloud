package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.dto.UserLevelDTO;
import com.mall4j.cloud.user.model.UserLevel;
import com.mall4j.cloud.user.vo.UserLevelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员等级表
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserLevelMapper {

	/**
	 * 获取会员等级表列表
	 *
	 * @param levelType 等级类型
	 * @param level     等级
	 * @return 会员等级表列表
	 */
	List<UserLevelVO> list(@Param("levelType") Integer levelType, @Param("level") Integer level);

	/**
	 * 根据会员等级表id获取会员等级表
	 *
	 * @param userLevelId 会员等级表id
	 * @return 会员等级表
	 */
	UserLevelVO getByUserLevelId(@Param("userLevelId") Long userLevelId);

	/**
	 * 根据会员等级表id查优惠券张数
	 * @param userLevelId
	 * @return
	 */
	Integer countCouponsNumByUserLevelId(@Param("userLevelId") Long userLevelId);

	/**
	 * 保存会员等级表
	 *
	 * @param userLevelDTO 会员等级表
	 */
	void save(@Param("userLevel") UserLevelDTO userLevelDTO);

	/**
	 * 更新会员等级表
	 *
	 * @param userLevelDTO 会员等级表
	 */
	void update(@Param("userLevel") UserLevelDTO userLevelDTO);

	/**
	 * 根据会员等级表id删除会员等级表
	 *
	 * @param id 会员等级表id
	 */
	void deleteByUserLevelId(@Param("id") Long id);

	/**
	 * 根据等级类型，获取最大的用户等级
	 *
	 * @param levelType 等级类型
	 * @return 最大的用户等级
	 */
    Integer getUserMaxLevelByLevelType(@Param("levelType") Integer levelType);

	/**
	 * 更新会员等级状态
	 *
	 * @param userLevelIds 等级id
	 */
	void updateStatusByUserLevelIds(@Param("userLevelIds") List<Long> userLevelIds);

	/**
	 * 根据等级类型，获取当前类型等级数量
	 *
	 * @param levelType 类型
	 * @return 数量
	 */
    int countByLevelType(Integer levelType);

	/**
	 * 根据会员类型与会员等级获取会员等级数据
	 *
	 * @param levelType 会员类型
	 * @param level     会员等级
	 * @return 会员等级数据
	 */
	UserLevelVO getOneByTypeAndLevel(@Param("levelType") Integer levelType, @Param("level") Integer level);

	/**
	 * 获取每个等级相关优惠券列表
	 *
	 * @param levelType 会员类型
	 * @param level     等级
	 * @return 会员等级数据列表
	 */
	List<UserLevelVO> listLevelCouponByLevelType(@Param("levelType") Integer levelType, @Param("level") Integer level);

	/**
	 * 获取等级列表， 等级包含等级关联的分类id列表 （普通会员等级）
	 *
	 * @param nowGrowth 所需成长值
	 * @param level     等级
	 * @return 等级列表
	 */
	List<UserLevelVO> selectList(@Param("nowGrowth") Integer nowGrowth, @Param("level") Integer level);

	/**
	 * 获取当前用户作为普通会员的等级
	 * @param growth 用户当前成长值
	 * @return
	 */
	int getUserNormalLevel(@Param("growth") Integer growth);

	/**
	 * 根据类型获取当前类型的最大等级
	 *
	 * @param levelType 等级类型
	 * @return 最大等级
	 */
	int getMaxLevel(@Param("levelType") Integer levelType);

	/**
	 * 根据权益id列表，获取等级列表
	 *
	 * @param rightsIds 权益id列表
	 * @return 等级列表
	 */
    List<UserLevel> listByCouponIds(@Param("rightsIds") List<Long> rightsIds);

	List<UserLevel> listByLevelType(@Param("levelType")Integer levelType);
}
