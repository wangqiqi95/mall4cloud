package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 活动报表信息
 * @author shijing
 * @date 2022-03-27
 */
@Data
@ApiModel(description = "活动报表信息")
public class ActivityReportVO implements Serializable {

    @ApiModelProperty(value = "活动id")
    private Long id;

    @ApiModelProperty(value = "活动名称")
    private String title;

    @ApiModelProperty(value = "活动开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "总领券数")
    private Integer receiveSum;

    @ApiModelProperty(value = "总核销数")
    private Integer writeOffSum;

    @ApiModelProperty(value = "活动收入")
    private BigDecimal activityIncome;

    @ApiModelProperty(value = "券过期数")
    private Integer overdueSum;

    @ApiModelProperty(value = "券活动概况")
    private List<ActivityReportDetailVO> activityReportDetail;

}
