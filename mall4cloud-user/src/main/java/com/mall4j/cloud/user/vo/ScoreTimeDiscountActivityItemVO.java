package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 积分限时折扣兑换券VO
 *
 * @author gmq
 * @date 2022-07-11 15:12:37
 */
@Data
public class ScoreTimeDiscountActivityItemVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("积分折扣活动id")
    private Long activityId;

    @ApiModelProperty("积分活动ID")
    private Long convertId;

    @ApiModelProperty("折扣百分比")
    private Integer discount;

    @ApiModelProperty(value = "兑换券-标题")
    private String convertTitle;
    @ApiModelProperty(value = "兑换券-积分换券活动类型（0：积分兑礼/1：积分换券）")
    private Short type;
    @ApiModelProperty(value = "兑换券-是否全部门店")
    private Boolean isAllShop;
    @ApiModelProperty(value = "兑换券-优惠券名称")
    private List<String> couponNames;
    @ApiModelProperty(value = "兑换券-所需积分")
    private Long convertScore;

}
