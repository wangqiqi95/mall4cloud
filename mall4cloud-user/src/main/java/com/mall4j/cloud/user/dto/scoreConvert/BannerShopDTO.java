package com.mall4j.cloud.user.dto.scoreConvert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Banner重复门店参数
 *
 * @author shijing
 * @date 2022-1-23
 */

@Data
@ApiModel(description = "Banner重复门店参数")
public class BannerShopDTO {

    @ApiModelProperty(value = "主键id")
    private Long activityId;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店编码")
    private String storeCode;

}
