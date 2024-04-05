package com.mall4j.cloud.user.vo.scoreConvert;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "积分商城首页（商城小程序）")
public class ScoreCouponAppDetailVO {

    @ApiModelProperty(value = "积分活动ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "优惠券ID")
    private String couponId;

    @ApiModelProperty(value = "活动封面")
    private String convertUrl;

    @ApiModelProperty(value = "标题")
    private String convertTitle;

    @ApiModelProperty(value = "说明")
    private String description;

    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "所需积分")
    private Long convertScore;

    @ApiModelProperty(value = "是否全部门店")
    private Boolean isAllShop;

    @ApiModelProperty(value = "适用门店数量")
    private Integer shopNum;

    @ApiModelProperty(value = "适用门店")
    private List<Long> shops;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "积分换券活动类型（0：积分兑礼/1：积分换券）")
    private Short type;

    @ApiModelProperty(value = "积分-限时折扣活动id")
    private Long scoreTimeDisActivityId;
    @ApiModelProperty(value = "折扣积分")
    private Long discountScore;
    @ApiModelProperty("积分折扣百分比")
    private Integer scoreDiscount;
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String commodityName;

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String commodityImgUrl;
    /**
     * 礼品描述
     */
    @ApiModelProperty("礼品描述")
    private String commodityDes;

    @ApiModelProperty("0兑礼到店，1其他")
    private Integer isToShop;
}
