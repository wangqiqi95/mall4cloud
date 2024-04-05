package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.biz.wx.cp.constant.TaskType;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群发任务客户关联表
 *
 * @author gmq
 * @date 2023-10-30 14:23:04
 */
@Data
public class CpTaskUserRef extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     *
     */
    private String groupName;

    /**
     *
     */
    private Long groupId;

    /**
     *
     */
    private String chatId;

    /**
     * 是否入群：0否/1是
     */
    private Integer joinGroup;

    /**
     * 客户id
     */
    private Long userId;

    /**
     * 客户姓名
     */
    private String userName;

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 0正常/1删除
     */
    private Integer isDelete;

    /**
     *
     */
    private Integer type;

    /**
     *
     */
    private String createBy;

    /**
     *
     */
    private String updateBy;

    /**
     * 0未执行/1已执行
     */
    private Integer status;

    /**
     * 是否触达：0/否/1是
     */
    private Integer reachStatus;

    /**
     * 企微执行状态
     */
    private Integer cpSendStatus;

    /**
     *
     */
    private String qiWeiUserId;

    /**
     *
     */
    private String saffQiWeiUserId;

    /**
     * 发送时间
     */
    private Date sendTime;

    private String state;

    public void init(){
        this.setStatus(0);
        this.setCpSendStatus(0);
        this.setReachStatus(0);
        this.setJoinGroup(0);
        this.setType(TaskType.GROUP_TAG.getCode());
        this.setCreateTime(new Date());
        this.setIsDelete(0);
    }
}
