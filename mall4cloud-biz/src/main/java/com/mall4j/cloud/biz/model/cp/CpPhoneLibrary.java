package com.mall4j.cloud.biz.model.cp;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 引流手机号库
 *
 * @author gmq
 * @date 2023-10-30 17:13:11
 */
@Data
public class CpPhoneLibrary extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统客户id
     */
    private Long userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 导入来源：0表格导入/1系统用户
     */
    private Integer importFrom;

    /**
     * 状态：0待分配/1任务中/2添加成功
     */
    private Integer status;

    /**
     * 企微客户id
     */
    private String externalUserId;

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

}
