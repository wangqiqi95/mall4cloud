package com.mall4j.cloud.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.coupon.vo.ActivityReportDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 活动报表信息
 * @author shijing
 * @date 2022-03-27
 */
@Data
@ApiModel(description = "活动报表信息")
public class ActivityReportDTO implements Serializable {

    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNo = 1;

    @ApiModelProperty(value = "每页长度",required = true)
    private Integer pageSize = 10;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "优惠券id")
    private String couponInfo;
}
