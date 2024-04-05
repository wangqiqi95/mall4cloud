package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("下单赠品添加实体")
public class OrderGiftDTO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;
    @ApiModelProperty("活动截至时间")
    private Date activityEndTime;
    @ApiModelProperty("是否全部门店 0 否 1是")
    private Integer isAllShop;
    @ApiModelProperty("适用门店")
    private String applyShopIds;
    @ApiModelProperty("赠品限制")
    private Integer giftLimit;
    @ApiModelProperty("适用商品集合")
    private String applyCommodityIds;
    @ApiModelProperty("状态 0未启用 1已启用")
    private Integer status;
    @ApiModelProperty(value = "创建人id",hidden = true)
    private Long createUserId;
    @ApiModelProperty(value = "创建人名称",hidden = true)
    private String createUserName;
    @ApiModelProperty(value = "更新人id",hidden = true)
    private Long updateUserId;
    @ApiModelProperty(value = "更新人姓名",hidden = true)
    private String updateUserName;

    @ApiModelProperty("赠品库存列表")
    private List<OrderGiftStockDTO> stocks;
}
