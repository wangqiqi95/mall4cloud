package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 分销推广-推荐商品VO
 *
 * @author gww
 * @date 2021-12-24 16:01:22
 */
@Data
public class DistributionRecommendSpuVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("有效期-开始时间")
    private Date startTime;

    @ApiModelProperty("有效期-结束时间")
    private Date endTime;

    @ApiModelProperty("适用门店类型 0-所有门店 1-指定门店")
    private Integer limitStoreType;

    @ApiModelProperty("适用门店集合(逗号隔开)")
    private String limitStoreIds;

    @ApiModelProperty("适用门店数量")
    private int limitStoreCount = 0;

    private List<String> limitStoreIdList;

    @ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

    @ApiModelProperty("商品ID")
    private Long spuId;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("商品编码")
    private String spuCode;

    @ApiModelProperty("商品介绍主图")
    private String mainImgUrl;

    @ApiModelProperty(value = "商品售价")
    private Long priceFee;

    @ApiModelProperty(value = "市场价，整数方式保存")
    private Long marketPriceFee;

    @ApiModelProperty("导购佣金")
    private BigDecimal guideRate;

    @ApiModelProperty("微客佣金")
    private BigDecimal shareRate;
}
