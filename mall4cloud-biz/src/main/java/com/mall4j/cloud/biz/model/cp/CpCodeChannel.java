package com.mall4j.cloud.biz.model.cp;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 活码渠道标识表
 *
 * @author gmq
 * @date 2023-11-01 10:33:33
 */
@Data
public class CpCodeChannel extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 渠道源标识
     */
    private Integer sourceFrom;

    /**
     * 渠道源 
     */
    private String sourceId;

    /**
     * 渠道群id
     */
    private String baseId;

    /**
     * 生成活码给到企微的参数
     */
    private String sourceState;

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
