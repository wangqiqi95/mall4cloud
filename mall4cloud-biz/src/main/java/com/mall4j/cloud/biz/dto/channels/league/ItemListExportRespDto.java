package com.mall4j.cloud.biz.dto.channels.league;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author axin
 * @Date 2023-02-27 14:16
 **/
@Data
public class ItemListExportRespDto {
    @ExcelProperty(value = "id",index = 0)
    @JsonSerialize(using = ToStringSerializer.class)
    @NumberFormat(value = "#")
    private Long id;

    @ExcelProperty(value = "商品名称", index = 1)
    private String title;

//    @ExcelProperty(value = "商品ID列表")
//    private List<String> productIds;

    //@ExcelProperty(value = "推广类型 1普通推广 2定向推广 3专属推广")
    @ExcelIgnore
    private Integer type;

    @ExcelProperty(value = "推广类型",index = 2)
    private String typeName;

    @ExcelProperty(value = "推广开始时间",index = 3)
    private Date beginTime;

    @ExcelProperty(value = "推广结束时间",index = 4)
    private Date endTime;

    @ExcelProperty(value = "分佣比例",index = 5)
    private Integer ratio;

    @ExcelProperty(value = "创建人",index = 6)
    private String createPerson;

    @ExcelProperty(value = "创建时间",index = 7)
    private Date createTime;

    @ExcelProperty(value = "优选联盟状态",index = 8)
    private String statusName;

    @ExcelIgnore
    private Integer status;

    @ExcelProperty(value = "达人",index = 9)
    private String finderNames;

    @ApiModelProperty(value = "达人列表")
    @ExcelIgnore
    private List<ItemListFinderRespDto> finder;
}
