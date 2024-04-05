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
public class StoreCodeItemExportRespDTO {

    @ExcelProperty(value = {"ID"},index = 0)
    private Long id;

    @ExcelProperty(value = {"触点名称"},index = 1)
    private String tentacleName;

    @ExcelProperty(value = {"触点链接"},index = 2)
    private String qrcodePath;

    @ExcelProperty(value = {"触点门店编码"},index = 3)
    private String storeCode;

    @ExcelProperty(value = {"触点门店名称"},index = 4)
    private String storeName;

    @ExcelProperty(value = {"用户uniId"},index = 5)
    private String uniId;

    @ExcelProperty(value = {"会员编号"},index = 6)
    private String vipCode;

    @ExcelProperty(value = {"用户昵称"},index = 7)
    private String nickName;

    @ExcelProperty(value = {"查看时间"},index = 8)
    private Date checkTime;

}
