package com.mall4j.cloud.seckill.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillStore {

    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("秒杀活动id")
    private Long seckillId;
    @ApiModelProperty("门店id")
    private Long storeId;

}
