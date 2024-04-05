package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信图片模板表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:22
 */
@Data
public class WeixinImgTemplateVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("模板图片")
    private String templateImg;

    @ApiModelProperty("公众号原始id")
    private String appId;

    private String mediaId;

}
