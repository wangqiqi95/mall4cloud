package com.mall4j.cloud.coupon.service;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.model.TCoupon;
import com.mall4j.cloud.coupon.vo.*;

import java.util.List;


/**
 * 商详领券 *
 * @author shijing
 * @date 2022-02-27
 */
public interface GoodsCouponActivityService {

	/**
	 * 分页获取商详领券活动列表
	 * @param param 分页参数
	 * @return 分页获取优惠券列表
	 */
	ServerResponseEntity<PageInfo<GoodsActivityListVO>> list(ActivityListDTO param);

	/**
	 * 新增活动
	 * @param param 活动详情
	 */
	ServerResponseEntity<Void> save(GoodsActivityDTO param);

	/**
	 * 活动详情
	 * @param id 活动id
	 */
	ServerResponseEntity<GoodsActivityVO> detail(Long id);

	/**
	 * 删除活动
	 * @param id 活动id
	 */
	ServerResponseEntity<Void> delete(Long id);

	/**
	 * 修改活动
	 * @param param 活动详情
	 */
	ServerResponseEntity<Void> update(GoodsActivityDTO param);

	/**
	 * 更改活动状态
	 * @param id 活动id
	 */
	ServerResponseEntity<Void> updateStatus(Long id,Short status);


	/**
	 * 删除关联门店
	 */
	ServerResponseEntity<Void> deleteShop(Long id,Long shopId,Boolean isAllShop);

	/**
	 * 新增关联门店
	 */
	ServerResponseEntity<Void> addShops(AddShopsDTO param);

	/**
	 * 商品页面优惠券列表
	 */
	ServerResponseEntity<List<AppGoodsActivityVO>> couponsForGoods(Long commodityId,Long storeId);

	/**
	 * 领取优惠券
	 */
	ServerResponseEntity<Void> userReceive(Long id,Long couponId);



}
