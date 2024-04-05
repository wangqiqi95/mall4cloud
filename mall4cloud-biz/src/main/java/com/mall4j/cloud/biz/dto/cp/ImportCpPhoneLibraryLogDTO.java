package com.mall4j.cloud.biz.dto.cp;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ImportCpPhoneLibraryLogDTO implements Serializable {

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("昵称")
    private String nickName;

    @ExcelProperty("备注")
    private String remarks;

    @ExcelProperty("导入日志")
    private String log;

}
