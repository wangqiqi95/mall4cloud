package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel
public class StoreSelectedParamDTO {

    @ApiModelProperty("门店名称/编码")
    private String keyword;
    @ApiModelProperty("已选择门店id集合")
    private List<Long> storeIdList;

    @ApiModelProperty("自定义单页最大条数")
    private Integer cusPageSize;
}
