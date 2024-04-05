package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 积分限时折扣VO
 *
 * @author gmq
 * @date 2022-07-11 15:11:39
 */
@Data
public class ScoreTimeDiscountVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("积分折扣活动id")
    private Long activityId;

    @ApiModelProperty("积分活动ID")
    private Long convertId;

    @ApiModelProperty("折扣百分比")
    private Integer discount;

    @ApiModelProperty("活动名称")
    private String title;

    @ApiModelProperty("周期类型：0-单周 1-每周 2-每月")
    private Integer timeType;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("按月：开始天")
    private Integer startDay;

    @ApiModelProperty("按月：结束天")
    private Integer endDay;

    @ApiModelProperty("周(多个用英文逗号分隔开:1,2,3,4,5,6,7)")
    private String weeks;

}
