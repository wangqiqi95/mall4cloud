package com.mall4j.cloud.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("已选择门店展示")
public class SelectStoreVO {
    @ApiModelProperty("门店id")
    private Long storeId;
    @ApiModelProperty("门店编码")
    private String storeCode;
    @ApiModelProperty("门店名称")
    private String stationName;
    @ApiModelProperty("距离")
    private String storeDistance;
    @ApiModelProperty("地址")
    private String addr;
    @ApiModelProperty("经度")
    private Double lng;
    @ApiModelProperty("纬度")
    private Double lat;

}
