package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签建群任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CpGroupCreateTask extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 发送范围: 0按部门员工/1按客户标签/2按客户分组
     */
    private Integer sendScope;

    /**
     * 入群引导语
     */
    private String slogan;

    /**
     * 群活码id
     */
    private Long codeId;

    /**
     * 发送方式
     */
    private Integer sendType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 
     */
    private String createName;

    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 发送状态:0带发送/1已发送
     */
    private Integer sendStatus;
    /**
     * 是否完成:0否/1是
     */
    private Integer isReplay;


    /**
     * 拉群方式：0企微群活码/1自建群活码
     */
    private Integer groupType;

    private Integer isDelete;

    /**
     * 引流链接
     */
    private String drainageUrl;
    /**
     * 引流页面
     */
    private String drainagePath;

    /**
     * 创建企业群发
     * 提醒员工次数
     */
    private Integer warnCount=0;
    /**
     * 最近一次提醒时间
     */
    private Date warnTime;
    /**
     * 创建企业群发
     * 无效或无法发送的external_userid或chatid列表
     */
    private String failList;
    /**
     * 创建企业群发
     * 企业群发消息的id
     */
    private String msgId;

    /**
     * 邀请人数
     */
    private Integer InviteCount;
    /**
     * 入群人数
     */
    private Integer joinGroupCount;
    /**
     * 完成发送员工数
     */
    private Integer sendStaffCount;

    /**
     * 客户总人数
     */
    private Integer userCount;

    /**
     * 员工总人数
     */
    private Integer staffCount;

    /**
     * 客群总数
     */
    private Integer groupCount;

    /**
     * 渠道唯一标识
     */
    private String state;

    /**
     * 是否允许成员在待发送客户列表中重新进行选择：0否/1是
     */
    private Integer allowSelect;

}
