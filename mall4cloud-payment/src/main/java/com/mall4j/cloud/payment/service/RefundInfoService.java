package com.mall4j.cloud.payment.service;

import com.mall4j.cloud.api.payment.bo.AftersaleMerchantHandleRefundOverdueRequest;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.bo.PayRefundBO;
import com.mall4j.cloud.payment.model.RefundInfo;
import com.mall4j.cloud.payment.vo.AccountDetailVO;
import com.mall4j.cloud.payment.vo.RefundInfoVO;

import java.util.Date;

/**
 * 退款信息
 *
 * @author FrozenWatermelon
 * @date 2021-03-11 14:45:01
 */
public interface RefundInfoService {

	/**
	 * 分页获取退款信息列表
	 * @param pageDTO 分页参数
	 * @return 退款信息列表分页数据
	 */
	PageVO<RefundInfo> page(PageDTO pageDTO);

	/**
	 * 根据退款信息id获取退款信息
	 *
	 * @param refundId 退款信息id
	 * @return 退款信息
	 */
	RefundInfo getByRefundId(Long refundId);

	RefundInfo getByRefundNumber(String refundNumber);

	/**
	 * 保存退款信息
	 * @param refundInfo 退款信息
	 */
	void save(RefundInfo refundInfo);

	/**
	 * 更新退款信息
	 * @param refundInfo 退款信息
	 */
	void update(RefundInfo refundInfo);

	/**
	 * 根据退款信息id删除退款信息
	 * @param refundId 退款信息id
	 */
	void deleteById(Long refundId);

	/**
	 * 执行退款
	 * @param payRefundBO
	 */
	void doRefund(PayRefundBO payRefundBO);

	/**
	 * 视频号订单 退款超时未处理回调 业务处理service
	 * @param refundOverdueRequest
	 */
	void liveStoreAftersaleMerchantHandleRefundOverdue(AftersaleMerchantHandleRefundOverdueRequest refundOverdueRequest);

	/**
	 * 退款成功，通知各个服务
	 * @param refundInfo
	 */
	void refundSuccess(RefundInfo refundInfo);

	/**
	 * 根据时间参数获取退款账户详情
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 退款账户详情
	 */
	AccountDetailVO getRefundAccountDetail(Date startTime, Date endTime);

	/**
	 * 根据参数获取退款详情
	 * @param pageDTO 分页参数
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 退款详情
	 */
	PageVO<RefundInfoVO> getRefundInfoPage(PageDTO pageDTO,Date startTime, Date endTime);
}
