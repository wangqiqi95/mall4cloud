package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("套餐一口价添加实体")
public class MealActivityDTO implements Serializable {
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
    @ApiModelProperty("套餐价格")
    private BigDecimal mealPrice;
    @ApiModelProperty("优惠类型 0 无优惠 1 抵用券叠加")
    private Integer preferenceType;
    @ApiModelProperty("状态 0未启用 1已启用")
    private Integer status;
    @ApiModelProperty("重复购买开关 0 禁用 1启用")
    private Integer buyRepeatSwitch;
    @ApiModelProperty("权重")
    private Integer weight;
    @ApiModelProperty(value = "创建人id",hidden = true)
    private Long createUserId;
    @ApiModelProperty(value = "创建人名称",hidden = true)
    private String createUserName;
    @ApiModelProperty(value = "更新人id",hidden = true)
    private Long updateUserId;
    @ApiModelProperty(value = "更新人姓名",hidden = true)
    private String updateUserName;

    @ApiModelProperty("商品池列表")
    private List<MealCommodityPoolDTO> pools;
}
