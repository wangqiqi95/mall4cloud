package com.mall4j.cloud.biz.model.chat;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会话关键词表
 *
 */
@Data
public class Keyword implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 敏感词
     */
    private String sensitives;
    /**
     * 匹配词
     */
    private String mate;
    /**
     *适用范围 1:外部客户 2：内部员工
     */
    private String suitRange;
    /**
     * 敏感词性质 1:商机 2:产品疑问 3:客户投诉 4:违规
     */
    private String nature;
    /**
     * 是否打标签 1:是 0:否
     */
    private Integer isLabel;
    /**
     * 标签
     */
    private String label;
    /**
     * 是否推荐回复 1:是 0:否
     */
    private Integer isRecommend;
    /**
     *  推荐回复内容
     */
    private String recommend;
    /**
     * 是否通知提醒1:是 0:否
     */
    private Integer isRemind;
    /**
     * 提醒类型 1：当事人 2：提醒指定员工
     */
    private String remindPeople;
    /**
     * 推荐回复话术
     */
    private String speechcraft;
    /**
     * 推荐回复素材
     */
    private String material;
    /**
     * 提醒人
     */
    private String staff;
    /**
     * 敏感词性质自定义
     */
    private Long custom;
    private String speechcraftId;
    private String materialId;
    private String tags;
    private String staffName;
    private String createName;

    private Integer isDelete;

    private Date createTime;
    private Date updateTime;

}
