package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
public class GroupCreateTaskVO {
    /**
     *
     */
    @ApiModelProperty("任务iD")
    private Long id;

    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    private String taskName;

    /**
     * 发送范围
     */
    @ApiModelProperty("发送范围 0: 全体员工 1 部门员工")
    private Integer sendScope;

    /**
     * 入群引导语
     */
    @ApiModelProperty("入群引导语")
    private String slogan;

    /**
     * 发送方式
     */
    @ApiModelProperty("发送方式 0 一个人发群")
    private Integer sendType;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("引流链接")
    private String drainageUrl;

    @ApiModelProperty("引流页面")
    private String drainagePath;

    @ApiModelProperty("引流链接mediaId")
    private String drainageUrlMediaId;

    @ApiModelProperty("是否允许成员在待发送客户列表中重新进行选择：0否/1是")
    private Integer allowSelect;

    @ApiModelProperty("提醒员工次数")
    private Integer warnCount=0;
}
