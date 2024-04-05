package com.mall4j.cloud.user.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 好友统计DTO
 *
 */
@Data
public class UserCountDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long staffId;

    @ApiModelProperty("员工")
    private String name;

    @ApiModelProperty("好友新增")
    private Integer addUser;

    @ApiModelProperty("好友流失")
    private Integer lossUser;

    @ApiModelProperty("好友删除")
    private Integer delUser;

    @ApiModelProperty("每月新增")
    private Integer monthCount;

    @ApiModelProperty("月份")
    private String mon;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("1男,0女,其余未知")
    private String sex;

    @ApiModelProperty("客户身份 0普通会员，1付费会员")
    private Integer sexCount;

    /**
     * 当前页
     */
    @NotNull(message = "pageNum 不能为空")
    @ApiModelProperty(value = "当前页", required = false)
    private Integer pageNum;

    @NotNull(message = "pageSize 不能为空")
    @ApiModelProperty(value = "每页大小", required = false)
    private Integer pageSize;

    private Integer status;

    private List<Integer> statusList;

    @ApiModelProperty("员工ID列表")
    private List<Long> staffIds;

    @ApiModelProperty("渠道活码分组id集合")
    private List<Long> codeGroupIds;

    @ApiModelProperty("渠道id集合")
    private List<Long> codeChannelIds;

    @ApiModelProperty("")
    private Integer contactChangeType;
}
