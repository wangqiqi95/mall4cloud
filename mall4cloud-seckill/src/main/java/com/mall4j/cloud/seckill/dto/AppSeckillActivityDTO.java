package com.mall4j.cloud.seckill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("秒杀活动参数")
public class AppSeckillActivityDTO {
    @ApiModelProperty("秒杀活动ID集合")
    private List<Long> seckillIdList;
}
