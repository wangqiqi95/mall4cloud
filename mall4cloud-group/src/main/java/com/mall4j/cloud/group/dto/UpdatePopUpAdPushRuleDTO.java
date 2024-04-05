package com.mall4j.cloud.group.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class UpdatePopUpAdPushRuleDTO {

    @ApiModelProperty(value = "EVERY_DAY(每天)，EVERY_WEEK(每周)，EVERY_MONTH(每月)，COUPON(优惠券)")
    private String popUpAdPushRule;

//    @ApiModelProperty(value = "开屏广告ID")
//    private Long openScreenAdId;openScreenAdId

    @ApiModelProperty(value = "主键")
    private Long popUpAdRuleId;

    @ApiModelProperty(value = "日期标识数组")
    private String ruleTimeTag;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;

}
