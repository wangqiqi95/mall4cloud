package com.mall4j.cloud.api.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("已选择门店展示")
public class SelectedStoreVO {
    @ApiModelProperty("门店id")
    private Long storeId;
    @ApiModelProperty("门店编码")
    private String storeCode;
    @ApiModelProperty("门店名称")
    private String stationName;
    /**
     * 门店图片
     */
    @ApiModelProperty("门店图片")
    private String pic;
    /**
     * 手机/电话号码
     */
    @ApiModelProperty("手机/电话号码")
    private String phone;
    /**
     * 省id
     */
    @ApiModelProperty("省id")
    private Long provinceId;

    /**
     * 省
     */
    @ApiModelProperty("省")
    private String province;

    /**
     * 市id
     */
    @ApiModelProperty("市id")
    private Long cityId;

    /**
     * 市
     */
    @ApiModelProperty("市")
    private String city;

    /**
     * 区id
     */
    @ApiModelProperty("区id")
    private Long areaId;

    /**
     * 区
     */
    @ApiModelProperty("区")
    private String area;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String addr;

    @ApiModelProperty("距离")
    private Double storeDistance;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private Double lng;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private Double lat;

    @ApiModelProperty("spuId列表")
    private List<Long> spuIdList;

    @ApiModelProperty("目标标题")
    private String targetTitle;

    @ApiModelProperty("库存情况：ENOUGH（可兑），SHORT（缺货），FEW（库存紧张）")
    private String inventory;

    @ApiModelProperty("库存排序规则：ENOUGH（1），FEW（2），SHORT（3）")
    private Integer sort;
}
