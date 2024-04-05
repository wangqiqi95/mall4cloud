package com.mall4j.cloud.biz.dto.cp;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ImportCpPhoneLibraryDTO implements Serializable {

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("昵称")
    private String nickName;

    @ExcelProperty("备注")
    private String remarks;

//    @ExcelIgnore
//    private String log;

}
