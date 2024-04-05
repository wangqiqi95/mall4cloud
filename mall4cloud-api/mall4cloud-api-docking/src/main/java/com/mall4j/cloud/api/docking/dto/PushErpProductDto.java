package com.mall4j.cloud.api.docking.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2022年8月12日, 0012 15:14
 * @Created by eury
 */
@Data
public class PushErpProductDto {

    @ApiModelProperty(value = "推送条数",required = false)
    private Integer pageSize=0;

    @ApiModelProperty(value = "商品货号",required = false)
    private List<String> spuCodes=new ArrayList<>();

}
