package com.mall4j.cloud.biz.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@ColumnWidth(25)
public class TaskClientImportErrorExcelDTO extends TaskClientImportExcelDTO {
    @ExcelProperty("错误信息")
    private String errorMsg;
}