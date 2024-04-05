package com.mall4j.cloud.coupon.service;

import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.api.vo.EsPageVO;
import com.mall4j.cloud.common.product.vo.search.ProductSearchVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.vo.CouponOrderVO;
import com.mall4j.cloud.coupon.dto.CouponDTO;
import com.mall4j.cloud.coupon.model.Coupon;
import com.mall4j.cloud.coupon.vo.CouponAppVO;
import com.mall4j.cloud.coupon.vo.CouponVO;

import java.util.Date;
import java.util.List;

/**
 * 优惠券
 *
 * @author YXF
 * @date 2020-12-08 17:22:56
 */
public interface CouponService {

	/**
	 * 分页获取优惠券列表
	 * @param pageDTO 分页参数
	 * @param couponDTO
	 * @return 优惠券列表分页数据
	 */
	PageVO<CouponVO> page(PageDTO pageDTO, CouponDTO couponDTO);

	/**
	 * 根据店铺id，获取优惠券列表
	 * @param shopId
	 * @return 优惠券列表
	 */
	List<CouponAppVO> getShopCouponList(Long shopId);

	/**
	 * 根据优惠券id，获取优惠券及关联商品信息
	 * @param couponId
	 * @return 优惠券列表
	 */
	CouponVO getCouponAndProdData(Long couponId);

	/**
	 * 根据优惠券id获取优惠券
	 *
	 * @param couponId 优惠券id
	 * @return 优惠券
	 */
	Coupon getByCouponId(Long couponId);

	/**
	 * 保存优惠券
	 * @param couponDTO
	 */
	void save(CouponDTO couponDTO);

	/**
	 * 更新优惠券
	 * @param couponDTO 优惠券
	 */
	void update(CouponDTO couponDTO);

	/**
	 * 根据优惠券id删除优惠券(逻辑删除)
	 * @param couponId
	 */
	void deleteById(Long couponId);

	/**
	 * 根据优惠券id，获取优惠券及关联的商品列表信息
	 * @param couponId
	 * @return
	 */
	CouponVO getCouponAndCouponProdsByCouponId(Long couponId);

	/**
	 * 发放优惠券给用户
	 * @param couponIds
	 * @param userId
	 */
	void batchBindCouponByCouponIds(List<Long> couponIds, Long userId);

	/**
	 * 根据优惠券id，获取商品列表
	 * @param pageDTO
	 * @param productSearch
	 * @param couponId
	 * @return
	 */
	EsPageVO<ProductSearchVO> spuListByCouponId(PageDTO pageDTO, ProductSearchDTO productSearch,  Long couponId);

	/**
	 * 用户领取优惠券
	 * @param couponId
	 */
	void receive(Long couponId);

	/**
	 * 获取用户所有的或者指定状态下的优惠券分页列表
	 * @param page
	 * @param type 类型 0：所有 1：平台优惠券 2：店铺优惠券
	 * @param status 优惠券状态 0:已过期  2:已使用
	 * @return
	 */
	PageVO<CouponAppVO> getUserCouponPage(PageDTO page, Long type, Integer status);

	/**
	 * 店铺优惠券列表
	 * @param userId
	 * @return
	 */
	List<CouponAppVO > generalCouponList(Long userId);

	/**
	 * 商品优惠券列表
	 * @param page
	 * @param userId
	 * @return
	 */
	PageVO<CouponAppVO> getProdCouponPage(PageDTO page, Long userId);

	/**
	 * 根据优惠券id列表，获取优惠券列表
	 * @param couponIds
	 * @return
	 */
	List<Coupon> getCouponListByCouponIds(List<Long> couponIds);

	/**
	 * 根据店铺和用户id，获取优惠券列表
	 * @param userId
	 * @param shopId
	 * @return
	 */
	List<CouponOrderVO> getCouponListByUserIdAndShopId(Long userId, Long shopId);

	/**
	 * 根据店铺和用户id，获取优惠券列表（新的）
	 * @param userId
	 * @param shopId
	 * @return
	 */
	List<CouponOrderVO> getCouponListByUserAndShop(Long userId, Long shopId);

	List<CouponOrderVO> getCouponListByUserAndShop(Long userId, Long shopId,List<Long> spuIds);

	/**
	 * 清除优惠券缓存
	 * @param shopId
	 * @param couponId
	 */
	void removeCacheByShopId(Long shopId, Long couponId);

	/**
	 * 更改优惠券的状态
	 * @param couponId
	 * @param status
	 */
	void changeCouponStatus(Long couponId, Integer status);

	/**
	 * 平台端优惠券管理分页
	 * @param pageDTO
	 * @param couponDTO
	 * @return
	 */
	PageVO<CouponVO> adminPage(PageDTO pageDTO, CouponDTO couponDTO);

	/**
	 * 更新活动投放状态、过期状态
	 * @param couponId
	 * @param putOnStatus 投放状态
	 * @param status 过期状态
	 */
	void changeCouponStatusAndPutOnStatus(Long couponId, Integer putOnStatus, Integer status);

	/**
	 * 平台下架优惠券
	 * @param offlineHandleEventDto
	 */
	void offline(OfflineHandleEventDTO offlineHandleEventDto);

	/**
	 * 获取下线的事件记录
	 * @param couponId
	 * @return
	 */
	OfflineHandleEventVO getOfflineHandleEvent(Long couponId);

	/**
	 * 平台审核商家提交的申请
	 * @param offlineHandleEventDto
	 * @param coupon
	 */
	void audit(OfflineHandleEventDTO offlineHandleEventDto, Coupon coupon);

	/**
	 * 违规活动提交审核
	 * @param offlineHandleEventDto
	 */
	void auditApply(OfflineHandleEventDTO offlineHandleEventDto);

	/**
	 * 根据优惠券id列表，投放状态，获取优惠券列表
	 * @param couponIds
	 * @param putOnStatus
	 * @return
	 */
    List<Coupon> getCouponListByCouponIdsAndPutOnStatus(List<Long> couponIds, Integer putOnStatus);

	/**
	 * 取消投放已过期的优惠券
	 */
    void cancelPut();

	/**
	 * 投放优惠券
	 */
	void putonCoupon();

	/**
	 * 处理商品下线
	 * @param spuIds
	 * @param shopIds
	 */
    void handleSpuOffline(List<Long> spuIds, List<Long> shopIds);
}
