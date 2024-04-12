package com.mall4j.cloud.biz.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Font;


@HeadFontStyle(color = Font.COLOR_NORMAL)
@Getter
@Setter
@EqualsAndHashCode
@ColumnWidth(25)
public class TaskClientImportExcelDTO {

    @ExcelProperty("昵称")
    private String name;

    @ExcelProperty("*手机号")
    private String phone;
}
