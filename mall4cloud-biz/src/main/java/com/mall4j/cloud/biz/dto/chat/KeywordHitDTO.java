package com.mall4j.cloud.biz.dto.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 命中关键词DTO
 */
@Data
public class KeywordHitDTO {

    private String id;
    @ApiModelProperty(value = "敏感词")
    private String sensitives;
    @ApiModelProperty(value = "匹配词")
    private String mate;
    @ApiModelProperty(value = "触发人")
    private String trigger;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "员工")
    private String staff;
    @ApiModelProperty(value = "top")
    private Integer top;
    private String label;
    @ApiModelProperty(value = "敏感词id")
    private Long keyId;

    private List<String> triggers;
}
