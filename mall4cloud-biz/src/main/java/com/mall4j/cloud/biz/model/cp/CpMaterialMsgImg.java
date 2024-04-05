package com.mall4j.cloud.biz.model.cp;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 *
 * @author gmq
 * @date 2023-12-06 16:14:28
 */
@Data
public class CpMaterialMsgImg extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 素材id
     */
    private Long matId;

    /**
     * 素材消息id
     */
    private Long matMsgId;

    /**
     * 图片地址
     */
    private String image;

    /**
     * 0正常/1删除
     */
    private Integer isDelete;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private String updateBy;

    /**
     * 排序
     */
    private Integer seq;

}
