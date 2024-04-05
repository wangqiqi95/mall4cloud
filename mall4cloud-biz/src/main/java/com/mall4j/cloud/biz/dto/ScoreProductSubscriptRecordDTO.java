package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Grady_Lu
 */
@Data
public class ScoreProductSubscriptRecordDTO {

    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;

    @ApiModelProperty(value = "积分活动Id",required = true)
    private Long convertId;

    @ApiModelProperty(value = "店铺编号集合",required = true)
    private List<String> storeCodeList;

    @ApiModelProperty(value = "spuId集合",required = true)
    private List<Long> spuIdList;


}
