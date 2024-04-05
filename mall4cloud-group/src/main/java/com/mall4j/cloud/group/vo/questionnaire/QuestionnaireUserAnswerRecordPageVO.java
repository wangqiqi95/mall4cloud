package com.mall4j.cloud.group.vo.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class QuestionnaireUserAnswerRecordPageVO {
    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("")
    private String activityId;

    @ApiModelProperty("用户编号")
    private Long userId;

    @ApiModelProperty("关联crm会员id")
    private String vipcode;

    @ApiModelProperty("手机号 (冗余字段)")
    private String phone;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("union_id")
    private String unionId;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("门店编码")
    private String storeCode;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("是否提交 0否1是")
    private Integer submitted;
    @ApiModelProperty("提交时间")
    private Date submittedTime;
    @ApiModelProperty("是否领奖")
    private Integer awarded;
    @ApiModelProperty("领奖时间")
    private Date awardedTime;
    @ApiModelProperty("是否发货")
    private Integer shipped;
    @ApiModelProperty("发货时间")
    private Date shippedTime;

    @ApiModelProperty("是否填写地址")
    private Integer isSetAddr;

    @ApiModelProperty("礼品类型 0积分 1优惠券 2抽奖 3实物")
    private Integer giftType;
    @ApiModelProperty("奖品 积分直接维护赠送的积分值 优惠券维护赠送的优惠券id 抽奖维护抽奖活动id")
    private String giftId;
    @ApiModelProperty("奖品名称")
    private String giftName;
    @ApiModelProperty("奖品图片")
    private String giftPic;
    @ApiModelProperty("抽奖游戏类型")
    private Integer gameType;

    @ApiModelProperty("问卷名称")
    private String name;

    @ApiModelProperty("表单表头")
    private String formNames;
    @ApiModelProperty("答案")
    private String content;

    @ApiModelProperty("表单标题")
    private String title;
}
