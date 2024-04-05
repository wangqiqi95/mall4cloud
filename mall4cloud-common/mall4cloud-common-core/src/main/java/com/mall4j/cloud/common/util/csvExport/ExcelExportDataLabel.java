package com.mall4j.cloud.common.util.csvExport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelExportDataLabel {
    private String         labelName;                             // 列名称

    private String         labelKey;                              // 列取值 key

    private ExcelExportConstant.LabelValueType labelValueType = ExcelExportConstant.LabelValueType.STRING;// 列值类型


    public ExcelExportDataLabel(String labelName, String labelKey) {
            this.labelKey=labelKey;
            this.labelName=labelName;
        }

}
