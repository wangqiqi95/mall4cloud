package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 余额充值级别表VO
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
@Data
public class UserRechargeVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("充值id")
    private Long rechargeId;

    @ApiModelProperty("充值余额标题")
    private String rechargeTitle;

    @ApiModelProperty("充值金额")
    private Long rechargeAmount;

    @ApiModelProperty("背景图片")
    private String img;

    @ApiModelProperty("赠送金额")
    private Long presAmount;

    @ApiModelProperty("赠送积分")
    private Long presScore;

    @ApiModelProperty("赠送成长值")
    private Integer presGrowth;

    @ApiModelProperty("顺序")
    private Integer seq;

	@ApiModelProperty("赠送优惠券的总数量")
	private Integer couponTotalNum;

}
