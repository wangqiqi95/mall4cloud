package com.mall4j.cloud.coupon.mapper;

import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.order.vo.CouponOrderVO;
import com.mall4j.cloud.coupon.dto.CouponDTO;
import com.mall4j.cloud.coupon.model.Coupon;
import com.mall4j.cloud.coupon.model.CouponUser;
import com.mall4j.cloud.coupon.vo.CouponAppVO;
import com.mall4j.cloud.coupon.vo.CouponVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 优惠券
 *
 * @author YXF
 * @date 2020-12-08 17:22:56
 */
public interface CouponMapper {

	/**
	 * 获取优惠券列表
	 *
	 * @param couponDTO
	 * @return 优惠券列表
	 */
	List<CouponVO> list(@Param("coupon") CouponDTO couponDTO);

	/**
	 * 获取优惠券列表
	 *
	 * @param couponDTO
	 * @return 优惠券列表
	 */
	List<CouponVO> adminList(@Param("coupon") CouponDTO couponDTO);

	/**
	 * 获取店铺的优惠券列表
	 *
	 * @param shopId
	 * @return
	 */
	List<CouponAppVO> getShopCouponList(@Param("shopId") Long shopId);

	/**
	 * 根据优惠券id获取优惠券
	 *
	 * @param couponId 优惠券id
	 * @return 优惠券
	 */
	Coupon getByCouponId(@Param("couponId") Long couponId);

	/**
	 * 保存优惠券
	 *
	 * @param coupon 优惠券
	 */
	void save(@Param("coupon") Coupon coupon);

	/**
	 * 更新优惠券
	 *
	 * @param coupon 优惠券
	 */
	void update(@Param("coupon") Coupon coupon);

	/**
	 * 更新优惠券
	 *
	 * @param coupon 优惠券
	 */
	void updateCouponAndStock(@Param("coupon") CouponDTO coupon);

	/**
	 * 根据优惠券id删除优惠券
	 *
	 * @param couponId
	 */
	void deleteById(@Param("couponId") Long couponId);

	/**
	 * 根据优惠券id，获取优惠券及关联的商品列表信息
	 *
	 * @param couponId
	 * @return
	 */
	CouponVO getCouponAndCouponProdsByCouponId(@Param("couponId") Long couponId);

	/**
	 * 根据优惠券id，获取优惠券列表
	 *
	 * @param couponIds
	 * @return 优惠券列表
	 */
	List<Coupon> getListByCouponIds(@Param("couponIds") List<Long> couponIds);

	/**
	 * 获取某个用户的优惠券列表
	 *
	 * @param userId
	 * @param status
	 * @return
	 */
	List<CouponUser> getCouponPage(@Param("userId") Long userId, @Param("status") Integer status);

	/**
	 * 获取用户所有的或者指定状态下的优惠券列表
	 *
	 * @param userId 用户id
	 * @param type   类型 0：所有 1：平台优惠券 2：店铺优惠券
	 * @param status 优惠券状态 0:已过期  2:已使用
	 * @return
	 */
	List<CouponAppVO> getUserCouponList(@Param("userId") Long userId, @Param("type") Long type, @Param("status") Integer status);

	/**
	 * 更新优惠券库存
	 *
	 * @param coupon
	 * @return
	 */
	int updateCouponStocks(@Param("coupon") Coupon coupon);

	/**
	 * 根据用户，返回对应的店铺优惠券列表
	 *
	 * @param userId
	 * @return
	 */
	List<CouponAppVO> generalCouponList(@Param("userId") Long userId);

	/**
	 * 获取用户可领取的优惠券列表
	 *
	 * @return
	 */
	List<CouponVO> getCouponList();

	/**
	 * 根据优惠券id列表，获取优惠券列表
	 *
	 * @param couponIds
	 * @param putOnStatus
	 * @return
	 */
	List<Coupon> getCouponListByCouponIds(@Param("couponIds") List<Long> couponIds, @Param("putOnStatus") Integer putOnStatus);

	/**
	 * 根据店铺和用户id，获取优惠券列表
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 */
	List<CouponOrderVO> getCouponListByUserIdAndShopId(@Param("userId") Long userId, @Param("shopId") Long shopId);

	List<CouponOrderVO> getCouponListByUserAndShop(@Param("userId") Long userId, @Param("shopId") Long shopId);

	List<CouponOrderVO> getCouponListByUserAndShopAndSpuIds(@Param("userId") Long userId, @Param("shopId") Long shopId,@Param("spuIds") List<Long> spuIds);

	List<CouponOrderVO> getCrmCouponList(@Param("shopId") Long shopId,@Param("crmCouponIds") List<String> crmCouponIds);

	List<CouponOrderVO> getCrmCouponListAndSpuIds(@Param("shopId") Long shopId,@Param("crmCouponIds") List<String> crmCouponIds,@Param("spuIds") List<Long> spuIds);

	/**
	 * 商品优惠券分页
	 *
	 * @param page
	 * @param userId
	 * @return
	 */
	List<CouponAppVO> getProdCouponList(@Param("page") PageAdapter page, @Param("userId") Long userId);

	/**
	 * 获取商品优惠券的数量
	 *
	 * @return
	 */
	Long getProdCouponListCount();

	/**
	 * 更新活动投放状态、过期状态
	 *
	 * @param couponId
	 * @param putOnStatus 投放状态
	 * @param status      过期状态
	 */
	void changeCouponStatusAndPutOnStatus(@Param("couponId") Long couponId, @Param("putOnStatus") Integer putOnStatus, @Param("status") Integer status);

	/**
	 * 查询已经过期的优惠券的店铺有哪些
	 *
	 * @return 店铺id列表
	 */
	List<Long> listOverdueStatusShopIds();

	/**
	 * 取消投放已过期的优惠券
	 */
	void cancelPut();

	/**
	 * 投放优惠券
	 */
	void putonCoupon();

	/**
	 * 批量修改优惠券库存
	 *
	 * @param coupons 修改参数
	 * @return
	 */
	int batchUpdateCouponStocks(@Param("coupons") List<Coupon> coupons);

	/**
	 * 根据商品id列表，获取优惠券
	 *
	 * @param spuIds
	 * @param shopIds
	 * @return
	 */
    List<CouponVO> listCouponBySpuIds(@Param("spuIds") List<Long> spuIds, @Param("shopIds") List<Long> shopIds);

	/**
	 * 根据优惠券id列表，下线优惠券
	 * @param couponIds
	 */
	void batchOfflineByDiscountIdsAndStatus(@Param("couponIds") List<Long> couponIds);

	/**
	 * 需要平台端取消投放的优惠券id列表
	 * @return
	 */
	List<Long> cancelPutPlatformCouponIds();

}
