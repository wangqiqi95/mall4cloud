package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SpuCommReplyDTO {
    @ApiModelProperty("评价id")
    @NotNull(message = "评价id不能为空")
    private Long id;

    @ApiModelProperty("类型 0:评论回复 1:追加评论回复 ")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty("内容 ")
    @NotBlank(message = "内容不能为空")
    private String  content;
}
