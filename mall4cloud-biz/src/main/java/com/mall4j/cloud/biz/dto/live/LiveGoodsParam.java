

package com.mall4j.cloud.biz.dto.live;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @author chaoge
 */
@Data
public class LiveGoodsParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "门店id")
    private Long shopId;

    @ApiModelProperty(value = "商品封面，原图")
    private String coverPic;

    @ApiModelProperty(value = "商品封面，存储的是mediaID（mediaID获取后，三天内有效）")
    private String coverImgUrl;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "价格类型，1：一口价（只需要传入price，price2不传） 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传） 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传）")
    private BigDecimal priceType;

    @ApiModelProperty(value = "商品价格(元)")
    private BigDecimal price;

    @ApiModelProperty(value = "商品价格(元)")
    private BigDecimal price2;

    @ApiModelProperty(value = "商品详情页的小程序路径，路径参数存在 url 的，该参数的值需要进行 encode 处理再填入")
    private String url;

    @ApiModelProperty(value = "主播微信号")
    private String anchorWechat;
}
