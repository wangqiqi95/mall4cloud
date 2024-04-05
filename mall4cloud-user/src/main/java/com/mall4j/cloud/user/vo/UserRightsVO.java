package com.mall4j.cloud.user.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.api.coupon.vo.CouponDataVO;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户权益信息VO
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
@Data
public class UserRightsVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("权益id")
    private Long rightsId;

    @ApiModelProperty("权益名称")
    private String rightsName;

    @ApiModelProperty("权益图标")
	@JsonSerialize(using = ImgJsonSerializer.class)
    private String icon;

    @ApiModelProperty("权益简介")
    private String description;

    @ApiModelProperty("权益详情")
	private String details;

    @ApiModelProperty("状态：-1: 删除 0:禁用 1：正常(仅用于系统核销)")
    private Integer status;

    @ApiModelProperty("排序 从小到大")
    private Integer seq;

    @ApiModelProperty("权益类型[0.自定义 1.积分回馈倍率 2.优惠券 3.积分赠送(数量) 4.会员折扣 5.包邮类型]")
    private Integer rightsType;

    @ApiModelProperty("积分回馈倍率")
    private Integer rateScore;

    @ApiModelProperty("赠送积分")
    private Long presScore;

    @ApiModelProperty("会员折扣")
    private Integer discount;

    @ApiModelProperty("折扣范围[0.全平台 1.自营店]")
    private Integer discountRange;

    @ApiModelProperty("包邮类型[1.全平台包邮 2.自营店包邮]")
    private Integer freeFeeType;

	@ApiModelProperty("优惠券id列表")
	private List<Long> couponIds;

	@ApiModelProperty("用户等级")
	private Integer level;

	@ApiModelProperty("等级名称")
	private String levelName;

    @ApiModelProperty("优惠券列表")
    private List<CouponDataVO> couponList;
}
