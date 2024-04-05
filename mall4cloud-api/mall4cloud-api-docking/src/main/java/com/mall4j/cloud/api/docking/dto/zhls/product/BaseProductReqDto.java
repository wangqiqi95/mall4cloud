package com.mall4j.cloud.api.docking.dto.zhls.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Author axin
 * @Date 2023-03-10 14:28
 **/
@Data
public class BaseProductReqDto {
    @ApiModelProperty(value = "数据源id（getSourceId方法返回的id）")
    private String dataSourceId;
}
