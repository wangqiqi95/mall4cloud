package com.mall4j.cloud.api.group.feign.vo;

import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("下单送赠品库存信息app实体")
public class OrderGiftStockAppVO implements Serializable {
    @ApiModelProperty("赠品id")
    private Integer id;
    @ApiModelProperty("活动id")
    private Integer orderGiftId;
    @ApiModelProperty("商品id")
    private Long commodityId;
    @ApiModelProperty("商品信息")
    private SpuCommonVO commodityInfo;
    @ApiModelProperty("商品库存")
    private Integer commodityStock;
}
