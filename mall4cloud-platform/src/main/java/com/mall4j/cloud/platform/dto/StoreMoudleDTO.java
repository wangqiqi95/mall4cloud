package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("插件门店参数")
public class StoreMoudleDTO {

    /**
     * 门店id
     */
    @ApiModelProperty("门店id")
    private Long storeId;

    /**
     * 门店id
     */
    @ApiModelProperty("组织节点id")
    private Long orgId;

    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")
    private String stationName;


    /**
     * 0:关闭 1:营业 2:强制关闭 3:审核中 4:审核失败
     */
    @ApiModelProperty("0:关闭 1:营业 2:强制关闭 3:审核中 4:审核失败")
    private Integer status;

    @ApiModelProperty("第三方门店编码")
    private String storeCode;

    @ApiModelProperty("c端是否展示 0-不展示 ，1 -展示")
    private Integer isShow;

    @ApiModelProperty("门店类型1-自营，2-经销，3-代销，4-电商，5-其他")
    private Integer type;
}
