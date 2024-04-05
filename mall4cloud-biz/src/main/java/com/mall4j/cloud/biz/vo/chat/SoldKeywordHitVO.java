package com.mall4j.cloud.biz.vo.chat;

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
public class SoldKeywordHitVO {

    @ExcelProperty(value = "类型")
    private String type;

    @ExcelProperty(value = "触发人")
    private String trigger;

    @ExcelProperty(value = "敏感词")
    private String sensitives;

    @ExcelProperty(value = "匹配词")
    private String mate;

    @ExcelProperty(value = "触发时间", converter = ExcelDateConverter.class)
    private Date triggerTime;

    @ExcelProperty(value = "提醒员工")
    private String staff;

}
