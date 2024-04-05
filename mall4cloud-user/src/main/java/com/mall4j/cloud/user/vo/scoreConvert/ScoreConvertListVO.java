package com.mall4j.cloud.user.vo.scoreConvert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "积分活动列表参数")
public class ScoreConvertListVO {

    @ApiModelProperty("积分活动ID（主键）")
    private Long convertId;

    @ApiModelProperty("标题")
    private String convertTitle;

    @ApiModelProperty("积分兑换数")
    private Long convertScore;

    @ApiModelProperty("积分活动封面")
    private String convertUrl;

    @ApiModelProperty("限制兑换总数")
    private Long maxAmount;

    @ApiModelProperty("每人限制兑换数")
    private Long personMaxAmount;

    @ApiModelProperty("兑换活动种类（0：积分换物/1：积分换券）")
    private Short convertType;

    @ApiModelProperty("状态 0：启用/1：停用")
    private Integer convertStatus;

    @ApiModelProperty("排序")
    private Long sort;

    @ApiModelProperty("优惠券id")
    private Long couponId;

    @ApiModelProperty("是否全部门店")
    private Boolean isAllShop;

    @ApiModelProperty("是否全部门店（兑换门店）")
    private Boolean isAllConvertShop;

    @ApiModelProperty("是否全部门店（优惠券）")
    private Boolean isAllCouponShop;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("商品名称")
    private String commodityName;

    @ApiModelProperty("商品图片")
    private String commodityImgUrl;

    @ApiModelProperty("发货方式（0：邮寄/1：门店自取）")
    private Short deliveryType;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("创建人ID")
    private Long createId;

    @ApiModelProperty("创建人姓名")
    private String createName;

    @ApiModelProperty("修改人ID")
    private Long updateId;

    @ApiModelProperty("修改人姓名")
    private String updateName;

    @ApiModelProperty("库存")
    private Long stocks;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("订单消费限制开关")
    private Integer orderSwitch;

    @ApiModelProperty("订单消费开始时间")
    private Date orderStartTime;

    @ApiModelProperty("订单消费结束时间")
    private Date orderEndTime;

    @ApiModelProperty("订单消费金额限制")
    private Long orderNum;

    @ApiModelProperty("订单类型限制")
    private String orderType;

    @ApiModelProperty("订单金额不足提示")
    private String orderTips;

    @ApiModelProperty("粉丝等级集合")
    private String fanLevels;

    @ApiModelProperty("粉丝等级不足提示")
    private String fanTips;

    @ApiModelProperty("礼品描述")
    private String commodityDes;

    @ApiModelProperty("用户兑换积分差")
    private Long userScoreDifference;

}
