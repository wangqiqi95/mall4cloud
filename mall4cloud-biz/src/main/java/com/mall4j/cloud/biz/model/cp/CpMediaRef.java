package com.mall4j.cloud.biz.model.cp;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author gmq
 * @date 2023-11-27 11:29:05
 */
@Data
@Builder
public class CpMediaRef implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String url;

    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private String mediaId;

    /**
     * 
     */
    private String thumbMediaId;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private String updateBy;

    /**
     * 
     */
    private Integer isDelete;

    /**
     * 
     */
    private String sourceId;

    /**
     * 
     */
    private Integer sourceFrom;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
