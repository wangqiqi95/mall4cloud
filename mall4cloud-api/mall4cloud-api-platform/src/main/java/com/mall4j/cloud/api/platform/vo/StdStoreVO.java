package com.mall4j.cloud.api.platform.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StdStoreVO {
    private Long  storeId;

    private String name;

    private String storeCode;

    /**
     * type 1门店 2逻辑仓（中台固定要逻辑仓）
     */
    private String type;
}
