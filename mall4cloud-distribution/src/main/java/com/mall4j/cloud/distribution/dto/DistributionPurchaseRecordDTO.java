package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 分销推广-加购记录DTO
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Data
public class DistributionPurchaseRecordDTO{

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("加购人ID")
    private Long purchaseId;

    @ApiModelProperty("加购人名称")
    private String purchaseName;

    @ApiModelProperty("类型 1 导购 2威客 3会员")
    private String purchaseType;

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

    @ApiModelProperty("门店ID集合")
    private List<Long> storeIds;

	@ApiModelProperty("门店类型 1门店 2片区 3全部")
	private Integer storeType;

	@ApiModelProperty("开始时间")
	private Date startDate;

	@ApiModelProperty("结束时间")
	private Date endDate;

    @ApiModelProperty("搜索关键字")
    private String keywords;
}
