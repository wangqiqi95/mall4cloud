package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 分销推广-发展奖励门店DTO
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:38:24
 */
@Data
public class DistributionDevelopingRewardStoreDTO{

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("发展奖励活动ID")
    private Long developingRewardId;

    @ApiModelProperty("门店ID")
    private Long storeId;

}
