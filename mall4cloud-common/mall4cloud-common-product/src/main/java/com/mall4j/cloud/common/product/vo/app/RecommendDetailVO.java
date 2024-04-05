package com.mall4j.cloud.common.product.vo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 种草详情VO
 *
 * @author cg
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendDetailVO extends RecommendVO{

    @ApiModelProperty("关联视频图片资源")
    private List<RecommendResourceVO> resourceList;
}
