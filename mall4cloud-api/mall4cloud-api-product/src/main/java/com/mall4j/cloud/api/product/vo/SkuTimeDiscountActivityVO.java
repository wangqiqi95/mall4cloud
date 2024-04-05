package com.mall4j.cloud.api.product.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @luzhengxiang
 * @create 2022-03-12 5:11 PM
 **/
@Data
public class SkuTimeDiscountActivityVO {
    @ApiModelProperty("")
    private Integer activityId;
    @ApiModelProperty("")
    private Long spuId;
    @ApiModelProperty("")
    private Long skuId;
    @ApiModelProperty("售价，整数方式保存")
    private Long price;

    //在会员日活动中是否可以使用优惠券标识 0否1是
    private Integer friendlyCouponUseFlag = 1;

    //在会员日活动中是否可以参加满减活动标识 0否1是
    private Integer friendlyDiscountFlag = 1;

    //1:门店会员价 2:官店会员价 3:门店限时调价 4:官店限时调价 5:虚拟门店活动价
    private Integer priceType=0;

    //是否使用会员日活动价
    private boolean memberPriceFlag=false;

    //是否使用虚拟门店价
    private boolean invateStorePriceFlag=false;
}
