package com.mall4j.cloud.group.vo;

import com.mall4j.cloud.api.coupon.vo.CouponListVO;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireDetailVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PopUpAdAttachmentVO {

    @ApiModelProperty(value = "主键")
    private Long popUpAdMediaId;

    @ApiModelProperty(value = "开屏广告主键")
    private Long popUpAdId;

    @ApiModelProperty(value = "素材链接")
    private String mediaUrl;

    @ApiModelProperty(value = "页面链接")
    private String link;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    @ApiModelProperty(value = "业务ID，与广告设置的业务类型关联")
    private Long businessId;

    @ApiModelProperty(value = "页面链接类型，H5，MINI_PROGRAM")
    private String linkType;

    @ApiModelProperty(value = "优惠券信息")
    private CouponListVO coupon;

    @ApiModelProperty(value = "优惠券信息")
    private QuestionnaireDetailVO questionnaire;

}
