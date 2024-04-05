package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserActionListVO {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("足迹类型 1:购买 2:加购 3:浏览 4:收藏")
    private Integer type;

    @ApiModelProperty("页面类型 1:海报 2:专题 3:朋友圈 4:商品 5:会员页 6:首页")
    private Integer pageType;

    @ApiModelProperty("活动id")
    private Long activityId;

    private Long orderId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("创建时间")
    protected Date createTime;

}
