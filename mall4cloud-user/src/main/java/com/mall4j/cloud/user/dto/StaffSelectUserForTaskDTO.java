package com.mall4j.cloud.user.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 导购端-会员-所有会员筛选条件查询入参
 * @author Peter_Tan
 * @date 2023/02/08
 */
@Data
public class StaffSelectUserForTaskDTO extends PageDTO {

    /**
     * 主任务ID
     */
    @ApiModelProperty("主任务ID")
    private Long taskId;

    /**
     * 子任务ID
     */
    @ApiModelProperty("子任务ID")
    private Long sonTaskId;

    /**
     * 会员企业微信ID
     */
    @ApiModelProperty("会员企业微信ID")
    private String vipCpUserId;

    /**
     * 触达状态 0-未触达 1-已触达
     */
    @ApiModelProperty("触达状态 0-未触达 1-已触达")
    private Integer reachStatus;

    /**
     * 是否加好友 0-否 1-是
     */
    @ApiModelProperty("是否加好友 0-否 1-是")
    private Integer addWechat;

    /**
     * 会员标签筛选条件
     */
    /*@ApiModelProperty("会员标签筛选条件")
    private List<Long> tagId;*/

    /**
     * 导购ID
     */
    private Long staffId;

}
