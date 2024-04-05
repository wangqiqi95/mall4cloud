package com.mall4j.cloud.user.vo.scoreConvert;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "积分换物参数")
public class ScoreBarterVO extends BaseVO {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long convertId;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String convertTitle;

    /**
     * 积分兑换数
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "积分兑换数")
    private Long convertScore;

    /**
     * 限制兑换总数
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "总库存")
    private Long maxAmount;

    /**
     * 每人限制兑换数
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "每人限制兑换数")
    private Long personMaxAmount;

    /**
     * 状态 0：启用/1：停用
     */
    @ApiModelProperty(value = "状态 0：启用/1：停用")
    private Integer convertStatus;

    @ApiModelProperty("c端是否展示 0-展示 ，1 -不展示")
    private Integer isShow;

    /**
     * 适用门店数量
     */
    @ApiModelProperty(value = "适用门店数量")
    private Integer shopNum;

    /**
     * 兑换门店数量
     */
    @ApiModelProperty(value = "兑换门店数量")
    private Integer convertShopNum;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String commodityName;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String commodityImgUrl;

    /**
     * 发货方式（0：邮寄/1：门店自取）
     */
    @ApiModelProperty(value = "发货方式（0：邮寄/1：门店自取）")
    private Short deliveryType;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 是否全部门店
     */
    @ApiModelProperty(value = "是否全部门店（适用门店）")
    private Boolean isAllShop;

    /**
     * 适用的门店
     */
    @ApiModelProperty(value = "适用门店")
    private List<Long> shops;

    /**
     * 是否全部门店
     */
    @ApiModelProperty(value = "是否全部门店（兑换门店）")
    private Boolean isAllConvertShop;

    /**
     * 适用的门店
     */
    @ApiModelProperty(value = "兑换门店")
    private List<Long> convertShops;

}
