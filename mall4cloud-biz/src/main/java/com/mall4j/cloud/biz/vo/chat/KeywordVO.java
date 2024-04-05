package com.mall4j.cloud.biz.vo.chat;

import com.mall4j.cloud.biz.model.chat.KeyCustom;
import com.mall4j.cloud.biz.model.chat.KeyWordLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 关键词VO
 *
 */
@Data
public class KeywordVO{
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("敏感词")
    private String sensitives;
    @ApiModelProperty("匹配词")
    private String mate;
    /**
     *适用范围 1:外部客户 2：内部员工
     */
    @ApiModelProperty("适用范围")
    private String suitRange;
    /**
     * 敏感词性质 1:商机 2:产品疑问 3:客户投诉 4:违规
     */
    @ApiModelProperty("敏感词性质")
    private String nature;
    /**
     * 是否打标签 1:是 0:否
     */
    @ApiModelProperty("是否打标签")
    private Integer isLabel;
    @ApiModelProperty("标签")
    private String label;
    /**
     * 是否推荐回复 1:是 0:否
     */
    @ApiModelProperty("是否推荐回复")
    private Integer isRecommend;
    @ApiModelProperty("推荐回复内容")
    private String recommend;
    /**
     * 是否通知提醒1:是 0:否
     */
    @ApiModelProperty("是否通知提醒")
    private Integer isRemind;
    /**
     * 提醒类型 1：当事人 2：提醒指定员工
     */
    @ApiModelProperty("提醒类型")
    private String remindPeople;
    @ApiModelProperty("提醒人")
    private String staff;
    @ApiModelProperty(value = "敏感词性质自定义")
    private KeyCustom customList;
    private Long custom;
    @ApiModelProperty(value = "标签列表")
    private String labelList;
    @ApiModelProperty(value = "话术id")
    private String speechcraftId;
    @ApiModelProperty(value = "素材id")
    private String materialId;
    private String tags;
    @ApiModelProperty("提醒人名称")
    private String staffName;
    @ApiModelProperty("操作人")
    private String createName;
}
