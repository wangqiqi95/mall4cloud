package com.mall4j.cloud.user.vo.scoreConvert;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "积分换物列表参数")
public class ScoreBarterListVO{

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
    @ApiModelProperty(value = "活动状态Key值")
    private Integer activityStatusKey;

    /**
     * 状态
     */
    @ApiModelProperty(value = "活动状态")
    private String activityStatus;

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
     * 适用门店id集合
     */
    @ApiModelProperty(value = "适用门店数量")
    private List<Long> shops;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String commodityName;

    /**
     * 发货方式（0：邮寄/1：门店自取）
     */
    @ApiModelProperty(value = "发货方式（0：邮寄/1：门店自取）")
    private Short deliveryType;

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
     * 剩余库存
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "剩余库存")
    private Long surplusAmount;


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

}
