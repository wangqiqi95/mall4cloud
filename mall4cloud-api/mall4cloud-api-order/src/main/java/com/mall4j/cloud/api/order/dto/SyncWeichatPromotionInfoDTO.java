package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SyncWeichatPromotionInfoDTO {

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;
    @ApiModelProperty(value = "推广员唯一ID")
    private String promoterId;
    @ApiModelProperty(value = "推广员视频号昵称")
    private String finderNickname;
    @ApiModelProperty(value = "分享员openid")
    private String sharerOpenid;

}
