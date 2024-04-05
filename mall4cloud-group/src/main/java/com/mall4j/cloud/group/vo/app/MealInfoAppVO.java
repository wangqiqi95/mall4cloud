package com.mall4j.cloud.group.vo.app;

import com.mall4j.cloud.group.vo.MealCommodityPoolVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class MealInfoAppVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("套餐价格")
    private BigDecimal mealPrice;
    @ApiModelProperty("优惠类型 0 无优惠 1 抵用券叠加")
    private Integer preferenceType;
    @ApiModelProperty("重复购买开关 0 禁用 1启用")
    private Integer buyRepeatSwitch;

    @ApiModelProperty("商品池列表-APP")
    private List<MealCommodityPoolAppVO> pools;
}
