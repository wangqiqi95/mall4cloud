package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PopUpAdContainerVO {

    @ApiModelProperty(value = "优惠券广告列表")
    private List<PopUpAdFormCouponVO> popUpAdFormCouponList;

    @ApiModelProperty(value = "视频广告列表")
    private List<PopUpAdFormVideoVO> popUpAdFormVideoList;

    @ApiModelProperty(value = "图片广告列表")
    private List<PopUpAdFormImageVO> popUpAdFormImageVOList;

    @ApiModelProperty(value = "问卷广告列表")
    private List<PopUpAdFormQuestionnaireVO> popUpAdFormQuestionnaireVOList;

}
