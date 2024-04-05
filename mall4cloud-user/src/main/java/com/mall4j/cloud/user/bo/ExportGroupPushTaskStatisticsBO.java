package com.mall4j.cloud.user.bo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportGroupPushTaskStatisticsBO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "推送明细";
    public static final String SHEET_NAME = "汇总概括";


    @ExcelProperty(value = {"未完成推送数"},index = 1)
    private Integer notPushCount;
    @ExcelProperty(value = {"已完成推送数"},index = 0)
    private Integer pushIssueFinishCount;
    @ExcelProperty(value = {"未添加好友数"},index = 2)
    private Integer notAddFriend;
    @ExcelIgnore
    private BigDecimal pushRate;
    @ExcelProperty(value = {"推送完成率"},index = 3)
    private String pushRateStr;
    @ExcelProperty(value = {"执行导购总数"},index = 4)
    private Integer staffCount;
    @ExcelProperty(value = {"触达客户总数"},index = 5)
    private Integer issueCount;

}
