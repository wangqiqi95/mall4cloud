package com.mall4j.cloud.api.platform.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class InsiderStorePageDTO extends PageDTO {

    private List<Long> storeIdList;

    private List<String> storeCodeList;

    /**
     * 市id
     */
    private Long cityId;

    /**
     * 区id
     */
    private Long areaId;

    /**
     * 省id
     */
    private Long provinceId;

    /**
     * 地址
     */
    private String addr;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 经度
     */
    private Double lng;

    /**
     * 纬度
     */
    private Double lat;

    @ApiModelProperty("状态(目前仅优惠券适用门店过滤使用)")
    private List<String> slcNames;
}
