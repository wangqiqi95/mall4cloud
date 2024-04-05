package com.mall4j.cloud.group.dto;


import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;



@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分页查询列表实体")
public class SignActivityPageDTO extends PageDTO implements Serializable {
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动状态 0 未启用 1已启用 2进行中 3 未开始 4已结束")
    private Integer activityStatus;
}
