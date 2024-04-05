package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("门店查询参数")
public class StorePageQueryDTO {
    @ApiModelProperty("门店名称/编码")
    private String keyword;

    @ApiModelProperty("地址关键字")
    private String address;

    @ApiModelProperty("省id")
    private Long provinceId;

    @ApiModelProperty("城市id")
    private Long cityId;
    @ApiModelProperty("区id")
    private Long areaId;

    @ApiModelProperty("门店id 集合")
    private List<Long> storeIds;

    @ApiModelProperty("根据城市名称模糊查询")
    private String cityName;

    @ApiModelProperty("经度")
    private String lng;

    @ApiModelProperty("纬度")
    private String lat;

    @ApiModelProperty("状态(目前仅C端使用：个人中心、优惠券门店列表)")
    private List<String> slcNames;
}
