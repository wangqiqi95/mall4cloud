package com.mall4j.cloud.coupon.service;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 优惠券 *
 * @author shijing
 * @date 2022-01-05
 */
public interface PushCouponActivityService {

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
	ServerResponseEntity<Void> save(ActivityDTO param);

	/**
	 * 活动详情
	 * @param id 活动id
	 */
	ServerResponseEntity<ActivityDetailVO> detail(Long id);

	/**
	 * 删除活动
	 * @param id 活动id
	 */
	ServerResponseEntity<Void> delete(Long id);

	/**
	 * 修改活动
	 * @param param 活动详情
	 */
	ServerResponseEntity<Void> update(ActivityDTO param);

	/**
	 * 更改活动状态
	 * @param id 活动id
	 */
	ServerResponseEntity<Void> updateStatus(Long id,Short status);

	/**
	 * 活动优惠券列表
	 * @param param 活动id
	 */
	ServerResponseEntity<PageInfo<ActivityCouponVO>> ActivityCouponList(ActivityCouponListVO param);

	/**
	 * 增加库存
	 * @param id 活动id，num库存增加数
	 */
	ServerResponseEntity<Void> addInventory(AddInventoryDTO param);

	/**
	 * 库存调整记录
	 * @param param 活动id
	 */
	ServerResponseEntity<PageInfo<InventoryListVO>> inventoryLog(ActivityCouponListVO param);

	/**
	 * 导购端-送券列表
	 * @param pageDTO 分页参数
	 * @param type 优惠券类型（0：抵用券/1：折扣券）
	 */
	ServerResponseEntity<PageVO<CouponForShoppersVO>> listForShoppers(PageDTO pageDTO, Integer type);

	/**
	 * 导购端-卡券详情
	 * @param id
	 * @param userId
	 */
	ServerResponseEntity<CouponDetailForShoppersVO> detailForShoppers(Long id, Long userId);

	/**
	 * 导购端-发券
	 */
	ServerResponseEntity<Void> sendCoupon(StaffSendCouponDTO staffSendCouponDTO);

	/**
	 * 删除关联门店
	 */
	ServerResponseEntity<Void> deleteShop(Long id,Long shopId,Boolean isAllShop);

	/**
	 * 新增关联门店
	 */
	ServerResponseEntity<Void> addShops(AddShopsDTO param);

	/**
	 * 活动报表
	 */
	ServerResponseEntity<ActivityReportVO> activityReport(ActivityReportDTO param);
}
