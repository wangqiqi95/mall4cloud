package com.mall4j.cloud.platform.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.mall4j.cloud.common.model.ExcelModel;
import lombok.Data;

/**
 * @author FrozenWatermelon
 * @date 2022-01-25 18:13:49
 */
@Data
public class ImportStaffsDto extends ExcelModel {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "员工信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 1;

    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"员工工号"}, index = 0)
    private String staffNo;

    @ExcelProperty(value = {"员工名称"}, index = 1)
    private String staffName;

    @ExcelProperty(value = {"手机号"}, index = 2)
    private String mobile;

    @ExcelProperty(value = {"所属门店"}, index = 3)
    private String storeName;

    @ExcelProperty(value = {"员工身份"}, index = 4)
    private String roleType;

    @ExcelProperty(value = {"状态"}, index = 5)
    private String status;

    @ExcelProperty(value = {"职位"}, index = 6)
    private String position;

    @ExcelProperty(value = {"员工邮箱"}, index = 7)
    private String email;

}
