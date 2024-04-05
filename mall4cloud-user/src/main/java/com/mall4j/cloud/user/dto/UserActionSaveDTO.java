package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserActionSaveDTO {

    @ApiModelProperty("触点号")
    private String tentacleNo;

    @NotNull(message = "type 不能为空")
    @ApiModelProperty("类型 1:购买 2:加购 3:浏览 4:收藏")
    private Integer type;

    @NotNull(message = "pageType 不能为空")
    @ApiModelProperty("页面类型 1:海报 2:专题 3:朋友圈 4:商品 5:会员页 6:首页")
    private Integer pageType;

    @ApiModelProperty("活动id")
    private Long activityId;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;
}
