package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.coupon.dto.SyncPointConvertCouponDto;
import com.mall4j.cloud.api.coupon.vo.PaperCouponOrderVO;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderDetailVO;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderVo;
import com.mall4j.cloud.api.crm.dto.QueryHasCouponUsersRequest;
import com.mall4j.cloud.coupon.dto.CouponActivityDTO;
import com.mall4j.cloud.coupon.model.TCouponUser;
import com.mall4j.cloud.coupon.vo.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户优惠券
 *
 * @author shijing
 */

public interface TCouponUserMapper extends BaseMapper<TCouponUser> {

    void insertBatch(List<TCouponUser> list);

    List<MyCouponListVO> list(@Param("type") String type, @Param("userId") Long userId, @Param("startTime") Date startTime);

    List<UserCouponListVO> platformList(@Param("type") String type, @Param("userId") Long userId, @Param("startTime") Date startTime);

    MyCouponDetailVO detail(Long id);

    List<MyCouponListVO> getCouponListByBatchId(@Param("batchId") Long batchId, @Param("userId")Long userId);

    List<TCouponUser> couponUserList(TCouponUser param);

    void updateCouponStatus(@Param("orderIds")List<Long> orderIds,@Param("status")Integer status);

    List<TCouponUser> selectOrderNo(@Param("startTime")Date startTime, @Param("endTime")Date endTime);

    TCouponUser selectByOrderNo(Long OrderNo);

    TCouponUser selectByOrderNo2(Long OrderNo);

    TCouponUser selectByCouponCode(@Param("couponCode")String couponCode);
    TCouponUserOrderVo selectTCouponUserOrderVoByCouponCode(@Param("couponCode")String couponCode);

    List<TCouponUserOrderVo> selectByOrderIds(@Param("orderIds")List<Long> orderIds);

    WriteOffDetailVO writeOffCouponDetail(String couponCode);

    /**
     *
     * @param orderIds
     * @return
     */
    List<TCouponUserOrderDetailVO> getCouponsByOrderIds(@Param("orderIds") List<Long> orderIds);

    List<CouponActivityDetailVO> couponActivityDetail(CouponActivityDTO param);

    List<TCouponUser> indexStatistics(@Param("activityId") Long activityId,
                                      @Param("activitySource")Integer activitySource,
                                      @Param("couponInfo")String couponInfo,
                                      @Param("type") String type);

    Integer indexStatisticsCount(@Param("activityId") Long activityId,
                                      @Param("activitySource")Integer activitySource,
                                      @Param("couponInfo")String couponInfo,
                                      @Param("type") String type);

    BigDecimal activityIncome(@Param("activityId") Long activityId,
                              @Param("activitySource")Integer activitySource,
                              @Param("couponInfo")String couponInfo,
                              @Param("type") String type);

    List<UserCouponVO> couponOverdue(@Param("userId") Long userId, @Param("startTime")Date startTime, @Param("endTime")Date endTime);

    int crmUseCoupon(@Param("couponCode")String couponCode,@Param("orderNo")Long orderNo);

    int crmFreezeCoupon(@Param("couponCode")String couponCode,@Param("orderNo")Long orderNo,@Param("orderAmount")Long orderAmount,@Param("couponAmount")Long couponAmount);

    int crmEffective(@Param("couponCode")String couponCode);

    int updateByCouponCode(@Param("param") TCouponUser param);

    List<Long> getCouponUserIds(QueryHasCouponUsersRequest queryHasCouponUsersRequest);

    List<SyncPointConvertCouponDto> getSyncPointConvertCouponByBatchIds(@Param("batchIds") List<Long> batchIds);
    
    List<PaperCouponOrderVO> listPaperCouponOrder(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
