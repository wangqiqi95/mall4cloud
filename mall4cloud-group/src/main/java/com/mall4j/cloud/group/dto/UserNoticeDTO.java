package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户签到提醒实体")
public class UserNoticeDTO implements Serializable {
    private Integer activityId;
    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;
    private String openId;
    private String formId;
}
