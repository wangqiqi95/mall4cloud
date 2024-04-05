package com.mall4j.cloud.api.biz.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 员工收发消息VO
 *
 */
@Data
@ColumnWidth(20)
public class SoldStaffSessionVO {

    /**员工信息**/
    @ExcelProperty(value = {"员工"})
    private String staffName;

    @ColumnWidth(value = 35)
    @ExcelProperty(value = {"部门"})
    private String emp;

    /**好友会话**/
    @ExcelProperty(value = {"好友会话","互动个数"})
    private Integer interactionCount;
    @ExcelProperty(value = {"好友会话","发送消息"})
    private Integer sendCount;
    @ExcelProperty(value = {"好友会话","接收消息"})
    private Integer receiveCount;

    /**客户群聊会话**/
    @ExcelProperty(value = {"客户群聊会话","活跃个数"})
    private Integer activeCont;
    @ExcelProperty(value = {"客户群聊会话","发送消息"})
    private Integer roomSend;
    @ExcelProperty(value = {"客户群聊会话","接收消息"})
    private Integer roomReceive;

    /**消息总数**/
    @ExcelProperty(value = {"消息总数","发送消息"})
    private Integer sendSum;
    @ExcelProperty(value = {"消息总数","接收消息"})
    private Integer receiveSum;

}
