package com.mall4j.cloud.api.platform.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class StoreSelectedParamFeignDTO extends PageDTO {

    @ApiModelProperty("门店名称/编码")
    private String keyword;
    @ApiModelProperty("已选择门店id集合")
    private List<Long> storeIdList;

    @ApiModelProperty("自定义单页最大条数")
    private Integer cusPageSize;
}
