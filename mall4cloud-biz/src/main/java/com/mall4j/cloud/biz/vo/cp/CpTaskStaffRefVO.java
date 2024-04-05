package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 群发任务人工关联表VO
 *
 * @author gmq
 * @date 2023-11-15 16:38:45
 */
@Data
public class CpTaskStaffRefVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("员工企业微信id")
    private String userId;

    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("任务id")
    private Long taskId;

    @ApiModelProperty("0正常/1删除")
    private Integer isDelete;

    @ApiModelProperty("")
    private String createBy;

    @ApiModelProperty("")
    private String updateBy;

    @ApiModelProperty("0未执行/1已执行")
    private Integer status;

    private String msgId;

    @ApiModelProperty("已发送客户总数")
    private Integer sendUserCount;

    @ApiModelProperty("已邀请客户总数")
    private Integer inviteUserCount;

}
