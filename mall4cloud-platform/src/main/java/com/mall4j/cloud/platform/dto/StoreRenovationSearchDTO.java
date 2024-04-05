package com.mall4j.cloud.platform.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("装修页查询参数")
public class StoreRenovationSearchDTO extends PageDTO {

    private Long renovationId;

    /**
     * 门店id
     */
    @ApiModelProperty("门店id")
    private Long storeId;

    @ApiModelProperty("状态 0-未启用 1-已启用")
    private Integer status;

    /**
     * 装修名称
     */
    @ApiModelProperty("装修名称")
    private String name;

    /**
     * 是否主页 1.是 0.不是
     */
    @ApiModelProperty("页面类型 0-微页面，1 -主页，2-底部导航，3-分类页，4-会员主页")
    private Integer homeStatus;

}
