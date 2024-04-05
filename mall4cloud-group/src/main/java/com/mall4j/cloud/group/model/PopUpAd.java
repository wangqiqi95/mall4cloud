package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_pop_up_ad")
@ApiModel(value="PopUpAd对象", description="")
public class PopUpAd extends Model<PopUpAd> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "pop_up_ad_id", type = IdType.AUTO)
    private Long popUpAdId;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime activityBeginTime;

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

    private Boolean deleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建用户id")
    private Long createUserId;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新用户id")
    private Long updateUserId;

    @ApiModelProperty(value = "自动关闭时间，0不自动关闭")
    private Integer autoOffSeconds;

    @ApiModelProperty(value = "内容类型：IMAGE图片广告，VIDEO视频，COUPON优惠券，QUESTIONNAIRE问卷")
    private String attachmentType;

    @ApiModelProperty(value = "可见人群规则，1全部，2指定")
    private Integer visibleType;

    @ApiModelProperty(value = "指定可见会员标签ID")
    private Long userTagId;

    @ApiModelProperty(value = "推送类型，1全时段，2指定时段")
    private Integer pushType;

    @ApiModelProperty(value = "EVERY_DAY(每天)，EVERY_WEEK(每周)，EVERY_MONTH(每月)，COUPON（优惠券）")
    private String pushRule;

    @ApiModelProperty(value = "日期标识数组")
    private String ruleTimeTag;


    @Override
    protected Serializable pkVal() {
        return this.popUpAdId;
    }

}
