package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("预售活动详情实体")
public class AdvanceActivityVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("预售活动开始时间")
    private Date advanceBeginTime;
    @ApiModelProperty("预售活动截至时间")
    private Date advanceEndTime;
    @ApiModelProperty("发货时间")
    private Date deliverTime;
    @ApiModelProperty("适用门店")
    private String applyShopIds;
    @ApiModelProperty("商品id")
    private Long commodityId;
    @ApiModelProperty("预告开关")
    private Integer heraldSwitch;
    @ApiModelProperty("最小起售数量")
    private Integer minSellNum;
    @ApiModelProperty("购买限制数量")
    private Integer buyLimitNum;
    @ApiModelProperty("参与门槛开关")
    private Integer participationThreshold;
    @ApiModelProperty("参与粉丝等级集合")
    private String fansLevel;
    @ApiModelProperty("优惠类型 0 无优惠 1 抵用券叠加")
    private Integer preferenceType;
    @ApiModelProperty("状态 0未启用 1已启用")
    private Integer status;


    @ApiModelProperty("商品列表")
    private List<AdvanceCommodityVO> commodities;
}
