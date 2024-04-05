package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 群发任务人工关联表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@Data
public class CpTaskStaffRef extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 员工企业微信id
     */
    private String userId;

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 0未执行/1已执行
     */
    private Integer status;

    /**
     * 任务id
     */
    private Long taskId;

    private Integer isDelete;

    private String createBy;

    private String updateBy;

    /**
     * 已发送客户总数
     */
    private Integer sendUserCount;

    /**
     * 已邀请客户总数
     */
    private Integer inviteUserCount;

    private String msgId;

    /**
     * 创建企业群发
     * 无效或无法发送的external_userid或chatid列表
     */
    private String failList;

    /**
     * 创建企业群发
     * 提醒员工次数
     */
    private Integer warnCount=0;

    /**
     * 最近一次提醒时间
     */
    private Date warnTime;

}
