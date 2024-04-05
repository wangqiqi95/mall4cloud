package com.mall4j.cloud.biz.dto.channels.league;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 优选联盟推广计划
 * @Author axin
 * @Date 2023-02-20 15:56
 **/
@Data
public class ItemPlanListPageReqDto extends PageDTO {
    @ApiModelProperty(value = "计划名称")
    private String name;

    @ApiModelProperty(value = "推广类型 1普通推广 2定向推广 3专属推广")
    private Integer type;
}
