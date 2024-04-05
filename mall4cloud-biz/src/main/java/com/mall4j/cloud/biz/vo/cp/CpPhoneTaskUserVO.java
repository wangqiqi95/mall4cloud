package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 引流手机号任务关联客户VO
 *
 * @author gmq
 * @date 2023-10-30 17:13:43
 */
@Data
public class CpPhoneTaskUserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("手机号库id")
    private Long phoneUserId;

    @ApiModelProperty("任务id")
    private Long taskId;

    @ApiModelProperty("执行员工id")
    private Long staffId;

    @ApiModelProperty("执行员工姓名")
    private String staffName;

    @ApiModelProperty("状态：0未添加/1添加成功/2添加失败")
    private Integer status;
    private String statusName;

    @ApiModelProperty("")
    private String createBy;

    @ApiModelProperty("")
    private String updateBy;

    private Integer userStatus;

    @ApiModelProperty("分配日期")
    private Date distributeTime;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("导入来源：0表格导入/1系统用户")
    private Integer importFrom;
    private String importFromName;

}
