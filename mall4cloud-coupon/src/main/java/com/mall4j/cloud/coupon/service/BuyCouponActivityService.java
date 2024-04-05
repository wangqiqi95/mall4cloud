package com.mall4j.cloud.coupon.service;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.model.BuyCouponLog;
import com.mall4j.cloud.coupon.vo.*;

import java.util.List;


/**
 * 优惠券 *
 * @author shijing
 * @date 2022-01-05
 */
public interface BuyCouponActivityService {

	/**
	 * 分页获取优惠券活动列表
	 * @param param 分页参数
	 * @return 分页获取优惠券列表
	 */
	ServerResponseEntity<PageInfo<ActivityListVO>> list(ActivityListDTO param);

	/**
	 * 新增活动
	 * @param param 活动详情
	 */
	ServerResponseEntity<Void> save(BuyActivityDTO param);

	/**
	 * 活动详情
	 * @param id 活动id
	 */
	ServerResponseEntity<BuyActivityDetailVO> detail(Long id);

	/**
	 * 删除活动
	 * @param param 活动id
	 */
	ServerResponseEntity<Void> delete(Long id);

	/**
	 * 修改活动
	 * @param param 活动详情
	 */
	ServerResponseEntity<Void> update(BuyActivityDTO param);

	/**
	 * 更改活动状态
	 * @param param 活动id
	 */
	ServerResponseEntity<Void> updateStatus(Long id,Short status);

	/**
	 * 活动优惠券列表
	 * @param param 活动id
	 */
	ServerResponseEntity<PageInfo<BuyActivityCouponVO>> ActivityCouponList(ActivityCouponListVO param);

	/**
	 * 增加库存
	 * @param param 活动id，num库存增加数
	 */
	ServerResponseEntity<Void> addInventory(AddInventoryDTO param);

	/**
	 * 库存调整记录
	 * @param param 活动id
	 */
	ServerResponseEntity<PageInfo<InventoryListVO>> inventoryLog(ActivityCouponListVO param);

	/**
	 * 删除关联门店
	 */
	ServerResponseEntity<Void> deleteShop(Long id,Long shopId,Boolean isAllShop);

	/**
	 * 新增关联门店
	 */
	ServerResponseEntity<Void> addShops(AddShopsDTO param);

	/**
	 * 商城小程序-现金买券首页
	 */
	ServerResponseEntity<List<AppBuyActivityVO>> appBuyActivity();

	/**
	 * 商城小程序-现金买券详情
	 */
	ServerResponseEntity<AppBuyActivityDetailVO> appBuyActivityDetail(Long id);

	/**
	 * 商城小程序-优惠券详情
	 */
	ServerResponseEntity<AppBuyCouponDetailVO> appCouponDetail(Long id);

	/**
	 * 商城小程序-买券
	 */
	ServerResponseEntity<Long> buyCoupon(Long id,Long couponId,Long storeId);

	/**
	 * 商城小程序-买券支付的回调
	 */
	ServerResponseEntity<Void> payCoupon(PayCouponDTO param);

	/**
	 * 商城小程序-买券记录
	 */
	ServerResponseEntity<PageInfo<BuyCouponLog>> buyCouponLog(Integer pageNo,Integer pageSize);

	/**
	 * 活动报表
	 */
	ServerResponseEntity<ActivityReportVO> activityReport(ActivityReportDTO param);

}
