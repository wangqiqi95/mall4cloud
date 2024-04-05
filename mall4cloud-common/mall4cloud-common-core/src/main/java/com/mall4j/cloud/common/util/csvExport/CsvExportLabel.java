package com.mall4j.cloud.common.util.csvExport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvExportLabel {
    private String         labelName;                             // 列名称
    private String         labelKey;                              // 列取值 key
}
