package com.mall4j.cloud.group.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 开屏广告规则表
 * </p>
 *
 * @author ben
 * @since 2023-04-19
 */
@Data
public class PopUpAdRuleVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long popUpAdRuleId;

//    @ApiModelProperty(value = "EVERY_DAY(每天)，EVERY_WEEK(每周)，EVERY_MONTH(每月)")
//    private String popUpAdPushRule;

    @ApiModelProperty(value = "开屏广告ID")
    private Long popUpAdId;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "开始时间")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人ID")
    private Long createUser;

    @ApiModelProperty(value = "修改人ID")
    private Long updateUser;
}
