package com.mall4j.cloud.biz.vo.cp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableField;
import com.mall4j.cloud.common.util.csvExport.ExcelDateConverter;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分页获取素材浏览列表
 *
 * @author gmq
 * @date 2023-10-30 17:13:43
 */
@Data
@ColumnWidth(20)
public class CpMaterialUseRecordExportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "使用时间", converter = ExcelDateConverter.class)
    private Date createTime;

    @ExcelProperty(value = "使用员工")
    private String staffName;

}
