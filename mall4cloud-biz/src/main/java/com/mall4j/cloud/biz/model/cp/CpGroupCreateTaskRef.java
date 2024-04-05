package com.mall4j.cloud.biz.model.cp;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 *
 * @author gmq
 * @date 2024-01-19 15:49:54
 */
@Data
public class CpGroupCreateTaskRef implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String groupId;

    private Integer scope;

    /**
     * 
     */
    private String tagId;

}
