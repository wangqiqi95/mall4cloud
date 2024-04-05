package com.mall4j.cloud.distribution.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 主推商品活动查询DataTransferObject
 *
 * @author EricJeppesen
 * @date 2022/10/19 10:49
 */
@Data
public class DistributionRecommendActivityQueryDTO implements Serializable {

    private static final long serialVersionUID = -7812089225635915829L;
    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * 门店标识
     */
    @ApiModelProperty(value = "门店标识")
    private List<Long> shopIds;

    /**
     * 产品标识
     */
    @ApiModelProperty(value = "产品标识")
    private List<Long> spuIds;

    /**
     * 状态:（-1所有数据，0未开启,1已开启,2进行中,3已结束,）
     */
    @ApiModelProperty(value = "状态:（-1所有数据，0未开启,1已开启,2进行中,3已结束）")
    private Integer activityStatus;

    private PageDTO pageDTO;

}
