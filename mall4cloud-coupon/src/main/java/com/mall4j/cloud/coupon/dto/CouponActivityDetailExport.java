package com.mall4j.cloud.coupon.dto;

import lombok.Data;

/**
 * @Description 券活动明细导出
 * @Author axin
 * @Date 2022-11-03 13:47
 **/
@Data
public class CouponActivityDetailExport {
    public final static String PUll_COUPON_ACTIVITY_DETAIL_FILE_NAME="推券活动明细";

    /**
     * 入参条件
     */
    private CouponActivityDTO couponActivityDTO;

    /**
     * 下载中心ID
     */
    private Long downloadCenterId;
}
