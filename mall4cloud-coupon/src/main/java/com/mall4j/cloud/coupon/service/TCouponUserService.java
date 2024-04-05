package com.mall4j.cloud.coupon.service;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.coupon.dto.SyncPointConvertCouponDto;
import com.mall4j.cloud.api.coupon.vo.CouponUserCountDataVO;
import com.mall4j.cloud.api.coupon.vo.PaperCouponOrderVO;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderDetailVO;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderVo;
import com.mall4j.cloud.api.crm.dto.QueryHasCouponUsersRequest;
import com.mall4j.cloud.common.order.vo.CouponOrderVO;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.model.TCouponUser;
import com.mall4j.cloud.coupon.vo.*;

import java.util.Date;
import java.util.List;


/**
 * 优惠券 *
 *
 * @author shijing
 * @date 2022-01-03 14:55:56
 */
public interface TCouponUserService {

    /**
     * 发券
     */
    ServerResponseEntity<Void> receive(ReceiveCouponDTO param);

    /**
     * 批量发券
     */
    ServerResponseEntity<Void> batchReceive(BatchReceiveCouponDTO param);

    /**
     * 用户优惠券列表
     */
    ServerResponseEntity<PageInfo<MyCouponListVO>> myCouponList(AppCouponListDTO param);

    /**
     * 后台用户优惠券列表
     */
    ServerResponseEntity<PageInfo<UserCouponListVO>> userCouponList(AppCouponListDTO param);

    /**
     * 用户优惠券详情
     */
    ServerResponseEntity<MyCouponDetailVO> myCouponDetail(Long id);

    /**
     * 根据兑换批次号查询优惠券详情
     *
     * @param param 优惠券详情
     */
    ServerResponseEntity<PageInfo<MyCouponListVO>> getCouponListByBatchId(BatchCouponListDTO param);

    /**
     * 核销优惠券
     *
     * @param code 优惠券码
     */
    ServerResponseEntity<Void> writeOff(String code, Short type, Long userId);

    /**
     * 可用优惠券列表（下单）
     */
    List<CouponOrderVO> couponList(Long userId, Long shopId, Long actualTotal, List<ShopCartItemVO> shopCartItems, String couponCode);

    /**
     * 修改优惠券状态（冻结，解冻，核销）
     */
    ServerResponseEntity<Void> updateCouponStatus(UpdateCouponStatusDTO param);

    /**
     * 导购下领券统计
     *
     * @param staffId
     * @return
     */
    ServerResponseEntity<StaffReceiveCouponStatsVO> staffReceiveCouponStats(Long staffId);

    /**
     * 导购下核销统计
     *
     * @param staffId
     * @return
     */
    ServerResponseEntity<StaffWriteOffCouponStatsVO> staffWriteOffCouponStats(Long staffId);

    ServerResponseEntity<List<TCouponUser>> couponUserList(TCouponUser param);

    ServerResponseEntity<Integer> countCanUseCoupon(Long userId);

    /**
     * 查询一段时间内，已核销的企业券信息
     */
    ServerResponseEntity<List<TCouponUser>> selectOrderNo(Date startTime, Date endTime);

    /**
     * 根据订单id查询优惠券信息
     */
    UpdateCouponStatusDTO selectByOrderNo(Long orderNo);

    TCouponUser selectByCouponCode(String couponCode);

    /**
     * 批量给用户发券
     */
    ServerResponseEntity<Void> batchReceiveCoupon(SystemCouponDTO param);

    /**
     * 查看订单是否使用企业券
     */
    Boolean isUseEnterpriseCoupon(Long orderNo);

    /**
     * 根据券码查询用户优惠券详情
     */
    ServerResponseEntity<WriteOffDetailVO> writeOffCouponDetail(String couponCode);


    /**
     * 线下核销优惠券
     */
    ServerResponseEntity<Void> writeOffCoupon(WriteOffCouponDTO param);


    /**
     * 券活动明细
     */
    PageInfo<CouponActivityDetailVO> couponActivityDetail(CouponActivityDTO param);

    /**
     * 统计用户优惠券相关数据
     *
     * @param userId
     * @return
     */
    CouponUserCountDataVO countCouponUserByUserId(Long userId);


    /**
     * 通过订单ids获取优惠券信息
     *
     * @param orderIds 优惠券ids
     * @return 集合
     */
    List<TCouponUserOrderDetailVO> getCouponsByOrderIds(List<Long> orderIds);

    List<TCouponUserOrderVo> selectByOrderIds(List<Long> orderIds);

    void syncCRMCoupon(CouponOrderVO couponOrderVO);

    List<Long> getCouponUserIds(QueryHasCouponUsersRequest queryHasCouponUsersRequest);

    String couponActivityDetailExport(CouponActivityDTO param);

    /**
     * 根据批次号同步优惠券信息
     * @param batchIds
     * @return
     */
    List<SyncPointConvertCouponDto> getSyncPointConvertCouponByBatchIds(List<Long> batchIds);
    
    List<PaperCouponOrderVO> listPaperCouponOrder(Date startTime, Date endTime);
}
