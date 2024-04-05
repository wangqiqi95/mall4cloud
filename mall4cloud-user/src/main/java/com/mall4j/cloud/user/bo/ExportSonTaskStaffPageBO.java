package com.mall4j.cloud.user.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportSonTaskStaffPageBO {
    public static final String SHEET_NAME = "推送任务导购明细";

    @ExcelProperty(value = {"导购ID"},index = 0)
    private Long staffId;
    @ExcelProperty(value = {"导购编号"},index = 1)
    private String staffNo;
    @ExcelProperty(value = {"导购昵称"},index = 2)
    private String staffNickName;
    @ExcelProperty(value = {"导购所属门店ID"},index = 3)
    private String staffStoreCode;
    @ExcelProperty(value = {"导购所属门店名称"},index = 4)
    private String staffStoreName;
    @ExcelProperty(value = {"触达任务总数"},index = 5)
    private Integer pushIssueCount;
    @ExcelProperty(value = {"触达任务完成数"},index = 6)
    private Integer pushIssueFinishCount;
    @ExcelProperty(value = {"是否完成触达"},index = 7)
    private String finalSendRemark;
    @ExcelProperty(value = {"触达完成时间"},index = 8)
    private LocalDateTime finishTime;
    @ExcelProperty(value = {"未加好友数"},index = 9)
    private Integer notAddFriendCount;
}
