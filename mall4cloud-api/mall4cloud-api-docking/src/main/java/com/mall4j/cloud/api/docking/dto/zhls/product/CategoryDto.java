package com.mall4j.cloud.api.docking.dto.zhls.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author axin
 * @Date 2023-03-10 14:32
 **/
@Data
public class CategoryDto {

    @ApiModelProperty(value = "您为商品类目分配的唯一 id。一般而言，是您在商品库为该商品类目分配的 id。 字段长度最小 1 字节，长度最大 255 字节",required = true)
    @NotBlank(message = "类目id不能为空")
    private String external_category_id;

    @ApiModelProperty(value = "类目名称 字段长度最小 1 字节，长度最大 255 字节",required = true)
    @NotBlank(message = "类目名称不能为空")
    private String category_name;

    @ApiModelProperty(value = "类目类型，1：前台类目；2：后台类目",required = true)
    @NotNull(message = "类目类型不能为空")
    private Integer category_type;

    @ApiModelProperty(value = "类目层级",required = true)
    @NotNull(message = "类目层级不能为空")
    private Integer category_level;

    @ApiModelProperty(value = "父类目 id，顶级类目填 0 字段长度最小 1 字节，长度最大 255 字节",required = true)
    @NotBlank(message = "父类目ID不能为空")
    private String external_parent_category_id;

    @ApiModelProperty(value = "是否为根节点，true：是；false：不是",required = true)
    @NotNull(message = "是否根节点不能为空")
    private Boolean is_root;

    @ApiModelProperty(value = "商家标记商品类目信息已删除，0：未删除；1：已删除，为空默认为 0")
    private Integer is_deleted;
}
