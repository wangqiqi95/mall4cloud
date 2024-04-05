package com.mall4j.cloud.group.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 促销位 活动
 *
 * @author FrozenWatermelon
 * @date 2023-07-18 17:09:16
 */
@Data
@TableName(value = "promotion_column")
public class PromotionColumn implements Serializable{
    private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动开始时间
     */
    private Date activityBeginTime;

    /**
     * 活动结束时间
     */
    private Date activityEndTime;

    /**
     * 是否全部门店 0 否 1是
     */
    private Integer isAllShop;

    /**
     * 状态 0 未启用 1已启用
     */
    private Integer status;

    /**
     * 
     */
    private Integer deleted;

    /**
     * 促销位图片
     */
    private String imgs;

    /**
     * 截止时间
     */
    private Date deadline;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 更新人id
     */
    private Long updateUserId;

    /**
     * 更新人名称
     */
    private String updateUserName;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 更新时间
     */
    protected Date updateTime;

}
