package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "签到汇总实体")
public class SignGatherVO implements Serializable {
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("签到周期天数")
    private Integer signRound;
    @ApiModelProperty("签到用户人数")
    private Integer signUserNum;
    @ApiModelProperty("签到总次数")
    private Integer signTimes;
    @ApiModelProperty("领取的积分总数")
    private Integer allPointNum;
    @ApiModelProperty("领取的优惠券总数")
    private Integer allCouponNum;
    @ApiModelProperty("常规签到积分总数")
    private Integer normalSignPointNum;
    @ApiModelProperty("常规签到优惠券总数")
    private Integer normalSignCouponNum;
    @ApiModelProperty("连签积分总数")
    private Integer seriesSignPointNum;
    @ApiModelProperty("连签优惠券总数")
    private Integer seriesSignCouponNum;
    @ApiModelProperty("连签天数与人数")
    private List<SignGatherSeriesVO> series;
}
