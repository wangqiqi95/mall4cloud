package com.mall4j.cloud.biz.vo.cp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
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
public class CpChatScriptUserSoldVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "姓名")
    private String staffName;

    @ExcelProperty(value = "时间", converter = ExcelDateConverter.class)
    private Date createTime;



}
