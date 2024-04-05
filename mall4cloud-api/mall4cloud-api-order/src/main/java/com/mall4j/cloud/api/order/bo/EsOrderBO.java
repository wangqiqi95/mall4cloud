package com.mall4j.cloud.api.order.bo;

import com.mall4j.cloud.common.order.vo.OrderItemVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2021/2/5
 */
@Data
public class EsOrderBO {

    @ApiModelProperty(value = "订单项", required = true)
    private List<EsOrderItemBO> orderItems;

    @ApiModelProperty(value = "订单商品集合")
    private List<OrderItemVO> orderItemList;

    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    @ApiModelProperty("会员名称")
    private String userName;

    @ApiModelProperty("会员手机号")
    private String userMobile;

    /**
     * 下单代购ID
     */
    @ApiModelProperty(value = "下单代购ID")
    private Long buyStaffId;

    @ApiModelProperty(value = "订单号", required = true)
    private Long orderId;

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNumber;

    @ApiModelProperty(value = "订单号", required = true)
    private Long wechatOrderId;

    @ApiModelProperty(value = "总价", required = true)
    private Long actualTotal;

    @ApiModelProperty(value = "使用积分", required = true)
    private Long orderScore;

    @ApiModelProperty(value = "订单状态", required = true)
    private Integer status;

    @ApiModelProperty(value = "订单类型(0普通订单 1团购订单 2秒杀订单)", required = true)
    private Integer orderType;

    @ApiModelProperty(value = "订单退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）", required = true)
    private Integer refundStatus;

    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递", required = true)
    private Integer deliveryType;

    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    @ApiModelProperty(value = "订单运费", required = true)
    private Long freightAmount;

    @ApiModelProperty(value = "订单创建时间", required = true)
    private Date createTime;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer allCount;

    @ApiModelProperty(value = "订单发票id")
    private Long orderInvoiceId;

    @ApiModelProperty(value = "用户备注信息")
    private String remarks;

    @ApiModelProperty(value = "收货人姓名")
    private String consignee;

    @ApiModelProperty(value = "收货人手机号")
    private String mobile;

    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "区")
    private String area;
    @ApiModelProperty(value = "收货地址")
    private String addr;

    @ApiModelProperty(value = "平台备注")
    private String platformRemarks;

    /**
     * 平台运费减免金额
     */
    private Long platformFreeFreightAmount;

    /**
     * 用户订单地址Id
     */
    private Long orderAddrId;

    /**
     * 总值
     */
    private Long total;


    /**
     * 卖家备注
     */
    private String shopRemarks;

    /**
     * 支付方式 请参考枚举PayType
     */
    private Integer payType;

    /**
     * 订单关闭原因 1-超时未支付 2-退款关闭 4-买家取消 15-已通过货到付款交易
     */
    private Integer closeType;

    /**
     * 发货时间
     */
    private Date updateTime;

    /**
     * 付款时间
     */
    private Date payTime;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 完成时间
     */
    private Date finallyTime;

    /**
     * 结算时间
     */
    private Date settledTime;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 预售发货时间
     */
    private Date bookTime;

    /**
     * 是否已支付，1.已支付0.未支付
     */
    private Integer isPayed;

    /**
     * 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
     */
    private Integer deleteStatus;

    /**
     * 积分抵扣金额
     */
    private Long scoreAmount;

    /**
     * 会员折扣金额
     */
    private Long memberAmount;

    /**
     * 平台优惠券优惠金额
     */
    private Long platformCouponAmount;

    /**
     * 商家优惠券优惠金额
     */
    private Long shopCouponAmount;

    /**
     * 满减优惠金额
     */
    private Long discountAmount;

    /**
     * 触点号
     */
    @ApiModelProperty(value = "触点号")
    private String tentacleNo;

    /**
     * 分销关系 1分享关系 2服务关系 3自主下单 4代客下单
     */
    @ApiModelProperty(value = "分销关系 1分享关系 2服务关系 3自主下单 4代客下单")
    private Integer distributionRelation;

    /**
     * 分销佣金
     */
    @ApiModelProperty(value = "分销佣金")
    private Long distributionAmount;

    /**
     * 分销佣金状态 0待结算 1已结算 2已提现 3提现中
     */
    @ApiModelProperty(value = "分销佣金状态 0待结算 1已结算 2已提现 3提现中")
    private Integer distributionStatus;

    /**
     * 分销用户ID
     */
    @ApiModelProperty(value = "分销用户ID")
    private Long distributionUserId;

    @ApiModelProperty("分销用户名称")
    private String distributionUserName;

    @ApiModelProperty("分销用户手机")
    private String distributionUserMobile;

    @ApiModelProperty("分销用户工号")
    private String distributionUserNo;

    /**
     * 分销用户门店ID
     */
    @ApiModelProperty(value = "分销用户门店ID")
    private Long distributionStoreId;

    /**
     * 分销用户类型 1-导购 2-微客
     */
    @ApiModelProperty(value = "分销用户类型 1-导购 2-微客")
    private Integer distributionUserType;

    /**
     * 发展佣金
     */
    @ApiModelProperty(value = "发展佣金")
    private Long developingAmount;

    /**
     * 发展佣金状态 0待结算 1已结算 2已提现 3提现中
     */
    @ApiModelProperty(value = "发展佣金状态 0待结算 1已结算 2已提现 3提现中")
    private Integer developingStatus;

    /**
     * 发展用户ID
     */
    @ApiModelProperty(value = "发展用户ID")
    private Long developingUserId;

    @ApiModelProperty("发展用户名称")
    private String developingUserName;

    @ApiModelProperty("发展用户手机")
    private String developingUserMobile;

    @ApiModelProperty("发展用户工号")
    private String developingUserNo;

    /**
     * 发展用户门店ID
     */
    @ApiModelProperty(value = "发展用户门店ID")
    private Long developingStoreId;

    /**
     * 平台优惠金额
     */
    private Long platformAmount;

    /**
     * 优惠总额
     */
    private Long reduceAmount;

    /**
     * 店铺改价优惠金额
     */
    private Long shopChangeFreeAmount;

    /**
     * 运费减免金额
     */
    private Long freeFreightAmount;

    @ApiModelProperty("下单门店ID")
    private Long storeId;

    @ApiModelProperty("下单门店名称")
    private String storeName;

    @ApiModelProperty("下单门店编码")
    private String storeCode;

    /**
     * 发展佣金结算时间
     */
    private Date developingSettleTime;

    /**
     * 发展佣金提现时间
     */
    private Date developingWithdrawTime;

    /**
     * 分销佣金结算时间
     */
    private Date distributionSettleTime;

    /**
     * 分销佣金提现时间
     */
    private Date distributionWithdrawTime;

    private String traceId;

    /**
     * 联营分佣退款状态 0-正常 1-退款待处理 2-退款已处理
     */
    private Integer jointVentureRefundStatus;

    @ApiModelProperty(value = "订单来源 0普通订单 1直播订单 2视频号3.0订单 3 视频号4.0订单", required = true)
    private Integer orderSource;
    
    @ApiModelProperty(value = "订单支付方式")
    private Integer orderPayType;
    /**
     * 联营分佣状态
     */
    private Integer jointVentureCommissionStatus;

    @ApiModelProperty("是否发展订单 true是 false不是")
    private Boolean isDevelopingOrder;

    public Boolean getIsDevelopingOrder() {
        return isDevelopingOrder == null ? developingUserId > 0 : isDevelopingOrder;
    }

}
