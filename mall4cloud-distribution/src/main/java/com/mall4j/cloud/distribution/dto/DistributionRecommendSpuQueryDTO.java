package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class DistributionRecommendSpuQueryDTO {

    @ApiModelProperty("商品名称/编码")
    private String keyword;
    @ApiModelProperty("状态 -1所有 1启用 0禁用")
    private Integer status;
    @ApiModelProperty(value = "平台一级分类id")
    private Long primaryCategoryId;
    @ApiModelProperty(value = "平台二级分类id")
    private Long secondaryCategoryId;
    @ApiModelProperty(value = "平台三级分类id")
    private Long categoryId;
    @ApiModelProperty(value = "C端分类")
    private Long shopCategoryId;
    @ApiModelProperty(value = "排序：1新品,2销量倒序,3销量正序,4商品价格倒序,5商品价格正序")
    private Integer sort;

    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 商品id集合
     */
    private List<Long> spuIdList;
    /**
     * 当前时间
     */
    private Date currentTime;
    /**
     * 是否展示佣金比率
     */
    private boolean showCommissionRate;

    @ApiModelProperty("活动筛选类型(多选逗号分隔): 1限时调价 2会员日 3拼团")
    private String searchActivityType;

    @ApiModelProperty("市场价筛选：最高价(单位分)")
    private Long marketPriceFeeStart;
    @ApiModelProperty("市场价筛选：最低价(单位分)")
    private Long marketPriceFeeEnd;

    @ApiModelProperty("属性筛选")
    private List<String> attrValues;


}
