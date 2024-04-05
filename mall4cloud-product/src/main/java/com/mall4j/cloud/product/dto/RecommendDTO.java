package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * @author cg
 */
@Data
public class RecommendDTO {

    @ApiModelProperty("主键id-修改时必传")
    private Long recommendId;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty("标题")
    private String title;

    @NotBlank(message = "正文不能为空")
    @ApiModelProperty("正文")
    private String content;

    @NotBlank(message = "封面不能为空")
    @ApiModelProperty("封面")
    private String coverUrl;

    @ApiModelProperty("商品ids，逗号隔开")
    private String productIds;

    @ApiModelProperty("关联视频地址")
    private String video;

    @ApiModelProperty("关联图片地址")
    private List<String> images;

}
