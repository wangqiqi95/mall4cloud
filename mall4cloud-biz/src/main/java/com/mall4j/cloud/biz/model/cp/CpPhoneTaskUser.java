package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 引流手机号任务关联客户
 *
 * @author gmq
 * @date 2023-10-30 17:13:43
 */
@Data
public class CpPhoneTaskUser extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
	@TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 手机号库id
     */
    private Long phoneUserId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 执行员工id
     */
    private Long staffId;

    /**
     * 状态：0未添加/1添加成功/2添加失败
     */
    private Integer status;

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
     * 分配日期
     */
    private Date distributeTime;

    @TableField(exist = false)
    private Integer libStatus;

}
