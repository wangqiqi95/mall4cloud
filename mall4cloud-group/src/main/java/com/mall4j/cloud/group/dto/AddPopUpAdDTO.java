package com.mall4j.cloud.group.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.group.vo.PopUpAdAttachmentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AddPopUpAdDTO {

    @NotBlank(message = "名称不能为必填项")
    @ApiModelProperty(value = "广告名称")
    private String activityName;

    @NotNull(message = "开始时间为必传项")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime activityBeginTime;

    @NotNull(message = "结束时间为必传项")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动终止时间")
    private LocalDateTime activityEndTime;

//    @NotNull(message = "可用门店标识为必传项")
    @ApiModelProperty(value = "是否全部门店 0否 1是")
    private Integer isAllShop;

    @ApiModelProperty(value = "广告频率类型 0每天一次 1每次打开出现弹窗 2累计仅一次")
    private Integer adFrequency;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "状态 0未启用 1已启用")
    private Boolean status;

    @ApiModelProperty(value = "自动关闭时间")
    private Integer autoOffSeconds;

    @ApiModelProperty(value = "内容类型：IMAGE图片广告，VIDEO视频，COUPON优惠券，QUESTIONNAIRE问卷")
    private String attachmentType;

    @ApiModelProperty(value = "可见规则，1全部，2指定")
    private Integer visibleType;

    @NotNull(message = "推送时段类型(pushType)为必传项")
    @ApiModelProperty(value = "推送类型，1全时段，2指定时段")
    private Integer pushType;

    @ApiModelProperty(value = "EVERY_DAY(每天)，EVERY_WEEK(每周)，EVERY_MONTH(每月)，COUPON（优惠券）")
    private String pushRule;

    @ApiModelProperty(value = "指定人群标签")
    private Long userTagId;

    @ApiModelProperty(value = "日期标识数组")
    private String ruleTimeTag;

    @ApiModelProperty(value = "指定门店ID集合")
    private List<Long> storeIdList;

//    @Size(max = 5, message = "最多可添加5个广告附件")
    @ApiModelProperty(value = "广告附件集合")
    private List<AddPopUpAdAttachmentDTO> popUpAdAttachmentList;

    @Size(max = 3, message = "最多可设置3个广告推送时段规则")
    @ApiModelProperty(value = "广告推送时段规则集合")
    private List<AddPopUpAdPushRuleDTO> popUpAdPushRuleList;

    @Size(max = 10, message = "广告应用页面最多设置10个")
    @ApiModelProperty(value = "广告应用页面")
    private List<String> pageList;

}
