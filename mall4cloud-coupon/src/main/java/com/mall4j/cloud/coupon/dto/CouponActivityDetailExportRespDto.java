package com.mall4j.cloud.coupon.dto;

import com.alibaba.excel.annotation.ExcelProperty;
//import com.mall4j.cloud.coupon.config.EasyExcelSQLTimestampConverter;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Description 推券明细报表
 * @Author axin
 * @Date 2022-11-03 16:01
 **/
@Data
public class CouponActivityDetailExportRespDto {
    @ExcelProperty(value = {"会员姓名"},index = 0)
    private String userName;

    @ExcelProperty(value = {"会员手机"},index = 1)
    private String userPhone;

//    @ExcelProperty(value = {"领取门店ID"}, index = 1)
//    private Long shopId;

    @ExcelProperty(value = {"领取门店编码"}, index = 2)
    private String shopCode;

    @ExcelProperty(value = {"领取门店名称"}, index = 3)
    private String shopName;

    @ExcelProperty(value = {"领取时间"}, index = 4)
    private Date receiveTime;

    @ExcelProperty(value = {"发放人id"}, index = 5)
    private Long createId;

    @ExcelProperty(value = {"发放人名称"}, index = 6)
    private String createName;

    @ExcelProperty(value = {"发放人工号"}, index = 7)
    private String createNo;

//    @ExcelProperty(value = {"核销时间"}, index = 8,converter = EasyExcelSQLTimestampConverter.class)
    private Timestamp writeOffTime;


    /**
     * 发放人手机号
     */
    //@ExcelProperty(value = {"发放人手机号"}, index = 9)
//    private String createPhone;
//
//    @ApiModelProperty(value = "优惠券id")
//    private Long couponId;
//
//    @ApiModelProperty(value = "用户id")
//    private Long userId;
//

//
//    @ApiModelProperty(value = "核销人id")
//    private Long writeOffUserId;
//
//    @ApiModelProperty(value = "核销人名称")
//    private String writeOffUserName;
//
//    @ApiModelProperty(value = "核销人工号")
//    private String writeOffUserCode;
//
//    @ApiModelProperty(value = "核销人手机号")
//    private String writeOffUserPhone;
//
//    @ApiModelProperty(value = "核销门店id")
//    private Long writeOffShopId;
//
//    @ApiModelProperty(value = "     * 核销门店名称\n")
//    private String writeOffShopName;
//
//    /**
//     * 优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
//     */
//    @ApiModelProperty(value = "优惠券来源信息： 1：小程序添加/2：CRM同步优惠券")
//    private Integer couponSourceType;
}
