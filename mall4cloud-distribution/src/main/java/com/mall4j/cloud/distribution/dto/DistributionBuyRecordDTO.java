package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 分销推广-下单记录DTO
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Data
public class DistributionBuyRecordDTO{

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("下单人ID")
    private Long buyId;

    @ApiModelProperty("下单人名称")
    private String buyName;

    @ApiModelProperty("类型 1 导购 2威客 3会员")
    private Integer buyType;

    @ApiModelProperty("触点号")
    private String tentacleNo;

    @ApiModelProperty("分享人ID")
    private Long shareId;

    @ApiModelProperty("类型 1 导购 2威客 3会员")
    private Integer shareType;

    @ApiModelProperty("活动ID")
    private Long activityId;

    @ApiModelProperty("活动类型 1海报 2专题 3朋友圈 4商品")
    private Integer activityType;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

	@ApiModelProperty("搜索关键字")
	private String keywords;
}
