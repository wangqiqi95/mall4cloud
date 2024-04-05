package com.mall4j.cloud.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.coupon.dto.TCouponActivityCentreAddDTO;
import com.mall4j.cloud.api.coupon.dto.TCouponActivityCentreParamDTO;
import com.mall4j.cloud.api.coupon.vo.CouponListVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.coupon.model.TCouponActivityCentre;
import java.util.List;

/**
 * 优惠券关联活动
 */
public interface TCouponActivityCentreService extends IService<TCouponActivityCentre> {

	/**
	 * 分页获取优惠券关联活动列表
	 * @param pageDTO 分页参数
	 * @return 优惠券关联活动列表分页数据
	 */
	PageVO<CouponListVO> page(PageDTO pageDTO, TCouponActivityCentreParamDTO paramDTO);

	List<CouponListVO> couponACList(TCouponActivityCentreParamDTO paramDTO);

	/**
	 * 保存限时调价活动优惠券
	 */
	void saveTo(TCouponActivityCentreAddDTO activityCentreReqDTO);

	/**
	 * 更新限时调价活动优惠券
	 */
	void updateTo(TCouponActivityCentreAddDTO activityCentreReqDTO);

	/**
	 * 根据限时调价活动优惠券id删除限时调价活动优惠券
	 * @param activityId 限时调价活动优惠券id
	 */
	void deleteById(Long activityId);
}
