package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 活动列表信息 *
 * @author shijing
 * @date 2022-01-05
 */
@Data
@ApiModel(description = "活动详情信息")
public class BuyActivityDetailVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "活动名称")
    private String title;

    @ApiModelProperty("活动入口banner")
    private String banner;

    @ApiModelProperty(value = "券码集合")
    private List<BuyActivityCouponVO> coupons;

    @ApiModelProperty(value = "是否全部门店")
    private Boolean isAllShop;

    @ApiModelProperty(value = "适用门店数量")
    private Integer shopNum;

    @ApiModelProperty("门店id列表")
    private List<Long> shops;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;
}
