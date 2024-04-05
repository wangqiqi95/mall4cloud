package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 自动拉群与群活码关联表
 *
 * @author gmq
 * @date 2023-11-06 14:47:45
 */
@Data
public class CpGroupCodeTool implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 自动拉群表id
     */
    private Long relGroupId;

    /**
     * 群活码id
     */
    private Long groupId;

    /**
     * 群活码源：0群活码/1自动拉群-群活码/2标签建群-群活码
     */
    private Integer codeFrom;

    @TableField(exist = false)
    private String key;

}
