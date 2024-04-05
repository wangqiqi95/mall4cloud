package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信二维码扫码记录表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 18:13:49
 */
@Data
public class WeixinQrcodeScanRecordDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String headimgurl;

    @ApiModelProperty("openid")
    private String openid;

    @ApiModelProperty("扫码时间")
    private Date scanTime;

    @ApiModelProperty("场景值ID")
    private String sceneId;

    @ApiModelProperty("场景值来源")
    private Integer sceneSrc;

    @ApiModelProperty("公众号ID")
    private String appId;

    @ApiModelProperty("是否扫码关注 0:非扫码关注,1:扫码关注")
    private String isScanSubscribe;

    @ApiModelProperty("二维码id")
    private String qrcodeId;

    @ApiModelProperty("二维码ticket")
    private String ticket;

    @ApiModelProperty("扫码自动回复规则id")
    private String autoScanId;

}
