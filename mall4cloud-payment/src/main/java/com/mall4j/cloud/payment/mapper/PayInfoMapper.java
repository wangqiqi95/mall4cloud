package com.mall4j.cloud.payment.mapper;

import com.mall4j.cloud.api.payment.vo.GetPayInfoByOrderIdsAndPayTypeVO;
import com.mall4j.cloud.api.payment.vo.PayInfoFeignVO;
import com.mall4j.cloud.payment.dto.PayInfoDTO;
import com.mall4j.cloud.payment.model.PayInfo;
import com.mall4j.cloud.payment.vo.AccountDetailVO;
import com.mall4j.cloud.payment.vo.PayInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 订单支付记录
 *
 * @author FrozenWatermelon
 * @date 2020-12-25 09:50:59
 */
public interface PayInfoMapper {

	/**
	 * 获取订单支付记录列表
	 *
	 * @return 订单支付记录列表
	 */
	List<PayInfo> list();

	/**
	 * 获取订单支付记录列表
	 *
	 * @return 订单支付记录列表
	 */
	List<PayInfoFeignVO> payedListByOrderIds(@Param("orderIds") List<Long> orderIds);
	
	/**
	 * 获取未支付的支付记录列表
	 * @param orderIds
	 * @return
	 */
	List<PayInfoFeignVO> getNotPayPayInfoByOrderids(@Param("orderIds") List<Long> orderIds);

	/**
	 * 根据订单支付记录id获取订单支付记录
	 *
	 * @param payId 订单支付记录id
	 * @return 订单支付记录
	 */
	PayInfo getByPayId(@Param("payId") Long payId);

	/**
	 * 根据订单id获取订单支付记录
	 *
	 * @param orderId 订单id
	 * @return 订单支付记录
	 */
	PayInfo getByOrderId(@Param("orderId") Long orderId);

	/**
	 * 保存订单支付记录
	 *
	 * @param payInfo 订单支付记录
	 */
	void save(@Param("payInfo") PayInfo payInfo);

	/**
	 * 更新订单支付记录
	 *
	 * @param payInfo 订单支付记录
	 */
	void update(@Param("payInfo") PayInfo payInfo);

	void updateByOrderIds(@Param("payInfo") PayInfo payInfo);

	/**
	 * 根据订单支付记录id删除订单支付记录
	 *
	 * @param payId
	 */
	void deleteById(@Param("payId") Long payId);

	/**
	 * 根据支付订单号获取订单支付状态
	 *
	 * @param orderIds 订单号ids
	 * @return 支付状态
	 */
	Integer getPayStatusByOrderIds(@Param("orderIds") String orderIds);

	PayInfo getUnPaydPayInfoByOrderIds(@Param("orderIds") String orderIds);

	PayInfo getPayInfoByOrderIds(@Param("orderIds") String orderIds);
	/**
	 * 查询订单是否已经支付
	 *
	 * @param orderIds 订单id
	 * @param userId   用户id
	 * @param payEntry 支付入口
     * @return 是否已经支付
	 */
	Integer isPay(@Param("orderIds") String orderIds, @Param("userId") Long userId, @Param("payEntry") Integer payEntry);

	/**
	 * 根据订单id，获取订单支付的信息
	 *
	 * @param orderIds 订单ids
	 * @return 支付信息
	 */
    List<PayInfo> listByOrderIds(@Param("orderIds") List<Long> orderIds);

	/**
	 * 根据时间参数获取收入账户详情
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 收入账户详情
	 */
	AccountDetailVO getIncomeAccountDetail(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 根据时间参数获取支付详情
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 支付详情
	 */
	List<PayInfoVO> getPayInfoVO(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 根据订单列表及支付类型获取订单支付记录
	 * @param orderIds 订单ID列表
	 * @param payType 支付类型
	 * @return
	 */
	GetPayInfoByOrderIdsAndPayTypeVO getPayInfoByOrderIdsAndPayType(@Param("orderIds") List<Long> orderIds, @Param("payType") Integer payType);

	
	/**
	 * 根据OrderNumber查询订单支付记录
	 * @param orderNumber
	 * @return
	 */
	PayInfo getPayInfoByOrderNumber(@Param("orderNumber") String orderNumber);
	
	List<PayInfo> listByPayStatusAndPayType(@Param("payStatus") Integer payStatus,@Param("payType") Integer payType);
	
}
