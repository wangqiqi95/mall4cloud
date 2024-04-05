package com.mall4j.cloud.user.model.scoreConvert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 积分活动表
 *
 * @author shijing
 * @date 2021-12-10 17:18:04
 */

@Data
@TableName("score_convert")
public class ScoreConvert extends BaseModel implements Serializable {

    private static final long serialVersionUID = -762291655634719586L;


    /**
     * 主键ID
     */
    @TableId(type= IdType.AUTO)
    private Long convertId;

    /**
     * 标题
     */
    private String convertTitle;

    /**
     * 积分兑换数
     */
    private Long convertScore;

    /**
     * 积分活动封面
     */
    private String convertUrl;

    /**
     * 限制兑换总数
     */
    private Long maxAmount;

    /**
     * 每人限制兑换数
     */
    private Long personMaxAmount;


    /**
     * 兑换活动种类（0：积分换物/1：积分换券）
     */
    private Short convertType;

    /**
     * 积分换券活动类型（0：积分兑礼/1：积分换券/2:兑礼到家）
     */
    private Short type;


    /**
     * 状态 0：启用/1：停用
     */
    private Integer convertStatus;

    @ApiModelProperty("c端是否展示 0-展示 ，1 -不展示")
    private Integer isShow;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 优惠券id
     */
    private Long couponId;
    /**
     * 是否全部门店
     */
    private Boolean isAllShop;

    /**
     * 是否全部门店（兑换门店）
     */
    private Boolean isAllConvertShop;

    /**
     * 是否全部门店（优惠券）
     */
    private Boolean isAllCouponShop;
    /**
     * 描述
     */
    private String description;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 商品图片
     */
    private String commodityImgUrl;

    /**
     * 发货方式（0：邮寄/1：门店自取）
     */
    private Short deliveryType;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建人ID
     */
    private Long createId;

    /**
     * 创建人姓名
     */
    private String createName;

    /**
     * 修改人ID
     */
    private Long updateId;

    /**
     * 修改人姓名
     */
    private String updateName;

    /**
     * 是否删除（0：未删除/1：已删除）
     */
    private Short del;

    /**
     * 库存
     */
    private Long stocks;

    /**
     * 版本号
     */
    private Integer version;


    @ApiModelProperty("活动开始前x小时提醒")
    private Integer newRemind;
    @ApiModelProperty("活动开始前提醒时间")
    private Date newRemindTime;
    @ApiModelProperty("是否发送积分上新订阅消息 0 否 1是")
    private Integer newNotice;
    @ApiModelProperty("积分上新温馨提示")
    private String newReminder;

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

    @ApiModelProperty("礼品描述")
    private String commodityDes;

    @ApiModelProperty("指定消费门店")
    private String appointShop;

}
