package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("预售活动列表实体")
public class AdvanceActivityListVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("商品名称")
    private String commodityName;
    @ApiModelProperty("商品图片链接")
    private String commodityPicUrl;
    @ApiModelProperty("预售活动开始时间")
    private Date advanceBeginTime;
    @ApiModelProperty("预售活动截至时间")
    private Date advanceEndTime;
    @ApiModelProperty("活动最低价")
    private BigDecimal activityMinPrice;
    @ApiModelProperty("活动最高价")
    private BigDecimal activityMaxPrice;
    @ApiModelProperty("发货时间")
    private Date deliverTime;
    @ApiModelProperty("适用门店")
    private String applyShopIds;
    @ApiModelProperty("活动状态 0 禁用 1启用 2进行中 3 未开始 4已结束")
    private Integer status;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "创建人名称")
    private String createUserName;
}
