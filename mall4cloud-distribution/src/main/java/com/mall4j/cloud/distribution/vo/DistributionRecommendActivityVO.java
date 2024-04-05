package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 活动主体-ViewObject
 *
 * @author EricJeppesen
 * @date 2022/10/19 10:18
 */
@Data
public class DistributionRecommendActivityVO implements Serializable {

    private static final long serialVersionUID = -1731069908182270929L;
    /**
     * 标识
     */
    @ApiModelProperty("标识")
    private Long id;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 活动有效期（起始时间）
     */
    @ApiModelProperty("活动有效期（起始时间）")
    private Date activityBeginTime;

    /**
     * 活动有效期（结束时间）
     */
    @ApiModelProperty("活动有效期（结束时间）")
    private Date activityEndTime;

    /**
     * 是否适用所有的门店-1是,0否
     */
    @ApiModelProperty("是否适用所有的门店-1是,0否")
    private Integer applicableForAllStores;

    /**
     * 是否适用所有的产品-1是,0否
     */
    @ApiModelProperty("是否适用所有的产品-1是,0否")
    private Integer applicableForAllGoods;

    /**
     * 适用门店数量（缓存）
     */
    @ApiModelProperty("适用门店数量")
    private Integer applicableStoresAmount;

    /**
     * 参加活动SPU数量
     */
    @ApiModelProperty("参加活动商品SPU数量")
    private Integer activityGoodsAmount;

    /**
     * 活动开启状态-0未开启,1已开启
     */
    @ApiModelProperty("活动开启状态-0未开启,1已开启,2进行中,3已结束")
    private Integer activityStatus;

    private String statusName;

    /**
     * 创建人（标识）
     */
    @ApiModelProperty("创建人（标识）")
    private Long createUserId;
    /**
     * 创建人（姓名）
     */
    @ApiModelProperty("创建人（姓名）")
    private String createUserName;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

}
