package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 有数商品推荐
 * @Author axin
 * @Date 2023-05-08 11:49
 **/
@Data
public class ZhlsCommodityReqDto extends PageDTO {
    @ApiModelProperty(value = "第一次请求传空，第二次请求传上一次返回的")
    private String sequenceId;

    @ApiModelProperty(value = "请求热度推荐（true则返回热度，false则正常返回个性化结果，默认为false）")
    private Boolean reqHotRecommend;

    @ApiModelProperty(value = "相关性策略类型时必填，传商品的spuid，为您推荐该商品的相似商品")
    private String itemId;

    @ApiModelProperty(value = "排除的商品ID列表，对这些商品不做推荐")
    private List<String> excludeItemIds;

}
