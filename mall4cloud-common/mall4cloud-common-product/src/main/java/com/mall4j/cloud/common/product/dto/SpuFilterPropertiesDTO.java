package com.mall4j.cloud.common.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author axin
 * @Date 2023-06-12
 **/
@Data
public class SpuFilterPropertiesDTO {
    @ApiModelProperty("所属类别")
    private List<AttrMappingDTO> spuStyleTypes;

    @ApiModelProperty("所属人群")
    private List<AttrMappingDTO> sexMappings;

    @ApiModelProperty("是否默认属性")
    private Boolean defaultProp;

    @ApiModelProperty("是否模糊匹配")
    private Boolean vague;

    @ApiModelProperty("关联属性id")
    private Long attrId;


    @Data
    public  static class AttrMappingDTO {
        private String attrName;

        private List<String> attrValue;
    }
}
