package com.mall4j.cloud.api.biz.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall4j.cloud.common.util.csvExport.ExcelDateConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 员工收发消息VO
 *
 */
@Data
@ColumnWidth(25)
public class SoldSessionFileVO {

    @ExcelProperty(value = {"消息时间"},converter = ExcelDateConverter.class)
    private Date msgtime;

    @ExcelProperty(value = {"发送人UserId"})
    private String from;

    @ExcelProperty(value = {"发送人昵称/姓名"})
    private String fromName;

    @ExcelProperty(value = {"接收人UserIds"})
    private String tolist;

    @ExcelProperty(value = {"接收人昵称/姓名/群名"})
    private String tolistName;

    @ExcelProperty(value = {"发送内容"})
    private String content;

    @ExcelProperty(value = {"消息类型"})
    private String msgtype;

    @ExcelProperty(value = {"附件ID"})
    private String mediaId;

}
