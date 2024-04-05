package com.mall4j.cloud.biz.dto.channels.league;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
public class ItemPlanListExportRespDto {
    @ExcelProperty(value = "计划id",index = 0)
    private Long id;

    @ExcelProperty(value = "计划名称", index = 1)
    private String name;

//    @ExcelProperty(value = "达人ID列表")
//    private List<String> finderIds;

//    @ExcelProperty(value = "商品ID列表")
//    private List<String> productIds;

    //@ExcelProperty(value = "推广类型 1普通推广 2定向推广 3专属推广")
    @ExcelIgnore
    private Integer type;

    @ExcelProperty(value = "推广类型",index = 2)
    private String typeName;

    @ExcelProperty(value = "计划开始时间",index = 3)
    private Date beginTime;

    @ExcelProperty(value = "计划结束时间",index = 4)
    private Date endTime;

    @ExcelProperty(value = "创建人id",index = 5)
    private Long createPerson;

    @ExcelProperty(value = "创建人姓名",index = 6)
    private Long createPersonName;

    @ExcelProperty(value = "创建时间",index = 7)
    private Date createTime;
}
