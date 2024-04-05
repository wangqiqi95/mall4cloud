package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 自动拉群员工好友关系表
 *
 * @author gmq
 * @date 2023-11-08 10:20:46
 */
@Data
public class CpAutoGroupCodeUser extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 企微用户id
     */
    private String qiWeiUserId;

    /**
     * 
     */
    private Long staffId;

    /**
     * 客户昵称
     */
    private String nickName;

    /**
     * 
     */
    private Long codeId;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private String updateBy;

    /**
     * 0正常/1删除
     */
    private Integer isDelete;

    /**
     * 好友状态： 1-绑定 2-解绑中 3-删除客户
     */
    private Integer status;

    private String state;

    /**
     * 0未发送/1已发送/3发送失败
     */
    private Integer sendStatus;

    /**
     * 发送欢迎语备注
     */
    private String sendStatusRemark;

    /**
     * 是否入群：0否/1是
     */
    private Integer joinGroup;

    /**
     * 企微群id
     */
    private String chatId;

    /**
     * 系统群id
     */
    private Long groupId;

    /**
     * 加好友时间
     */
    private Date bindTime;

    /**
     * 入群时间
     */
    private Date joinGroupTime;

}
