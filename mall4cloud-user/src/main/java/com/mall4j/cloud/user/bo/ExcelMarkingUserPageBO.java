package com.mall4j.cloud.user.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExcelMarkingUserPageBO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "会员名单";
    public static final String SHEET_NAME = "会员名单";

    @ExcelProperty(value = {"会员卡号"},index = 0)
    private String vipCode;

    @ExcelProperty(value = {"会员昵称"},index = 1)
    private String vipNickName;

    @ExcelProperty(value = {"会员手机"},index = 2)
    private String vipPhone;

    @ExcelProperty(value = {"导购编号"},index = 3)
    private String staffNo;

    @ExcelProperty(value = {"导购昵称"},index = 4)
    private String staffNickName;

    @ExcelProperty(value = {"服务门店编号"},index = 5)
    private String serviceStoreCode;

    @ExcelProperty(value = {"服务门店名"},index = 6)
    private String serviceStoreName;

    @ExcelProperty(value = {"标签组分类名"},index = 7)
    private String parentGroupName;

    @ExcelProperty(value = {"标签组名"},index = 8)
    private String groupName;

    @ExcelProperty(value = {"标签名"},index = 9)
    private String tagName;

}
