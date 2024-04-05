package com.mall4j.cloud.coupon.service;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.coupon.dto.CouponSyncDto;
import com.mall4j.cloud.api.coupon.dto.QueryCrmIdsDTO;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.CodeListDTO;
import com.mall4j.cloud.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.coupon.dto.CouponListDTO;
import com.mall4j.cloud.coupon.dto.SpuListDTO;
import com.mall4j.cloud.coupon.model.TCoupon;
import com.mall4j.cloud.coupon.model.TCouponCode;
import com.mall4j.cloud.coupon.vo.CouponListVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 优惠券 *
 * @author shijing
 * @date 2022-01-03 14:55:56
 */
public interface TCouponService {

	/**
	 * 分页获取优惠券列表
	 * @param param 分页参数
	 * @return 分页获取优惠券列表
	 */
	ServerResponseEntity<PageInfo<CouponListVO>> list(CouponListDTO param);

	/**
	 * 新增优惠券
	 * @param param 优惠券详情
	 */
	ServerResponseEntity<Void> save(CouponDetailDTO param);

	/**
	 * 同步优惠券
	 * @param param 优惠券详情
	 */
	ServerResponseEntity<CouponSyncDto> syncAddCoupon(com.mall4j.cloud.api.coupon.dto.CouponDetailDTO param);

	/**
	 * 优惠券详情
	 * @param id 优惠券id
	 */
	ServerResponseEntity<CouponDetailDTO> detail(Long id);

	/**
	 * 修改优惠券
	 * @param param 优惠券详情
	 */
	ServerResponseEntity<Void> update(CouponDetailDTO param);

	ServerResponseEntity<Void> addCode(CouponDetailDTO param);

	/**
	 * 同步优惠券
	 * @param param 优惠券详情
	 */
	ServerResponseEntity<CouponSyncDto> syncUpdateCoupon(com.mall4j.cloud.api.coupon.dto.CouponDetailDTO param);

	/**
	 * 失效优惠券
	 * @param id 优惠券id
	 */
	ServerResponseEntity<Void> failure(Long id);

	/**
	 * 券码导入
	 */
	ServerResponseEntity<List<String>> importCode(MultipartFile file);

	/**
	 * 根据券id查询优惠券信息
	 */
	ServerResponseEntity<List<TCoupon>> selectCouponByIds(List<Long> ids);

	/**
	 * 查看券码详情
	 */
	ServerResponseEntity<PageInfo<TCouponCode>> couponCodeList(CodeListDTO param);

	/**
	 * 查看优惠券包含商品
	 */
	ServerResponseEntity<PageVO<SpuCommonVO>> couponSpuList(SpuListDTO param);

	/**
	 * 查看优惠券适用门店
	 */
	ServerResponseEntity<PageVO<SelectedStoreVO>> couponShopList(SpuListDTO param);


	void syncCoupons();

	List<String> queryCrmIds(QueryCrmIdsDTO queryCrmIdsDTO);

	TCoupon queryByCrmCouponId(String crmCouponId);

	void removeByCrmCouponId(String crmCouponId);

	CouponDetailDTO queryCacheByCouponId(Long id);

	void removeCacheByCouponId(Long id);

//	ServerResponseEntity<Void> addCode(CouponDetailDTO param);
}
