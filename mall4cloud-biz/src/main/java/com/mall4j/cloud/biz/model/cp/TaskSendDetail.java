package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;

import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.wx.cp.constant.SendStatus;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 群发任务明细表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskSendDetail extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
    public TaskSendDetail(){}
    public TaskSendDetail(TaskPush taskPush, StaffVO staff ){
        this.pushId = taskPush.getId();
        this.updateTime= taskPush.getCreateTime();
        this.createTime=taskPush.getCreateTime();
        this.sendStatus=SendStatus.WAIT.getCode();
        this.isRelay = 0;
        this.staffId=staff.getId();
        this.staffName = staff.getStaffName();
        this.userId = staff.getQiWeiUserId();
        this.storeId = staff.getStoreId();
        this.storeName = staff.getStoreName();
    }
    /**
     * 
     */
    private Long id;

    /**
     * pushId
     */
    private Long pushId;
    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 员工姓名
     */
    private String staffName;
    /**
     * 员工企业微信id
     */
    private String userId;

    /**
     * 所属店id
     */
    private Long storeId;

    /**
     * 所属店名称
     */
    private String storeName;

    /**
     * 是否转发
     */
    private Integer isRelay;

    /**
     * 完成时间
     */
    private Date completeTime;

    /**
     * 是否完成发送
     */
    private Integer sendStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
