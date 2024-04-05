package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 引流手机号库VO
 *
 * @author gmq
 * @date 2023-10-30 17:13:11
 */
public class CpPhoneLibraryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("系统客户id")
    private Long userId;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("导入来源：0表格导入/1系统用户")
    private Integer importFrom;

    @ApiModelProperty("状态：0待分配/1任务中/2添加成功")
    private Integer status;

    @ApiModelProperty("企微客户id")
    private String externalUserId;

    @ApiModelProperty("")
    private String createBy;

    @ApiModelProperty("")
    private String updateBy;

}
