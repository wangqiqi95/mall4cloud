package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订阅消息优惠券
 * @author shijing
 * @date 2022-04-05
 */
@Data
public class UserCouponVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "领取时间")
    private Date receiveTime;

    @ApiModelProperty(value = "生效开始时间")
    private Date startTime;

    @ApiModelProperty(value = "生效结束时间")
    private Date endTime;

    @ApiModelProperty(value = "优惠券说明")
    private String description;

    @ApiModelProperty(value = "是否全部门店")
    private Boolean isAllShop;

    @ApiModelProperty(value = "适用门店")
    private List<Long> shops ;

    @ApiModelProperty("适用范围（0：不限/1：线上/2：线下）")
    private Short applyScopeType;

    private Long userId;

}
