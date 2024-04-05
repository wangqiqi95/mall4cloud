package com.mall4j.cloud.biz.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description 触点门店记录导出
 * @Author tan
 * @Date 2023-01-06 13:26
 **/
@Data
public class QrcodeTentacleStoreExportRespDTO {

    @ExcelProperty(value = {"ID"},index = 0)
    private Long id;

    @ExcelProperty(value = {"触点门店Id"},index = 1)
    private String tentacleStoreId;

    @ExcelProperty(value = {"门店编码"},index = 2)
    private String storeCode;

    @ExcelProperty(value = {"门店名称"},index = 3)
    private String storeName;

    @ExcelProperty(value = {"用户uniId"},index = 4)
    private String uniId;

    @ExcelProperty(value = {"用户uniId"},index = 5)
    private String storeId;

    @ExcelProperty(value = {"会员编号"},index = 6)
    private String vipCode;

    @ExcelProperty(value = {"用户昵称"},index = 7)
    private String nickName;

    @ExcelProperty(value = {"查看时间"},index = 8)
    private Date checkTime;

}
