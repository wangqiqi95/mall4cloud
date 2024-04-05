package com.mall4j.cloud.product.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author axin
 * @Date 2023-06-16
 **/
@Data
public class AttrFilterDto {

    private List<String> sexs;

    private List<String> styleTypes;

}
