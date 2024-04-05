package com.mall4j.cloud.biz.vo.chat;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 会话同意VO
 *
 */
@Data
@ColumnWidth(20)
public class SoldSessionAgreeVO {

    @ExcelProperty(value = "昵称")
    private String userName;

    @ExcelProperty(value = "名称")
    private String cpRemark;

    @ExcelProperty(value = "手机号")
    private String phone;

    @ExcelProperty(value = "企业")
    private String corpFullName;

    @ExcelProperty(value = "存档状态")
    private String agreeStatus;
}
