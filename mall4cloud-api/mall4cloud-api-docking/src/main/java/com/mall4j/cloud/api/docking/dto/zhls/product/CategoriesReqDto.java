package com.mall4j.cloud.api.docking.dto.zhls.product;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author axin
 * @Date 2023-03-10 15:25
 **/
@Data
public class CategoriesReqDto extends BaseProductReqDto{
    private List<CategoryDto> categories;
}
