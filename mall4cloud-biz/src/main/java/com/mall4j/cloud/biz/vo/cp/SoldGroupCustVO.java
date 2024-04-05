package com.mall4j.cloud.biz.vo.cp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall4j.cloud.common.util.csvExport.ExcelDateConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 命中关键词VO
 *
 */
@Data
@ColumnWidth(20)
public class SoldGroupCustVO {

    @ExcelProperty(value = "群名称")
    private String groupName;

    @ExcelProperty(value = "群主")
    private String ownerName;

    @ExcelProperty(value = "群人数")
    private String total;

    @ExcelProperty(value = "群创建时间",converter = ExcelDateConverter.class)
    private Date createTime;
}
