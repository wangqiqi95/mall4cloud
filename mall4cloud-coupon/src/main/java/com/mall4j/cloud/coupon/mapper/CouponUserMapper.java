package com.mall4j.cloud.coupon.mapper;

import com.mall4j.cloud.api.coupon.vo.CouponUserCountDataVO;
import com.mall4j.cloud.coupon.dto.CouponUserCountDTO;
import com.mall4j.cloud.coupon.dto.CouponUserDTO;
import com.mall4j.cloud.coupon.model.CouponUser;
import com.mall4j.cloud.coupon.vo.CouponUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 优惠券用户关联信息
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
public interface CouponUserMapper {

	/**
	 * 获取优惠券用户关联信息列表
	 *
	 * @return 优惠券用户关联信息列表
	 */
	List<CouponUserVO> list();

	/**
	 * 根据优惠券用户关联信息id获取优惠券用户关联信息
	 *
	 * @param couponUserId 优惠券用户关联信息id
	 * @return 优惠券用户关联信息
	 */
	CouponUserVO getByCouponUserId(@Param("couponUserId") Long couponUserId);

	/**
	 * 保存优惠券用户关联信息
	 *
	 * @param couponUser 优惠券用户关联信息
	 */
	void save(@Param("couponUser") CouponUser couponUser);

	/**
	 * 更新优惠券用户关联信息
	 *
	 * @param couponUser 优惠券用户关联信息
	 */
	void update(@Param("couponUser") CouponUser couponUser);

	/**
	 * 根据优惠券用户关联信息id删除优惠券用户关联信息
	 *
	 * @param couponUserId
	 */
	void deleteById(@Param("couponUserId") Long couponUserId);

	/**
	 * 根据优惠券id和用户id，获取用户拥有该优惠券的数量
	 *
	 * @param couponId
	 * @param userId
	 * @return 用户拥有该优惠券的数量
	 */
    int getUserHasCouponCount(@Param("couponId") Long couponId, @Param("userId") Long userId);

	/**
	 * 批量保存用户优惠券管理信息
	 *
	 * @param couponUsers
	 */
	void saveBatch(@Param("couponUsers") List<CouponUser> couponUsers);

	/**
	 * 获取用户拥有的优惠券数量
	 *
	 * @param couponId
	 * @param userId
	 * @return
	 */
	int getCouponCountForUser(@Param("couponId") Long couponId, @Param("userId") Long userId);

	/**
	 * 删除用户优惠券（逻辑删除）
	 *
	 * @param userId
	 * @param couponUserId
	 */
	void deleteUserCouponByCouponId(@Param("userId") Long userId, @Param("couponUserId") Long couponUserId);


	/**
	 * 批量更新优惠券状态
	 *
	 * @param status        状态
	 * @param userCouponIds 用户优惠券ids
	 */
	void batchUpdateUserCouponStatus(@Param("status") Integer status, @Param("userCouponIds") List<Long> userCouponIds);

	/**
	 * 获取用户优惠券的数量
	 *
	 * @param userId
	 * @return key--  expiredCount:失效  allCount:全部 useCount:已使用  platformCount:平台券 multishopCount:店铺券
	 */
	Map<String, Long> getCouponCountByStatus(@Param("userId") Long userId);

	/**
	 * 获取用户优惠券id列表
	 *
	 * @param userId
	 * @param couponIds
	 * @return
	 */
    List<CouponUserCountDTO> listByAndShopIdOrSpuId(@Param("userId") Long userId, @Param("couponIds") List<Long> couponIds);

	/**
	 * 获取可使用的优惠券数量
	 *
	 * @param userId
	 * @return 可使用的优惠券数量
	 */
    Integer countCanUseCoupon(@Param("userId") Long userId);

	/**
	 * 还原订单使用的商家优惠券
	 *
	 * @param userCouponId
	 */
	void reductionCoupon(@Param("userCouponId") Long userCouponId);

	/**
	 * 统计用户优惠券相关数据
	 *
	 * @param userId
	 * @return
	 */
    CouponUserCountDataVO countCouponUserByUserId(@Param("userId") Long userId);

	/**
	 * 删除用户失效180天以上的优惠券
	 * @param date 180天前
	 */
    void deleteUnValidTimeCoupons(@Param("date") Date date);

	/**
	 * 设置用户的过期优惠券为失效状态
	 */
	void updateStatusByTime();

	/**
	 * 条件查询，获取用户优惠券列表
	 * @param couponUserDTO 条件查询参数
	 * @return 用户优惠券列表
	 */
	List<CouponUserVO> getPageByUserId(@Param("couponUserDTO") CouponUserDTO couponUserDTO);

	/**
	 * 获取领券会员数
	 * @param shopId 店铺id
	 * @param startTime 起始时间
	 * @param endTime 结束时间
	 * @return 领券会员数
	 */
	List<Long> countMemberCouponByParam(@Param("shopId") Long shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 获取用户未使用、未删除的用户优惠券
	 * @param userIds 用户ids
	 * @param couponIds 优惠券ids
	 * @return 用户优惠券集合
	 */
    List<CouponUser> getByCouponIdsAndUserIds(@Param("userIds") List<Long> userIds, @Param("couponIds") List<Long> couponIds);

	/**
	 * 批量逻辑删除用户优惠券
	 * @param couponUserIds 被删除用户优惠券id集合
	 * @param now 修改时间
	 * @return 删除行数
	 */
	int batchDeleteUserCoupon(@Param("couponUserIds") List<Long> couponUserIds, @Param("now") Date now);

	/**
	 * 批量获取用户该优惠券（couponId）的数量
	 * @param couponIds 优惠券ids
	 * @param userIds 用户ids
	 * @return 集合
	 */
    List<CouponUserCountDTO> getCouponsCountForUsers(@Param("couponIds") List<Long> couponIds, @Param("userIds") List<Long> userIds);
}
