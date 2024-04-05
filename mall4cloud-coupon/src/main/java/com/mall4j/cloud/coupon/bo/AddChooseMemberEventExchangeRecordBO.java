package com.mall4j.cloud.coupon.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddChooseMemberEventExchangeRecordBO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "活动ID（t_choose_member_event表ID）")
    private Long eventId;

    @ApiModelProperty(value = "所属店铺ID")
    private Long belongShopId;

    @ApiModelProperty(value = "所属商铺名称")
    private String belongShopName;

    @ApiModelProperty(value = "所属商铺编码")
    private String belongShopCode;

    @ApiModelProperty(value = "兑换个数")
    private Integer exchangeNum;

    @ApiModelProperty(value = "收货地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "收件人")
    private String consignee;

    @ApiModelProperty(value = "用户手机号码")
    private String mobile;

    @ApiModelProperty(value = "收货人手机号码")
    private String deliveryMobile;

    @ApiModelProperty(value = "会员卡号")
    private String userVipCode;

    @ApiModelProperty(value = "发货状态")
    private Integer deliveryStatus;

}
