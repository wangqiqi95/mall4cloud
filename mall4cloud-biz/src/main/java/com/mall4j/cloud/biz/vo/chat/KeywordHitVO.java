package com.mall4j.cloud.biz.vo.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 命中关键词VO
 *
 */
@Data
public class KeywordHitVO {
    private String id;
    /**
     * 敏感词
     */
    @ApiModelProperty("敏感词")
    private String sensitives;
    /**
     * 匹配词
     */
    @ApiModelProperty("匹配词")
    private String mate;
    /**
     *触发人
     */
    @ApiModelProperty("触发人")
    private String trigger;

    @ApiModelProperty("触发人")
    private String triggerPhone;
    /**
     * 类型 1:1对1 0:社群
     */
    @ApiModelProperty("类型 1:1对1 0:社群")
    private String type;
    /**
     * 标签
     */
    @ApiModelProperty("标签")
    private String label;
    /**
     * 提醒员工
     */
    @ApiModelProperty("提醒员工")
    private String staff;

    @ApiModelProperty("提醒员工手机号")
    private String staffPhone;
    /**
     * 员工id
     */
    @ApiModelProperty("员工id")
    private String staffId;
    /**
     * 触发人id
     */
    @ApiModelProperty("触发人id")
    private String triggerId;
    /**
     * 触发时间
     */
    @ApiModelProperty("触发时间")
    private Date triggerTime;
    /**
     * 敏感词次数
     */
    @ApiModelProperty("敏感词次数")
    private Integer countSen;

    private Long keyId;
    @ApiModelProperty("命中词")
    private String hitName;
    private String hitTime;
}
