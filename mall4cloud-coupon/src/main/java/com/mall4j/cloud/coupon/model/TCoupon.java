package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 优惠券设置
 * @author shijing
 * @date 2022-01-03
 */
@Data
@TableName("t_coupon")
public class TCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 主键id
     */
    private Long id;

    /**
     * 优惠券id
     */
    private String code;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）
     */
    private Short kind;

    /**
     * 优惠券类型（0：抵用券/1：折扣券）
     */
    private Short type;

    /**
     * 价格类型（0：吊牌价/1：实付金额）
     */
    private Short priceType;

    /**
     * 折扣券最大抵扣金额
     */
    private BigDecimal maxDeductionAmount;

    /**
     * 抵用金额
     */
    private BigDecimal reduceAmount;

    /**
     * 折扣力度
     */
    private BigDecimal couponDiscount;

    /**
     * 金额限制类型（0：不限/1：满额）
     */
    private Short amountLimitType;

    /**
     * 限制金额
     */
    private BigDecimal amountLimitNum;

    /**
     * 商品限制类型（0：不限/1：不超过/2：不少于）
     */
    private Short commodityLimitType;

    /**
     * 商品限制件数
     */
    private Integer commodityLimitNum;

    /**
     * 商品限制件数  当类型为数量区间时存最大值
     */
    private Integer commodityLimitMaxNum;

    /**
     * 适用商品类型（0：不限/1：指定商品/2：指定商品不可用）
     */
    private Short commodityScopeType;

    /**
     * 适用商品 分类类型（0：不限/1：指定分类可用）
     */
    private Short categoryScopeType;

    /**
     * 适用范围（0：不限/1：线上/2：线下）
     */
    private Short applyScopeType;

    /**
     * "优惠券图片标识（0：默认/1：礼品券/2：生日礼/3:员工券/4:升级礼/5：保级礼/6:服务券/7:满减券/8:折扣券/9:入会券/10:免邮券）")
     */
    private Integer couponPicture;

    /**
     * 是否全部门店
     */
    private Boolean isAllShop;

    /**
     * 优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
     */
    private Short sourceType;

    /**
     * 优惠券封面
     */
    private String coverUrl;

    /**
     * 优惠券说明
     */
    private String description;

    /**
     * 优惠券备注
     */
    private String note;

    /**
     * 生效时间类型（1：固定时间/2：领取后生效）
     */
    private int timeType;

    /**
     * 生效开始时间
     */
    private Date startTime;

    /**
     * 生效结束时间
     */
    private Date endTime;

    /**
     * 领券后x天起生效
     */
    private Integer afterReceiveDays;

    /**
     * 优惠券状态（0：有效/1：失效）
     */
    private int status;

    /**
     * 创建人
     */
    private Long createId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * crm优惠券id
     */
    private String crmCouponId;

    /**
     * 是否有商品原折扣限制
     */
    private Integer disNoles;

    /**
     * 折扣限制值（例如7折维护 7 ）
     */
    private BigDecimal disNolesValue;

    /**
     * 是否支持0元商品单
     */
    private Integer issharePaytype;

}
