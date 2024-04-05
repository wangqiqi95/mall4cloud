package com.mall4j.cloud.group.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class PopUpAdDataPageVO {

    @ApiModelProperty(value = "主键")
    private Long popUpAdId;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime activityBeginTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "活动终止时间")
    private LocalDateTime activityEndTime;

    @ApiModelProperty(value = "是否全部门店 0否 1是")
    private Integer isAllShop;

    @ApiModelProperty(value = "广告频率类型 0每天一次 1每次打开出现弹窗 2累计仅一次")
    private Integer adFrequency;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "状态 0未启用 1已启用")
    private Boolean status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建用户id")
    private Long createUserId;

    @ApiModelProperty(value = "创建用户名称")
    private String createUserName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新用户id")
    private Long updateUserId;

    @ApiModelProperty(value = "更新用户名称")
    private String updateUserName;

    @ApiModelProperty(value = "自动关闭时间")
    private Integer autoOffSeconds;

    @ApiModelProperty(value = "内容类型：IMAGE图片广告，VIDEO视频，COUPON优惠券，QUESTIONNAIRE问卷")
    private String attachmentType;

    @ApiModelProperty(value = "可见规则，1全部，2指定")
    private Integer visibleType;

    @ApiModelProperty(value = "推送类型，1全时段，2指定时段")
    private Integer pushType;

    @ApiModelProperty(value = "EVERY_DAY(每天)，EVERY_WEEK(每周)，EVERY_MONTH(每月)，COUPON（优惠券）")
    private String pushRule;

    @ApiModelProperty(value = "适用门店数")
    private Integer storeCount;

    @ApiModelProperty(value = "浏览次数")
    private Integer browseCount;

    @ApiModelProperty(value = "浏览人数")
    private Integer browsePeopleCount;

    @ApiModelProperty(value = "点击次数")
    private Integer clickCount;

    @ApiModelProperty(value = "点击人数")
    private Integer clickPeopleCount;
}
