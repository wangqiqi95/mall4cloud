package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddTagActivityShopsDTO {
    @ApiModelProperty("活动 id")
    @NotNull(message = "活动id不能为空")
    private  Long id;
    @ApiModelProperty("商店id")
    private List<Long> shops;
}
