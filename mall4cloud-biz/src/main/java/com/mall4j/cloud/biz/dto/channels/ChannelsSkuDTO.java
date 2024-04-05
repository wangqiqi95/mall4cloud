package com.mall4j.cloud.biz.dto.channels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

/**
 * 视频号4.0商品DTO
 *
 * @author FrozenWatermelon
 * @date 2023-02-07 15:01:48
 */
@Data
public class ChannelsSkuDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("小程序商品id")
    private Long spuId;

    @ApiModelProperty("商品id")
    private Long channelsSpuId;

    @ApiModelProperty("sku_id")
    private Long skuId;

    @ApiModelProperty("sku_id")
    private Long outSkuId;

    @ApiModelProperty("sku小图")
    private String thumbImg;

    @ApiModelProperty("商品详情信息")
    private String descInfo;

    @ApiModelProperty("商品封面，存储的是mediaID（mediaID获取后，三天内有效）")
    private String converImgUrl;

    @ApiModelProperty("价格")
    private Long price;

    @ApiModelProperty("小程序预警库存")
    private Integer warningStock;

    @ApiModelProperty("小程序预警库存")
    private Integer channelsWarningStock;

    @ApiModelProperty("商家删除直播商品时间")
    private Date deleteTime;

    @ApiModelProperty(value = "视频号售卖库存")
    private Integer stockNum;

    @ApiModelProperty(value = "skuCode")
    private String skuCode;
}
