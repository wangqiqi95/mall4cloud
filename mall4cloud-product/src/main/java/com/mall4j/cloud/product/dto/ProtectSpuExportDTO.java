package com.mall4j.cloud.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.common.util.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

/**
 * 电脑保护价导入参数
 *
 * @gmq
 * @create 2022-06-21 11:55 PM
 **/
@Data
public class ProtectSpuExportDTO {

    @Excel(name = "商品名称")
    @ApiModelProperty("商品名称")
    private String spuName;

    @Excel(name = "商品货号")
    @ApiModelProperty("商品货号")
    private String spuCode;

    @Excel(name = "电商保护价")
    @ApiModelProperty("电商保护价")
    private String protectPrice;

    @Excel(name = "开始时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date startTime;

    @Excel(name = "结束时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private java.util.Date endTime;

}
