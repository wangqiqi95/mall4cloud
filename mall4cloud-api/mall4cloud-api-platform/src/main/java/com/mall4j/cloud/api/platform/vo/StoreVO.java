package com.mall4j.cloud.api.platform.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StoreVO {
    private Long  storeId;

    private String name;

    private String storeCode;

    private Integer status;

    @ApiModelProperty("开启电子价签同步：0否 1是")
    private Integer pushElStatus;

    @ApiModelProperty("是否虚拟门店：0否 1是")
    private Integer storeInviteType=0;

    @ApiModelProperty("官店门店code")
    private String mainStoreCode;
}
