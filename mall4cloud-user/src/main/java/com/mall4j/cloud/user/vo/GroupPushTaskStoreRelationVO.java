package com.mall4j.cloud.user.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupPushTaskStoreRelationVO {

    @ApiModelProperty(value = "主键")
    private Long taskShopRelationId;

    @ApiModelProperty(value = "群发任务ID")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
}
