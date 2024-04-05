package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "奖品统计列表实体")
public class PrizeCensusVO implements Serializable {
    @ApiModelProperty("奖品id")
    private Integer id;
    @ApiModelProperty("奖品名称")
    private String prizeName;
    @ApiModelProperty("奖品数量")
    private String prizeNum;
    @ApiModelProperty("奖品类型 1优惠券 2积分")
    private Integer prizeType;
    @ApiModelProperty(value = "积分数量",hidden = true)
    private Integer pointNum;
    @ApiModelProperty(value = "优惠券id",hidden = true)
    private Long couponId;
    @ApiModelProperty("今天获得奖品数")
    private Integer todayReceiveNum;
    @ApiModelProperty("累计获得奖品数")
    private Integer totalReceiveNum;
    @ApiModelProperty("今天核销优惠券数")
    private Integer todayUseCoupon;
    @ApiModelProperty("累计核销优惠券数")
    private Integer totalUseCoupon;
}
