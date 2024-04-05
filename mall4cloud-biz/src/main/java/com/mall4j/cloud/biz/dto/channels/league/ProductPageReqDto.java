package com.mall4j.cloud.biz.dto.channels.league;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description
 * @Author axin
 * @Date 2023-02-21 15:10
 **/
@Data
public class ProductPageReqDto extends PageDTO {

    @ApiModelProperty(value = "计划id",required = true)
    @NotNull(message = "计划id不能为空")
    private Long id;

    @ApiModelProperty(value = "商品ids")
    private List<String> productIds;
}
