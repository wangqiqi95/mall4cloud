package com.mall4j.cloud.user.bo;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserTagRelationBO {

    @ExcelIgnore
    @ApiModelProperty(value = "标签组ID")
    private Long groupId;

    @ExcelIgnore
    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ExcelIgnore
    @ApiModelProperty(value = "标签与标签组关联ID")
    private Long groupTagRelationId;

    @ExcelProperty("会员卡号")
    @ApiModelProperty(value = "会员")
    private String vipCode;

    @ExcelIgnore
    @ApiModelProperty(value = "创建人（打标人）")
    private Long createUser;
}
