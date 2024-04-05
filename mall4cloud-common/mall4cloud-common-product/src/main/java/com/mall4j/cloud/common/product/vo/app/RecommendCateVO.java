package com.mall4j.cloud.common.product.vo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 种草分类VO
 *
 * @author cg
 */
@Data
public class RecommendCateVO {

    @ApiModelProperty("主键id")
    private Long recommendCateId;

    @ApiModelProperty("创建人name")
    private String userName;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("是否显示：0-否 | 1-是")
    private Integer isShow;

    @ApiModelProperty("是否默认：0-否 | 1-是")
    private Integer isDefault;

    @ApiModelProperty("关联种草数量")
    private Integer recommendCount;

    @ApiModelProperty("封面")
    private String coverUrl;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
