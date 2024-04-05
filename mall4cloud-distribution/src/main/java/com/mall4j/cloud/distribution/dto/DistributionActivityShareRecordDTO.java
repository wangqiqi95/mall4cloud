package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 分销推广-活动分享效果DTO
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Data
public class DistributionActivityShareRecordDTO{

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("活动ID")
    private Long activityId;

    @ApiModelProperty("活动类型 1海报 2专题 3朋友圈 4商品")
    private Integer activityType;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("分享次数")
    private Integer shareNum;

    @ApiModelProperty("浏览次数")
    private Integer browseNum;

    @ApiModelProperty("浏览人次")
    private Integer browseUserNum;

    @ApiModelProperty("加购次数")
    private Integer purchaseNum;

    @ApiModelProperty("加购人次")
    private Integer purchaseUserNum;

    @ApiModelProperty("下单次数")
    private Integer buyNum;

    @ApiModelProperty("下单人次")
    private Integer buyUserNum;

	@ApiModelProperty("记录是否存在")
	private boolean exist;

	@ApiModelProperty("记录类型 1分享 2浏览 3加购 4下单")
	private Integer recordType;

	@ApiModelProperty("查询开始时间")
	private Date queryStartDate;

	@ApiModelProperty("查询结束时间")
	private Date queryEndDate;

    /**
     * 浏览人ID
     */
    private Long browseId;

    /**
     * 类型 1 导购 2威客 3会员
     */
    private Integer browseType;

}
