package com.mall4j.cloud.user.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportStaffSendRecordBO {

    public static final String SHEET_NAME = "推送任务触达明细";

    @ExcelProperty(value = {"主任务ID"},index = 0)
    private Long pushTaskId;
    @ExcelProperty(value = {"主任务名称"},index = 1)
    private String pushTaskName;
    @ExcelProperty(value = {"子任务ID"},index = 2)
    private Long groupPushSonTaskId;
    @ExcelProperty(value = {"子任务名称"},index = 3)
    private String sonTaskName;
    @ExcelProperty(value = {"导购编号"},index = 4)
    private String staffNo;
    @ExcelProperty(value = {"导购名称"},index = 5)
    private String staffNickName;
    @ExcelProperty(value = {"服务门店编号"},index = 6)
    private String staffStoreCode;
    @ExcelProperty(value = {"服务门店名称"},index = 7)
    private String staffStoreName;
    @ExcelProperty(value = {"会员卡号"},index = 8)
    private String vipCode;
    @ExcelProperty(value = {"触达状态"},index = 9)
    private String sendRemark;
    @ExcelProperty(value = {"触达时间"},index = 10)
    private LocalDateTime finishTime;
    @ExcelProperty(value = {"触达类型"},index = 11)
    private String sendModelRemark;
    @ExcelProperty(value = {"是否添加"},index = 12)
    private String addRemark;

}
