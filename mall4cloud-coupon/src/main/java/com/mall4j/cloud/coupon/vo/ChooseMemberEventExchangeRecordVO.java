package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChooseMemberEventExchangeRecordVO {

    @ApiModelProperty(value = "兑换记录ID")
    private Long exchangeRecordId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "活动ID（t_choose_member_event表ID）")
    private Long eventId;

    @ApiModelProperty(value = "兑换个数")
    private Integer exchangeNum;

    @ApiModelProperty(value = "收货地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "收件人")
    private String consignee;

    @ApiModelProperty(value = "用户手机号码")
    private String mobile;

    @ApiModelProperty(value = "发货状态：0未发货，1待发放，2已发放")
    private Integer deliveryStatus;

    @ApiModelProperty(value = "物流公司")
    private String logisticsCompany;

    @ApiModelProperty(value = "查询单号")
    private String trackingNumber;

    @ApiModelProperty(value = "收件人手机号码")
    private String deliveryMobile;

    @ApiModelProperty(value = "兑换类型")
    private Integer exchangeType;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "服务门店ID")
    private Long belongShopId;

    @ApiModelProperty(value = "服务门店名称")
    private String belongShopName;

    @ApiModelProperty(value = "服务门店编码")
    private String belongShopCode;

    @ApiModelProperty(value = "导入状态：0未确认，1确认")
    private Integer exportStatus;

    @ApiModelProperty(value = "会员卡号")
    private String userVipCode;

    @ApiModelProperty(value = "活动名称")
    private String eventTitle;

    @ApiModelProperty(value = "封面图url")
    private String coverImageUrl;

    @ApiModelProperty(value = "礼品url")
    private String giftImageUrl;

    @ApiModelProperty(value = "优惠券ID")
    private Long couponId;

    @ApiModelProperty(value = "活动介绍")
    private String presentation;

}
