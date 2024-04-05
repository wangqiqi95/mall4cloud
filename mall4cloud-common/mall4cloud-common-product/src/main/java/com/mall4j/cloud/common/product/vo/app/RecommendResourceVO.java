package com.mall4j.cloud.common.product.vo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 种草信息资源VO
 *
 * @author cg
 */
@Data
public class RecommendResourceVO{

    @ApiModelProperty("主键id")
    private Long recommendResourceId;

    @ApiModelProperty("种草id")
    private Long recommendId;

    @ApiModelProperty("资源类型：1-图片 | 2-视频")
    private Integer type;

    @ApiModelProperty("资源地址")
    private String url;
}
