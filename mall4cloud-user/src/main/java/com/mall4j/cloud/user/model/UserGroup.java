package com.mall4j.cloud.user.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户分组阶段配置
 *
 * @author gmq
 * @date 2023-11-10 11:01:57
 */
@Data
public class UserGroup extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String createBy;

    /**
     * 0正常/1删除
     */
    private Integer isDelete;

    /**
     * 
     */
    private String updateBy;

    /**
     * 名称
     */
    private String groupName;

    /**
     * 排序
     */
    private Integer weight;

    /**
     * 父级分组id
     */
    private Long parentId;

    /**
     * 0分组/1阶段
     */
    private Integer type;


}
