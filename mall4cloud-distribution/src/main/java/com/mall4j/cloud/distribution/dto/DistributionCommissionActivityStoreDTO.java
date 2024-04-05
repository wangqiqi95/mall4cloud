package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 佣金配置-活动佣金-门店DTO
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@Data
public class DistributionCommissionActivityStoreDTO {

	@NotNull(message = "活动ID不能为空")
    @ApiModelProperty("活动ID")
    private Long activityId;

    @ApiModelProperty("门店ID集合")
    private List<Long> storeIdList;

}
