package com.mall4j.cloud.group.bo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportPopUpAdOperateBO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "用户操作记录报表";
    public static final String SHEET_NAME = "用户操作记录概览";

    @ExcelProperty(value = {"开屏广告ID"},index = 0)
    private Long popUpAdId;

    @ExcelProperty(value = {"开屏广告名称"},index = 1)
    private String popUpAdName;

//    @ApiModelProperty(value = "用户ID")
//    private Long vipUserId;

    @ExcelProperty(value = {"用户卡号"},index = 2)
    private String vipCode;

    @ExcelProperty(value = {"用户昵称"},index = 3)
    private String vipNickName;

    @ExcelProperty(value = {"微信union_id"},index = 4)
    private String unionId;

    @ExcelIgnore
    @ApiModelProperty(value = "操作方式")
    private Integer operate;

    @ExcelProperty(value = {"操作方式"},index = 5)
    private String operateStr;

    @ExcelIgnore
    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ExcelProperty(value = {"门店code"},index = 6)
    private String storeCode;

    @ExcelProperty(value = {"门店名称"},index = 7)
    private String storeName;

    @ExcelProperty(value = {"操作页面url"},index = 8)
    private String entrance;

//    @ExcelProperty(value = {"行为时间"},index = 9,converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;



//    活动id，活动名称，行为（浏览/点击），访问门店Code，访问门店名称，会员名称，unionid，会员号，行为时间；

}
