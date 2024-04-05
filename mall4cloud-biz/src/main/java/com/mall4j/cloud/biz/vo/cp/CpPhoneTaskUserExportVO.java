package com.mall4j.cloud.biz.vo.cp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall4j.cloud.common.util.csvExport.ExcelDateConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 引流手机号任务关联客户VO
 *
 * @author gmq
 * @date 2023-10-30 17:13:43
 */
@Data
@ColumnWidth(20)
public class CpPhoneTaskUserExportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "手机号")
    private String phone;

    @ExcelProperty(value = "昵称")
    private String nickName;

    @ExcelProperty(value = "备注")
    private String remarks;

    @ExcelProperty(value = "来源")
    private String importFromName;

    @ExcelProperty(value = "分配日期", converter = ExcelDateConverter.class)
    private Date distributeTime;

    @ExcelProperty(value = "执行员工")
    private String staffName;

    @ExcelProperty(value = "添加状态")
    private String statusName;

}
