package com.mall4j.cloud.payment.service;

import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.payment.bo.PayInfoBO;
import com.mall4j.cloud.payment.bo.PayInfoResultBO;
import com.mall4j.cloud.payment.dto.BuyVipPayInfoDTO;
import com.mall4j.cloud.payment.dto.CouponPayInfoDTO;
import com.mall4j.cloud.payment.dto.PayInfoDTO;
import com.mall4j.cloud.payment.dto.RechargePayInfoDTO;
import com.mall4j.cloud.payment.model.PayInfo;
import com.mall4j.cloud.payment.vo.AccountDetailVO;
import com.mall4j.cloud.payment.vo.OrderPayInfoVO;
import com.mall4j.cloud.payment.vo.PayInfoVO;

import java.util.Date;
import java.util.List;

/**
 * 订单支付记录
 *
 * @author FrozenWatermelon
 * @date 2020-12-25 09:50:59
 */
public interface PayInfoService {

    /**
     * 调取微信 or 支付宝接口，创建支付订单，返回给前端，前端唤起应用进行支付
     * @param userId 用户id
     * @param payParam 支付参数
     * @return 前端唤起支付需要的参数
     */
    PayInfoBO pay(Long userId, PayInfoDTO payParam);


    /**
     * 根据订单支付记录id获取订单支付记录
     *
     * @param payId 订单支付记录id
     * @return 订单支付记录
     */
    PayInfo getByPayId(Long payId);

    /**
     * 标记为支付成功
     * @param payInfoResult 支付信息
     * @param temp 支付信息
     * @param orderIds 订单ids
     */
    void paySuccess(PayInfoResultBO payInfoResult, PayInfo temp, List<Long> orderIds);

    /**
     * 根据支付订单号获取订单支付状态
     * @param orderIds 订单号ids
     * @return 支付状态
     */
    Integer getPayStatusByOrderIds(String orderIds);

    /**
     * 查询订单是否已经支付
     * @param orderIds 订单id
     * @param userId 用户id
     * @param payEntry 支付入口 见 com.mall4j.cloud.payment.constant.PayEntry
     * @return 是否已经支付
     */
    Integer isPay(String orderIds, Long userId, Integer payEntry);

    /**
     * 标记为退款状态
     * @param payId 订单号
     */
    void markerRefund(Long payId);

    /**
     * 更新支付信息
     * @param payInfo
     */
    void update(PayInfo payInfo);

    void updateByBizOrderIds(PayInfo payInfo);

    /**
     * 根据订单id，获取订单支付的信息
     * @param orderIds 订单ids
     * @return 支付信息
     */
    List<PayInfo> listByOrderIds(List<Long> orderIds);


    /**
     * 调取微信 or 支付宝接口，创建充值订单，返回给前端，前端唤起应用进行支付
     * @param userId 用户id
     * @param payParam 支付参数
     * @return 前端唤起支付需要的参数
     */
    PayInfoBO recharge(Long userId, RechargePayInfoDTO payParam);

    /**
     * 充值成功
     * @param payInfoResult 支付信息
     * @param payType 支付方式
     * @param rechargeLogId 充值记录id
     */
    void rechargeSuccess(PayInfoResultBO payInfoResult, PayType payType, Long rechargeLogId);

    /**
     * 进行vip购买，返回支付信息
     * @param userId 用户id
     * @param payParam 支付参数
     * @return 支付信息
     */
    PayInfoBO vipBuy(Long userId, BuyVipPayInfoDTO payParam);

    /**
     * 购买会员成功
     * @param payInfoResult 支付信息
     * @param payType 支付方式
     * @param userLevelLogId 会员购买记录
     */
    void buyVipSuccess(PayInfoResultBO payInfoResult, PayType payType, Long userLevelLogId);

    /**
     * 根据时间参数获取收入账户详情
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 收入账户详情
     */
    AccountDetailVO getIncomeAccountDetail(Date startTime, Date endTime);

    /**
     * 根据时间参数获取支付详情
     * @param pageDTO 分页参数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 支付详情
     */
    PageVO<PayInfoVO> getPayInfoPage(PageDTO pageDTO,Date startTime, Date endTime);

    PayInfoBO coupon(Long userId, CouponPayInfoDTO payParam);

    void couponSuccess(PayInfoResultBO payInfoResultBO, PayType instance, Long couponOrderId);
    
    ServerResponseEntity<OrderPayInfoVO> queryOrderPayType(Long orderId);
    
    /**
     * 根据payStatus和payType获取支付详情
     * @param payStatus 支付状态
     * @param payType 支付类型
     * @return
     */
    List<PayInfo> listByPayStatusAndPayType(Integer payStatus ,Integer payType);
    
    PayInfoVO queryPayInfo(Long orderId);
}
