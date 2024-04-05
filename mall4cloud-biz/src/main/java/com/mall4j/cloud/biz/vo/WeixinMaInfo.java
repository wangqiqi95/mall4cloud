package com.mall4j.cloud.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信小程序模板素材表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:56
 */
@Data
public class WeixinMaInfo {
    private static final long serialVersionUID = 1L;

//    @ApiModelProperty("小程序名称")
//    private String maAppName;

    @ApiModelProperty("小程序appid")
    private String maAppId;

}
