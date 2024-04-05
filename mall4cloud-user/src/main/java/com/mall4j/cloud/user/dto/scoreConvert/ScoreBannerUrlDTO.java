package com.mall4j.cloud.user.dto.scoreConvert;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description 积分banner活动图片关联表
 * @author shijing
 * @date 2022-01-23
 */
@Data
public class ScoreBannerUrlDTO{

    @ApiModelProperty(value = "链接通用组件-类型")
    private Short type;

    @ApiModelProperty(value = "链接通用组件-链接类型")
    private Short linkType;

    @ApiModelProperty(value = "链接通用组件-小程序appId")
    private String appId;

    @ApiModelProperty(value = "图片url")
    private String bannerUrl;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "链接")
    private String url;

    @ApiModelProperty(value = "链接标题")
    /**
     * 标题
     */
    private String linkTitle;

}
