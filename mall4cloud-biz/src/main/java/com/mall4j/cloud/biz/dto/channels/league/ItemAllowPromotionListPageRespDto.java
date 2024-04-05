package com.mall4j.cloud.biz.dto.channels.league;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description 可推广商品列表
 * @Author axin
 * @Date 2023-04-21 11:00
 **/
@Getter
@Setter
public class ItemAllowPromotionListPageRespDto {

    @ApiModelProperty(value = "商品id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String title;

    @ApiModelProperty(value = "spuid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long spuId;

    @ApiModelProperty(value = "商品编码")
    private String spuCode;

    @ApiModelProperty(value = "商品图片")
    private String headImgs;

    @ApiModelProperty(value = "商品价格")
    private Long marketPriceFee;

    @ApiModelProperty(value = "视频号商品id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long outSpuId;


}
