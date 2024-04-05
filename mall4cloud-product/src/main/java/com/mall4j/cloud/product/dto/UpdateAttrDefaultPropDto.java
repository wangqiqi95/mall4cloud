package com.mall4j.cloud.product.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author axin
 * @Date 2023-06-21
 **/
@Data
public class UpdateAttrDefaultPropDto {
    @NotNull(message = "id不能为空")
    private Long attrId;
    private Boolean defaultProp;
}
