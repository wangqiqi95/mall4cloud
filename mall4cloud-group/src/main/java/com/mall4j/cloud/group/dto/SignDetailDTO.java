package com.mall4j.cloud.group.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "签到详情查询参数")
public class SignDetailDTO extends PageDTO implements Serializable {
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;
    @ApiModelProperty("活动截至时间")
    private Date activityEndTime;
    @ApiModelProperty("用户手机号")
    private String mobile;
    @ApiModelProperty("活动id")
    private Integer activityId;
    @ApiModelProperty("签到类型 1常规签到 2连续签到")
    private Integer signType;
}
