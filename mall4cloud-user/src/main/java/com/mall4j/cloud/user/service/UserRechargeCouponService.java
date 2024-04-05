package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.RechargeCouponDTO;
import com.mall4j.cloud.user.model.UserRechargeCoupon;

import java.util.List;
import java.util.Set;

/**
 * 余额优惠券关联表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public interface UserRechargeCouponService {

	/**
	 * 分页获取余额优惠券关联表列表
	 * @param pageDTO 分页参数
	 * @return 余额优惠券关联表列表分页数据
	 */
	PageVO<UserRechargeCoupon> page(PageDTO pageDTO);

	/**
	 * 根据余额优惠券关联表id获取余额优惠券关联表
	 *
	 * @param rechargeId 余额优惠券关联表id
	 * @return 余额优惠券关联表
	 */
	UserRechargeCoupon getByRechargeId(Long rechargeId);

	/**
	 * 保存余额优惠券关联表
	 * @param userRechargeCoupon 余额优惠券关联表
	 */
	void save(UserRechargeCoupon userRechargeCoupon);

	/**
	 * 更新余额优惠券关联表
	 * @param userRechargeCoupon 余额优惠券关联表
	 */
	void update(UserRechargeCoupon userRechargeCoupon);

	/**
	 * 根据余额优惠券关联表id删除余额优惠券关联表
	 * @param rechargeId 余额优惠券关联表id
	 */
	void deleteById(Long rechargeId);

	/**
	 * 批量保存充值详情关联的赠送优惠券
	 * @param rechargeId
	 * @param couponList
	 */
    void insertBatch(Long rechargeId, List<RechargeCouponDTO> couponList);

	/**
	 * 删除充值表详情中的一些关联优惠券
	 * @param rechargeId
	 * @param couponIdList
	 */
	void removeByRechargeIdAndCouponId(Long rechargeId, Set<Long> couponIdList);

	/**
	 * 修改充值表详情中的一些关联优惠券
	 * @param rechargeId
	 * @param updateList
	 */
	void updateBatchByCoupons(Long rechargeId, List<RechargeCouponDTO> updateList);

	/**
	 * 取消过期优惠券的绑定
	 * @param couponIds
	 */
	void cancelBindingCoupons(List<Long> couponIds);
}
