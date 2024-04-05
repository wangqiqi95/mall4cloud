package com.mall4j.cloud.biz.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微页面埋点数据表
 */
@Data
@TableName("micro_page_burial_point_record")
public class MicroPageBurialPointRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
	@TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;

    /**
     * 微信unionID
     */
	@TableField("union_id")
    private String unionId;

    /**
     * 微页面装修ID
     */
	@TableField("renovation_id")
    private Long renovationId;

    /**
     * 短链Key
     */
	@TableField("short_key")
    private String shortKey;

    /**
     * 客户昵称
     */
	@TableField("nike_name")
    private String nikeName;

    /**
     * 备注名称
     */
	@TableField("notes_name")
    private String notesName;

    /**
     * 手机号
     */
	@TableField("mobile")
    private String mobile;

    /**
     * 浏览时间
     */
	@TableField("browse_time")
    private Date browseTime;

    /**
     * 浏览时长
     */
	@TableField("browse_duration")
    private Integer browseDuration;

    /**
     * 逻辑删除标识，0：正常，1：删除
     */
	@TableField("is_deleted")
    private Integer isDeleted;
}
