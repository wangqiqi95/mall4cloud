package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 分销推广-推荐商品DTO
 *
 * @author gww
 * @date 2021-12-24 16:01:22
 */
@Data
public class DistributionRecommendSpuDTO{

    @ApiModelProperty("主键ID")
    private Long id;

	@ApiModelProperty("商品ID集合")
    private List<Long> spuIdList;

	@NotNull(message = "有效期-开始时间不能为空")
    @ApiModelProperty("有效期-开始时间")
    private Date startTime;

	@NotNull(message = "有效期-结束时间不能为空")
    @ApiModelProperty("有效期-结束时间")
    private Date endTime;

	@NotNull(message = "适用门店类型不能为空")
    @ApiModelProperty("适用门店类型 0-所有门店 1-指定门店")
    private Integer limitStoreType;

    @ApiModelProperty("适用门店集合")
    private List<Long> limitStoreIdList;

	@NotNull(message = "状态不能为空")
    @ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

    @ApiModelProperty("权重值")
    private Integer sort;

}
