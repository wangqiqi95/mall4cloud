package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("装修组件拼团查询参数")
public class AppGroupActivityDTO {
    @ApiModelProperty("活动Id集合")
    private List<Long> activityIdList;
}
