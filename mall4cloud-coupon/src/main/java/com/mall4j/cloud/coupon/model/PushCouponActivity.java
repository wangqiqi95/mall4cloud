package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @description 优惠券活动表
 * @author shijing
 * @date 2022-01-05
 */
@Data
public class PushCouponActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动状态（0：启用/1：禁用）
     */
    private int status;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 是否全部门店（0：全部门店/1：部分门店）
     */
    private Boolean isAllShop;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 创建人
     */
    private Long createId;

    /**
     * 创建人姓名
     */
    private String createName;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 修改人
     */
    private Long updateId;

    /**
     * 修改人姓名
     */
    private String updateName;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

    /**
     * 是否删除
     */
    private int del;

    @ApiModelProperty("订单消费限制开关")
    private Integer orderSwitch;

    @ApiModelProperty("订单消费开始时间")
    private Date orderStartTime;

    @ApiModelProperty("订单消费结束时间")
    private Date orderEndTime;

    @ApiModelProperty("订单消费类型【0:表示累计消费;1:表示单笔消费】")
    private Integer orderExpendType;

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

    @ApiModelProperty("指定消费门店")
    private String appointShop;

}
