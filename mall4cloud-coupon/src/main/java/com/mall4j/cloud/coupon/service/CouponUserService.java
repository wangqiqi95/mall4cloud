package com.mall4j.cloud.coupon.service;

import com.mall4j.cloud.api.coupon.bo.CouponGiveBO;
import com.mall4j.cloud.api.coupon.dto.BindCouponDTO;
import com.mall4j.cloud.api.coupon.vo.CouponUserCountDataVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.coupon.dto.CouponUserCountDTO;
import com.mall4j.cloud.coupon.dto.CouponUserDTO;
import com.mall4j.cloud.coupon.model.CouponUser;
import com.mall4j.cloud.coupon.vo.CouponAppVO;
import com.mall4j.cloud.coupon.vo.CouponUserVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 优惠券用户关联信息
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
public interface CouponUserService {

	/**
	 * 分页获取优惠券用户关联信息列表
	 *
	 * @param pageDTO 分页参数
	 * @return 优惠券用户关联信息列表分页数据
	 */
	PageVO<CouponUserVO> page(PageDTO pageDTO);

	/**
	 * 根据优惠券用户关联信息id获取优惠券用户关联信息
	 *
	 * @param couponUserId 优惠券用户关联信息id
	 * @return 优惠券用户关联信息
	 */
	CouponUserVO getByCouponUserId(Long couponUserId);

	/**
	 * 保存优惠券用户关联信息
	 *
	 * @param couponUser 优惠券用户关联信息
	 */
	void save(CouponUser couponUser);

	/**
	 * 更新优惠券用户关联信息
	 *
	 * @param couponUser 优惠券用户关联信息
	 */
	void update(CouponUser couponUser);

	/**
	 * 根据优惠券用户关联信息id删除优惠券用户关联信息
	 *
	 * @param couponUserId
	 */
	void deleteById(Long couponUserId);

	/**
	 * 根据优惠券id和用户id，获取用户拥有该优惠券的数量
	 *
	 * @param couponId
	 * @param usetId
	 * @return 用户拥有该优惠券的数量
	 */
    int getUserHasCouponCount(Long couponId, Long usetId);

	/**
	 * 批量保存
	 * @param couponUsers
	 */
	void saveBatch(List<CouponUser> couponUsers);

	/**
	 * 获取用户该优惠券（couponId）的数量
	 * @param couponId
	 * @param userId
	 * @return
	 */
	int getCouponCountForUser(Long couponId, Long userId);

	/**
	 * 批量获取用户该优惠券（couponId）的数量
	 * @param couponIds 优惠券ids
	 * @param userIds 用户ids
	 * @return 集合
	 */
	List<CouponUserCountDTO> getCouponsCountForUsers(List<Long> couponIds, List<Long> userIds);
	/**
	 * 删除用户优惠券（逻辑删除）
	 * @param couponUserId
	 */
    void deleteUserCouponByCouponId(Long couponUserId);

	/**
	 * 获取用户优惠券的数量
	 * @param userId
	 * @return  key-- expiredCount:失效  allCount:全部 useCount:已使用  platformCount:平台券 multishopCount:店铺券
	 */
	Map<String, Long> getCouponCountByStatus(Long userId);

	/**
	 * 获取用户优惠券id列表
	 * @param coupons
	 * @param couponIds
	 * @return
	 */
	void listByAndShopIdOrSpuId(List<CouponAppVO> coupons, List<Long> couponIds);

	/**
	 * 获取可使用的优惠券数量
	 * @param userId
	 * @return 可使用的优惠券数量
	 */
	Integer countCanUseCoupon(Long userId);

	/**
	 * 统计用户优惠券相关数据
	 * @param userId
	 * @return
	 */
    CouponUserCountDataVO countCouponUserByUserId(Long userId);

	/**
	 * 赠送优惠券
	 * @param couponGiveBO
	 */
	void giveCoupon(CouponGiveBO couponGiveBO);


	/**
	 * 删除用户失效180天以上的优惠券
	 * @param date 180天前
	 */
    void deleteUnValidTimeCoupons(Date date);

	/**
	 * 设置用户的过期优惠券为失效状态
	 */
	void updateStatusByTime();

	/**
	 * 获取某个用户的优惠券明细
	 * @param pageDTO 分页参数
	 * @param couponUserDTO 条件查询参数
	 * @return 用户的优惠券明细
	 */
	PageVO<CouponUserVO> getPageByUserId(PageDTO pageDTO, CouponUserDTO couponUserDTO);

	/**
	 * 批量发送优惠券给客户
	 * @param bindCouponDTOList  多个用户发优惠券
	 * @return 是否成功
	 */
    boolean batchBindCoupon(List<BindCouponDTO> bindCouponDTOList);
	/**
	 * 批量删除用户优惠券
	 * @param bindCouponDTOList  多个用户失效多张优惠券
	 * @return 是否成功
	 */
	boolean batchDeleteUserCoupon(List<BindCouponDTO> bindCouponDTOList);

}
