package com.mall4j.cloud.biz.vo.chat;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall4j.cloud.common.util.csvExport.ExcelDateConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 超时记录VO
 *
 */
@Data
@ColumnWidth(20)
public class SoldTimeOutLogVO {

    @ExcelProperty(value = "触发时间", converter = ExcelDateConverter.class)
    private Date triggerTime;

    @ExcelProperty(value = "超时员工")
    private String staffName;

    @ExcelProperty(value = "对接客户")
    private String userName;
}
