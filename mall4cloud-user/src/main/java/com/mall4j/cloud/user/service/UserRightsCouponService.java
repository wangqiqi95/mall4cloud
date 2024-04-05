package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.UserRightsCoupon;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public interface UserRightsCouponService {

	/**
	 * 分页获取列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<UserRightsCoupon> page(PageDTO pageDTO);

	/**
	 * 根据id获取
	 *
	 * @param rightsCouponId id
	 * @return 
	 */
	UserRightsCoupon getByRightsCouponId(Long rightsCouponId);

	/**
	 * 保存
	 * @param userRightsCoupon 
	 */
	void save(UserRightsCoupon userRightsCoupon);

	/**
	 * 更新
	 * @param userRightsCoupon 
	 */
	void update(UserRightsCoupon userRightsCoupon);

	/**
	 * 根据id删除
	 * @param rightsCouponId id
	 */
	void deleteById(Long rightsCouponId);
}
