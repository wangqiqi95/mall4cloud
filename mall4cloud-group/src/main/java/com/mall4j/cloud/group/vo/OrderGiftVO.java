package com.mall4j.cloud.group.vo;

import com.mall4j.cloud.group.model.OrderGiftShop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("下单赠品详情实体")
public class OrderGiftVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;
    @ApiModelProperty("活动截至时间")
    private Date activityEndTime;
    @ApiModelProperty("是否全部门店")
    private Integer isAllShop;
    @ApiModelProperty("赠品限制")
    private Integer giftLimit;
    @ApiModelProperty("适用商品集合")
    private String applyCommodityIds;
    @ApiModelProperty("状态 0未启用 1已启用")
    private Integer status;

    @ApiModelProperty("赠品库存列表")
    private List<OrderGiftStockVO> stocks;

    @ApiModelProperty("适用门店列表")
    private List<OrderGiftShop> shops;
}
