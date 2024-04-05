package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 分销数据-导购分销效果DTO
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Data
public class DistributionGuideShareRecordDTO{

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("导购ID")
    private Long guideId;

    @ApiModelProperty("导购名称")
    private String guideName;

    @ApiModelProperty("导购号")
    private String guideNo;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("活动类型 1海报 2专题 3朋友圈 4商品")
    private Integer activityType;

    @ApiModelProperty("数据日期")
    private Date dataTime;

    @ApiModelProperty("分享次数")
    private Integer shareNum;

    @ApiModelProperty("浏览次数")
    private Integer browseNum;

    @ApiModelProperty("浏览人次")
    private Integer browseUserNum;

    @ApiModelProperty("加购人次")
    private Integer purchaseUserNum;

    @ApiModelProperty("查询开始时间")
    private Date queryStartDate;

    @ApiModelProperty("查询结束时间")
    private Date queryEndDate;

    @ApiModelProperty("状态0 正常 1注销")
    private Integer status;
}
