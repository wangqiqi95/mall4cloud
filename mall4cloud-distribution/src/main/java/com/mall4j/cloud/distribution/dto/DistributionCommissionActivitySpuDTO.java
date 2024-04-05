package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 佣金配置-活动佣金-商品DTO
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@Data
public class DistributionCommissionActivitySpuDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("活动ID")
	@NotNull(message = "活动ID不能为空")
    private Long activityId;

    @ApiModelProperty("商品ID集合")
	@NotNull(message = "商品ID集合不能为空")
    private List<Long> spuIdList;

}
