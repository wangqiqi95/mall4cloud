package com.mall4j.cloud.group.vo.questionnaire;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户未提交记录统计VO
 * @date 2023/5/22
 */
@Data
public class QuestionnaireUserUnSubmitVO implements Serializable {

    @ApiModelProperty("用户ID")
    @ExcelProperty(index = 1, value = "用户ID")
    private Long userId;

    @ApiModelProperty("会员卡号")
    @ExcelProperty(index = 2, value = "会员卡号")
    private String vipcode;

    @ApiModelProperty("手机号 (冗余字段)")
    @ExcelProperty(index = 3, value = "手机号")
    private String phone;

    @ApiModelProperty("用户昵称")
    @ExcelProperty(index = 4, value = "用户昵称")
    private String nickName;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("门店编码")
    @ExcelProperty(index = 5, value = "门店编码")
    private String storeCode;

    @ApiModelProperty("门店名称")
    @ExcelProperty(index = 6, value = "门店名称")
    private String storeName;

    @ApiModelProperty("浏览次数")
    @ExcelProperty(index = 7, value = "浏览次数")
    private Long unSubmitCount;

    @ApiModelProperty("最新未提交时间")
    @ExcelProperty(index = 8, value = "最新未提交时间")
    private Date createTime;


}
