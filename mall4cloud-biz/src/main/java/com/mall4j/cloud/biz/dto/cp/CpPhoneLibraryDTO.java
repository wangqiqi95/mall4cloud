package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 引流手机号库DTO
 *
 * @author gmq
 * @date 2023-10-30 17:13:11
 */
@Data
public class CpPhoneLibraryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("系统客户id")
    private Long userId;

    @ApiModelProperty("手机号")
    private String phone;

    private List<String> phones;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("导入来源：0表格导入/1系统用户")
    private Integer importFrom;

    @ApiModelProperty("状态：0待分配/1任务中/2添加成功")
    private Integer status;

    @ApiModelProperty("状态：0待分配/1任务中/2添加成功")
    private List<Integer> statusList;

    @ApiModelProperty("企微客户id")
    private String externalUserId;

    private Date startTime;
    private Date endTime;
}
