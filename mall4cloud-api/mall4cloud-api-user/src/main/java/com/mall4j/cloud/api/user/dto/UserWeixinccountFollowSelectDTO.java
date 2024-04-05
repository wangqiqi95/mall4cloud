package com.mall4j.cloud.api.user.dto;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @create 2022-04-01 4:07 PM
 **/
@Data
public class UserWeixinccountFollowSelectDTO {

    @NotNull(message = "公众号原始id not null")
    @ApiModelProperty(value = "公众号原始id",required = true)
    private String appId;

    @ApiModelProperty(value ="状态 1:关注 2:取消关注",required = false)
    private Integer status;

    @ApiModelProperty("开始时间 yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String startTime;

    @ApiModelProperty("结束时间 yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String endTime;
}
