package com.mall4j.cloud.biz.model.cp;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 引流手机号任务关联员工
 *
 * @author gmq
 * @date 2023-10-30 17:13:37
 */
@Data
public class CpPhoneTaskStaff extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
	@TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 任务id
     */
    private Long taskId;

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
