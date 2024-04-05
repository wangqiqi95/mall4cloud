package com.mall4j.cloud.user.vo.scoreConvert;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "积分活动列表（商城小程序）")
public class ScoreCouponAppVO {

    @ApiModelProperty(value = "积分活动ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "优惠券ID")
    private String couponId;

    @ApiModelProperty(value = "活动封面")
    private String convertUrl;

    @ApiModelProperty(value = "标题")
    private String convertTitle;

    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "所需积分")
    private Long convertScore;

    @ApiModelProperty(value = "积分-限时折扣活动id")
    private Long scoreTimeDisActivityId;
    @ApiModelProperty(value = "折扣积分")
    private Long discountScore;
    @ApiModelProperty("积分折扣百分比")
    private Integer scoreDiscount;

    @ApiModelProperty(value = "积分换券活动类型（0：积分兑礼/1：积分换券）")
    private Short type;

    @ApiModelProperty(value = "兑换人数")
    private Long num;

    @ApiModelProperty("c端是否展示 0-展示 ，1 -不展示")
    private Integer isShow;
}
