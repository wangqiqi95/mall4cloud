package com.mall4j.cloud.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.model.CouponPackActivity;
import com.mall4j.cloud.coupon.model.CouponPackShop;
import com.mall4j.cloud.coupon.model.CouponPackStockLog;
import com.mall4j.cloud.coupon.vo.ActivityReportVO;
import com.mall4j.cloud.coupon.vo.CouponPackInfoVO;
import com.mall4j.cloud.coupon.vo.CouponPackListVO;
import com.mall4j.cloud.coupon.vo.CouponPackVO;

import java.util.List;

public interface CouponPackService extends IService<CouponPackActivity> {
    ServerResponseEntity<Void> saveOrUpdateCouponPackActivity(CouponPackDTO param);

    ServerResponseEntity<CouponPackVO> detail(Integer id);

    ServerResponseEntity<PageVO<CouponPackListVO>> couponPackPage(CouponPackPageDTO param);

    ServerResponseEntity<Void> enable(Integer id);

    ServerResponseEntity<Void> disable(Integer id);

    ServerResponseEntity<Void> delete(Integer id);

    ServerResponseEntity<Void> AddStock(AddStockDTO param);

    ServerResponseEntity<CouponPackInfoVO> info(Long activityId);

    ServerResponseEntity<Void> draw(CouponPackDrawDTO param);

    ServerResponseEntity<List<CouponPackShop>> getActivityShop(Integer activityId);

    ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param);

    ServerResponseEntity<Void> deleteActivityShop(Integer activityId,Integer shopId);

    ServerResponseEntity<Void> deleteAllShop(Integer activityId);

    ServerResponseEntity<PageVO<CouponPackListVO>> selectCouponPack(CouponPackSelectDTO param);

    /**
     * 活动报表
     */
    ServerResponseEntity<ActivityReportVO> activityReport(ActivityReportDTO param);

    void extracted(Long userId, ExpendConditionDTO expendConditionDTO);

    ServerResponseEntity<PageVO<CouponPackStockLog>> stockLog(Integer activityId, PageDTO param);
}
