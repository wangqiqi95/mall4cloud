package com.mall4j.cloud.biz.dto.channels.league;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 达人返回列表
 * @Author axin
 * @Date 2023-02-13 16:38
 **/
@Data
public class PromoterListExportRespDto {

    @ExcelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    @NumberFormat(value = "#")
    private Long id;

    @ExcelProperty(value = "视频号id")
    private String finderId;

    @ExcelProperty(value = "视频号名称")
    private String finderName;

    @ExcelProperty(value = "门店id")
    @JsonSerialize(using = ToStringSerializer.class)
    @NumberFormat(value = "#")
    private Long storeId;

    @ExcelProperty(value = "门店编码")
    private String storeCode;

    @ExcelProperty(value = "门店名称")
    private String storeName;

    @ExcelIgnore
    private Integer status;

    @ExcelProperty(value = "合作状态")
    private String statusName;


}
