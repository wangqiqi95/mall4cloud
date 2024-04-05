package com.mall4j.cloud.user.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall4j.cloud.common.util.csvExport.ExcelDateConverter;
import lombok.Data;

import java.util.Date;

@Data
@ColumnWidth(20)
public class SoldGroupPushSonTaskDetailStatisticVO {

    @ExcelProperty(value = "员工")
    private String staffName;

    @ExcelProperty(value = "触达人次")
    private String issueCount;

    @ExcelProperty(value = "是否完成发送")
    private String finishState;

    @ExcelProperty(value = "完成时间", converter = ExcelDateConverter.class)
    private Date finishTime;

}
