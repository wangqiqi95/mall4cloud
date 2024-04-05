package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VeekerApplyVO {

    @ApiModelProperty("申请状态 1:申请成功 0:申请失败")
    private Integer applyStatus;
    @ApiModelProperty("专属导购所属门店")
    private Long storeId;
    @ApiModelProperty("专属导购所属门店")
    private String storeName;
    @ApiModelProperty("专属导购名称")
    private String staffName;
    @ApiModelProperty("专属导购触点号")
    private String tentacleNo;

}
