package com.mall4j.cloud.coupon.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.mall4j.cloud.common.util.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExportUserToCreateEventBO {

    @ExcelProperty(value = {"手机号"},index = 0)
    private String mobile;

}
