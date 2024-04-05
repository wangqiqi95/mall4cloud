package com.mall4j.cloud.coupon.service;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.vo.*;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 优惠券 *
 * @author shijing
 * @date 2022-01-05
 */
public interface ReceiveCouponActivityService {

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
	ServerResponseEntity<Void> save(ReceiveActivityDTO param);

	/**
	 * 活动详情
	 * @param id 活动id
	 */
	ServerResponseEntity<ReceiveActivityDetailVO> detail(Long id);

	/**
	 * 删除活动
	 * @param param 活动id
	 */
	ServerResponseEntity<Void> delete(Long id);

	/**
	 * 修改活动
	 * @param param 活动详情
	 */
	ServerResponseEntity<Void> update(ReceiveActivityDTO param);

	/**
	 * 更改活动状态
	 * @param id 活动id
	 */
	ServerResponseEntity<Void> updateStatus(Long idm,Short status);

	/**
	 * 活动优惠券列表
	 * @param param 活动id
	 */
	ServerResponseEntity<PageInfo<ReceiveActivityCouponVO>> ActivityCouponList(ActivityCouponListVO param);

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
	 * 商城小程序-领券中心
	 */
	ServerResponseEntity<AppReceiveActivityVO> appReceiveActivity(Long storeId);
	/**
	 * 商城小程序-用户领取
	 */
	ServerResponseEntity<Void> userReceive(Long id,Long couponId,Long storeId,Long userId);

	/**
	 * 活动报表
	 */
	ServerResponseEntity<ActivityReportVO> activityReport(ActivityReportDTO param);

}
