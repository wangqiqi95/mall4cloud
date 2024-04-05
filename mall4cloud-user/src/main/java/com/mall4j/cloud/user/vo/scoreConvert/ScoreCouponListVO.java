package com.mall4j.cloud.user.vo.scoreConvert;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "积分换券列表参数")
public class ScoreCouponListVO{

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String convertTitle;

    /**
     * 状态
     */
    @ApiModelProperty(value = "活动状态")
    private String activityStatus;

    /**
     * 状态
     */
    @ApiModelProperty(value = "活动状态Key值")
    private Integer activityStatusKey;

    /**
     * 积分换券活动类型（0：积分兑礼/1：积分换券）
     */
    @ApiModelProperty(value = "积分换券活动类型（0：积分兑礼/1：积分换券）")
    private Short type;

    /**
     * 是否全部门店
     */
    @ApiModelProperty(value = "是否全部门店")
    private Boolean isAllShop;

    /**
     * 适用门店数量
     */
    @ApiModelProperty(value = "适用门店数量")
    private Integer shopNum;


    /**
     * 优惠券
     */
    @ApiModelProperty(value = "优惠券名称")
    private List<String> couponNames;

    /**
     * 积分兑换数
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "所需积分")
    private Long convertScore;

    /**
     * 限制兑换总数
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "总库存")
    private Long maxAmount;

    /**
     * 限制兑换总数
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "剩余库存")
    private Long surplusAmount;

    /**
     * 排序
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "排序")
    private Long sort;


    /**
     * 状态 0：启用/1：停用
     */
    @ApiModelProperty(value = "状态 0：启用/1：停用")
    private Integer convertStatus;

    @ApiModelProperty("c端是否展示 0-展示 ，1 -不展示")
    private Integer isShow;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty("订单消费限制开关")
    private Integer orderSwitch;

    @ApiModelProperty("订单消费开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date orderStartTime;


    @ApiModelProperty("订单消费结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date orderEndTime;

    @ApiModelProperty("订单消费金额限制")
    private Long orderNum;

    @ApiModelProperty("订单类型限制")
    private String orderType;

    @ApiModelProperty("订单金额不足提示")
    private String orderTips;

    @ApiModelProperty("粉丝等级集合")
    private String fanLevels;

    @ApiModelProperty("粉丝等级不足提示")
    private String fanTips;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String commodityName;

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String commodityImgUrl;
    /**
     * 礼品描述
     */
    @ApiModelProperty("礼品描述")
    private String commodityDes;

}
