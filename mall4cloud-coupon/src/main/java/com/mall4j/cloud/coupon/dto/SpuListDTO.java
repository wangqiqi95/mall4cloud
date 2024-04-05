package com.mall4j.cloud.coupon.dto;

import com.mall4j.cloud.common.product.dto.SpuSearchAttrDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 券码列表搜索条件 *
 * @author shijing
 * @date 2022-01-05
 */
@Data
@ApiModel(description = "券码列表搜索条件")
public class SpuListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNum = 1;
    @ApiModelProperty(value = "每页长度",required = true)
    private Integer pageSize = 10;

    @ApiModelProperty(value = "优惠券id",required = true)
    private Long couponId;

    @ApiModelProperty(value = "活动id",required = true)
    private Long activityId;

    @ApiModelProperty(value = "市id")
    private Long cityId;

    @ApiModelProperty(value = "省id")
    private Long provinceId;

    @ApiModelProperty(value = "区id")
    private Long areaId;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "经度")
    private Double lng;

    @ApiModelProperty(value = "纬度")
    private Double lat;

    @ApiModelProperty(value = "类型（0：优惠券 / 1：积分兑换活动）")
    private Integer type = 0;

    @ApiModelProperty("状态(目前仅优惠券适用门店过滤使用)")
    private List<String> slcNames;

    @ApiModelProperty(value = "类型（0：到店 / 1：其他）")
    private Integer toShop;

    @ApiModelProperty(value = "排序：1新品,2销量倒序,3销量正序,4商品价格倒序,5商品价格正序,6评论")
    private Integer sort;

    @ApiModelProperty("spu规格属性搜索")
    private SpuSearchAttrDTO spuSearchAttr;

    @ApiModelProperty("活动筛选类型(多选逗号分隔): 1限时调价 2会员日 3拼团")
    private String searchActivityType;

}
