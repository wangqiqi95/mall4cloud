package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 群发任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SendTask extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 推送状态
     */
    private Integer sendStatus;

    /**
     * 总推送量
     */
    private Integer sendTotal;

    /**
     * 标签
     */
    private String tags;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 删除标识
     */
    private Integer flag;


    /**
     * 是否设置tag为空
     */
    private Integer clearTag;

}
