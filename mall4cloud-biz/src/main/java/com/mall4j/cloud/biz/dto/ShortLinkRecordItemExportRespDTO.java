package com.mall4j.cloud.biz.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description 短链记录明细导出报表
 * @Author tan
 * @Date 2022-12-29 10:54
 **/
@Data
public class ShortLinkRecordItemExportRespDTO {

    @ExcelProperty(value = {"ID"},index = 0)
    private Long id;

    @ExcelProperty(value = {"短链ID"},index = 1)
    private String shortLinkId;

    @ExcelProperty(value = {"短链链接路径"},index = 2)
    private String shortLinkUrl;

    @ExcelProperty(value = {"用户uniId"},index = 3)
    private String uniId;

    @ExcelProperty(value = {"会员编号"},index = 4)
    private String vipCode;

    @ExcelProperty(value = {"用户昵称"},index = 5)
    private String nickName;

    @ExcelProperty(value = {"服务门店"},index = 6)
    private String staffStoreName;

    @ExcelProperty(value = {"服务门店Code"},index = 7)
    private String staffStoreCode;

    @ExcelProperty(value = {"查看时间"},index = 8)
    private Date checkTime;

}
