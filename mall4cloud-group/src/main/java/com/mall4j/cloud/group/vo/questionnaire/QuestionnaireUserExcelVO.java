package com.mall4j.cloud.group.vo.questionnaire;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @date 2023/5/30
 */
@Data
public class QuestionnaireUserExcelVO implements Serializable {

    @ExcelProperty(index = 0, value = "用户名称")
    private String name;

    @ExcelProperty(index = 1, value = "用户手机号")
    private String phone;

    @ExcelProperty(index = 2, value = "用户ID")
    private Long userId;

    @ExcelProperty(index = 3, value = "VIPCODE")
    private String vipcode;
}
