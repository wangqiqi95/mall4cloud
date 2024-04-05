package com.mall4j.cloud.biz.dto.chat;

import com.mall4j.cloud.biz.model.chat.KeyCustom;
import com.mall4j.cloud.biz.model.chat.KeyWordLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 会话关键词DTO
 */
@Data
public class KeywordDTO {

    private String id;
    @ApiModelProperty(value = "敏感词")
    private String sensitives;
    @ApiModelProperty(value = "匹配词")
    private String mate;
    @ApiModelProperty(value = "适用范围 1:外部客户 2：内部员工")
    private String suitRange;
    @ApiModelProperty(value = "敏感词性质 1:商机 2:产品疑问 3:客户投诉 4:违规")
    private String nature;
    @ApiModelProperty(value = "是否打标签 1:是 0:否")
    private Integer isLabel;
    @ApiModelProperty(value = "标签id")
    private String label;
    @ApiModelProperty(value = "是否推荐回复 1:是 0:否")
    private Integer isRecommend;
    @ApiModelProperty(value = "推荐回复内容")
    private String recommend;
    @ApiModelProperty(value = "是否通知提醒1:是 0:否")
    private Integer isRemind;
    @ApiModelProperty(value = "提醒类型 1：当事人 2：提醒指定员工")
    private String remindPeople;
    @ApiModelProperty(value = "提醒人")
    private String staff;
    @ApiModelProperty(value = "推荐回复话术")
    private String speechcraft;
    @ApiModelProperty(value = "推荐回复素材")
    private String material;
    @ApiModelProperty(value = "敏感词性质自定义")
    private KeyCustom customList;
    @ApiModelProperty(value = "标签列表")
    private String labelList;
    @ApiModelProperty(value = "话术id")
    private String speechcraftId;
    @ApiModelProperty(value = "素材id")
    private String materialId;
    @ApiModelProperty("提醒人名称")
    private String staffName;


}
