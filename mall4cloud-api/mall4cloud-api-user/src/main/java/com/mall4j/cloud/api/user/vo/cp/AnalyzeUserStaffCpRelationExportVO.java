package com.mall4j.cloud.api.user.vo.cp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall4j.cloud.common.util.csvExport.ExcelDateConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ColumnWidth(20)
public class AnalyzeUserStaffCpRelationExportVO {

    @ExcelProperty(value = "昵称")
    private String qiWeiNickName;

    @ExcelProperty(value = "备注")
    private String userRemarks;

    @ExcelProperty(value = "手机号")
    private String userPhone;

    @ExcelProperty(value = "接待成员")
    private String staffName;

    @ExcelProperty(value = "自动通过好友")
    private String autoTypeName;

    @ExcelProperty(value = "添加时间", converter = ExcelDateConverter.class)
    private Date cpCreateTime;

    @ExcelProperty(value = "好友关系状态")
    private String contactChangeTypeName;

}
