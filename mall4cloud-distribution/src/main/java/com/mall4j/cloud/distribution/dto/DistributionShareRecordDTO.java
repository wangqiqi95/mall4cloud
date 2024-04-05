package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 分享推广-分享记录DTO
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Data
public class DistributionShareRecordDTO{

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("分享人ID")
    private Long shareId;

    @ApiModelProperty("类型 1 导购 2威客 3会员")
    private Integer shareType;

    @ApiModelProperty("活动ID")
    private Long activityId;

    @ApiModelProperty("活动类型 1海报 2专题 3朋友圈 4商品")
    private Integer activityType;

    @ApiModelProperty("记录是否存在")
    private boolean exist;

    @ApiModelProperty("记录类型 1分享 2浏览 3加购 4下单")
    private Integer recordType;

}
