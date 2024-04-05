package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 活动列表信息 *
 * @author shijing
 * @date 2022-01-05
 */
@Data
@ApiModel(description = "活动列表信息")
public class InventoryListVO implements Serializable {

    @ApiModelProperty(value = "券名称")
    private String couponName;

    @ApiModelProperty(value = "调整数量")
    private Integer num;

    @ApiModelProperty(value = "创建人工号")
    private String createCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createName;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp createTime;

}
