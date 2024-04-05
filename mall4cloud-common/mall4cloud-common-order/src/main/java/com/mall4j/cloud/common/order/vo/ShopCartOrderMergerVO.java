package com.mall4j.cloud.common.order.vo;

import com.mall4j.cloud.api.order.dto.OrderActivityDTO;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.dto.OrderInvoiceDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 多个店铺订单合并在一起的合并类
 * "/confirm" 使用
 * @author FrozenWatermelon
 */
@Data
public class ShopCartOrderMergerVO {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户地址")
    private UserAddrVO userAddr;

    @ApiModelProperty(value = "自提信息Dto")
    private OrderSelfStationVO orderSelfStation;

    @ApiModelProperty(value = "实际总值", required = true)
    private Long actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Long total;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer totalCount;

    @ApiModelProperty(value = "订单优惠金额(所有店铺优惠金额和使用积分抵现相加)", required = true)
    private Long orderReduce;

    @ApiModelProperty(value = "订单店铺优惠金额(所有店铺优惠金额)", required = true)
    private Long orderShopReduce;

    @ApiModelProperty(value = "用户拥有的积分数量", required = true)
    private Long userHasScore;

    @ApiModelProperty(value = "用户使用积分数量", required = true)
    private Long usableScore;

    @ApiModelProperty(value = "整个订单最多可以使用的积分数", required = true)
    private Long maxUsableScore;

    @ApiModelProperty(value = "积分抵扣金额", required = true)
    private Long totalScoreAmount;

    @ApiModelProperty(value = "购物积分抵现比例(x积分抵扣1元）", required = true)
    private Long shoppingUseScoreRatio;

    @ApiModelProperty(value = "等级折扣金额", required = true)
    private Long totalLevelAmount;

    @ApiModelProperty(value = "免运费金额", required = true)
    private Long freeTransfee;

    @ApiModelProperty(value = "总运费", required = true)
    private Long totalTransfee;

    @ApiModelProperty(value = "用户是否选择积分抵现(0不使用 1使用 默认不使用)")
    private Integer isScorePay;

    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递")
    private Integer dvyType;

    @ApiModelProperty(value = "用户选择的自提id")
    private Long stationId;

    @ApiModelProperty(value = "同城配送可用状态 : 1 可用 -1 不在范围内 -2 商家没有配置同城配送信息 -3 起送费不够", required = true)
    private Integer shopCityStatus;

    @ApiModelProperty(value = "过滤掉的商品项", required = true)
    private List<ShopCartItemVO> filterShopItems;

    @ApiModelProperty(value = "每个店铺的订单信息", required = true)
    private List<ShopCartOrderVO> shopCartOrders;

    @ApiModelProperty(value = "平台订单可以使用的优惠券列表", required = true)
    private List<CouponOrderVO> coupons;

    @ApiModelProperty(value = "秒杀skuId")
    private Long seckillSkuId;

    @ApiModelProperty(value = "秒杀id")
    private Long seckillId;

    @ApiModelProperty(value = "拼团skuId")
    private Long groupSkuId;

    @ApiModelProperty(value = "拼团id")
    private Long groupActivityId;

    @ApiModelProperty(value = "订单发票")
    private List<OrderInvoiceDTO> orderInvoiceList;

    @ApiModelProperty(value = "订单类型")
    private OrderType orderType;

    @ApiModelProperty("下单代购ID")
    private Long buyStaffId;

    @ApiModelProperty(value = "触点号")
    private String tentacleNo;

    @ApiModelProperty(value = "下单门店")
    private Long storeId;

    @ApiModelProperty(value = "订单优惠券类型")
    private OrderActivityDTO activityDTO;
    @ApiModelProperty(value = "订单来源")
    private Integer orderSource;
    @ApiModelProperty(value = "来源id")
    private String sourceId;
    @ApiModelProperty(value = "视频号,跟踪ID，有效期十分钟，会影响主播归因、分享员归因等，需创建订单前调用，调用生成订单 api 时需传入该参数")
    private String traceId;
    @ApiModelProperty(value = "微信侧订单id")
    private Long wechatOrderId;
    @ApiModelProperty(value = "支付方式 (0积分支付 1:微信小程序支付 2:支付宝 3微信扫码支付 4 微信h5支付 5微信公众号支付 6支付宝H5支付 7支付宝APP支付 8微信APP支付 9余额支付)", required = true)
    private Integer payType;

    @ApiModelProperty("赠品集合")
    private List<OrderGiftInfoVO> giftInfoAppVOList;

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getOrderShopReduce() {
        return orderShopReduce;
    }

    public void setOrderShopReduce(Long orderShopReduce) {
        this.orderShopReduce = orderShopReduce;
    }

    public OrderSelfStationVO getOrderSelfStation() {
        return orderSelfStation;
    }

    public void setOrderSelfStation(OrderSelfStationVO orderSelfStation) {
        this.orderSelfStation = orderSelfStation;
    }

    public Long getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Long actualTotal) {
        this.actualTotal = actualTotal;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Long getOrderReduce() {
        return orderReduce;
    }

    public void setOrderReduce(Long orderReduce) {
        this.orderReduce = orderReduce;
    }

    public Long getMaxUsableScore() {
        return maxUsableScore;
    }

    public void setMaxUsableScore(Long maxUsableScore) {
        this.maxUsableScore = maxUsableScore;
    }

    public Long getTotalScoreAmount() {
        return totalScoreAmount;
    }

    public void setTotalScoreAmount(Long totalScoreAmount) {
        this.totalScoreAmount = totalScoreAmount;
    }

    public Long getTotalLevelAmount() {
        return totalLevelAmount;
    }

    public void setTotalLevelAmount(Long totalLevelAmount) {
        this.totalLevelAmount = totalLevelAmount;
    }

    public Long getFreeTransfee() {
        return freeTransfee;
    }

    public void setFreeTransfee(Long freeTransfee) {
        this.freeTransfee = freeTransfee;
    }

    public Long getTotalTransfee() {
        return totalTransfee;
    }

    public void setTotalTransfee(Long totalTransfee) {
        this.totalTransfee = totalTransfee;
    }

    public Integer getIsScorePay() {
        return isScorePay;
    }

    public void setIsScorePay(Integer isScorePay) {
        this.isScorePay = isScorePay;
    }

    public Integer getDvyType() {
        return dvyType;
    }

    public void setDvyType(Integer dvyType) {
        this.dvyType = dvyType;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Integer getShopCityStatus() {
        return shopCityStatus;
    }

    public void setShopCityStatus(Integer shopCityStatus) {
        this.shopCityStatus = shopCityStatus;
    }

    public List<ShopCartOrderVO> getShopCartOrders() {
        return shopCartOrders;
    }

    public void setShopCartOrders(List<ShopCartOrderVO> shopCartOrders) {
        this.shopCartOrders = shopCartOrders;
    }

    public UserAddrVO getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(UserAddrVO userAddr) {
        this.userAddr = userAddr;
    }

    public List<ShopCartItemVO> getFilterShopItems() {
        return filterShopItems;
    }

    public void setFilterShopItems(List<ShopCartItemVO> filterShopItems) {
        this.filterShopItems = filterShopItems;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public List<CouponOrderVO> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponOrderVO> coupons) {
        this.coupons = coupons;
    }

    public Long getSeckillSkuId() {
        return seckillSkuId;
    }

    public void setSeckillSkuId(Long seckillSkuId) {
        this.seckillSkuId = seckillSkuId;
    }

    public Long getUserHasScore() {
        return userHasScore;
    }

    public void setUserHasScore(Long userHasScore) {
        this.userHasScore = userHasScore;
    }

    public Long getUsableScore() {
        return usableScore;
    }

    public void setUsableScore(Long usableScore) {
        this.usableScore = usableScore;
    }

    public Long getShoppingUseScoreRatio() {
        return shoppingUseScoreRatio;
    }

    public void setShoppingUseScoreRatio(Long shoppingUseScoreRatio) {
        this.shoppingUseScoreRatio = shoppingUseScoreRatio;
    }

    public List<OrderInvoiceDTO> getOrderInvoiceList() {
        return orderInvoiceList;
    }

    public void setOrderInvoiceList(List<OrderInvoiceDTO> orderInvoiceList) {
        this.orderInvoiceList = orderInvoiceList;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Long getBuyStaffId() {
        return buyStaffId;
    }

    public void setBuyStaffId(Long buyStaffId) {
        this.buyStaffId = buyStaffId;
    }

    public String getTentacleNo() {
        return tentacleNo;
    }

    public void setTentacleNo(String tentacleNo) {
        this.tentacleNo = tentacleNo;
    }

    public Long getGroupSkuId() {
        return groupSkuId;
    }

    public void setGroupSkuId(Long groupSkuId) {
        this.groupSkuId = groupSkuId;
    }

    public Long getGroupActivityId() {
        return groupActivityId;
    }

    public void setGroupActivityId(Long groupActivityId) {
        this.groupActivityId = groupActivityId;
    }

    @Override
    public String toString() {
        return "ShopCartOrderMergerVO{" +
                "userId=" + userId +
                ", userAddr=" + userAddr +
                ", orderSelfStation=" + orderSelfStation +
                ", actualTotal=" + actualTotal +
                ", total=" + total +
                ", totalCount=" + totalCount +
                ", orderReduce=" + orderReduce +
                ", orderShopReduce=" + orderShopReduce +
                ", userHasScore=" + userHasScore +
                ", usableScore=" + usableScore +
                ", maxUsableScore=" + maxUsableScore +
                ", totalScoreAmount=" + totalScoreAmount +
                ", shoppingUseScoreRatio=" + shoppingUseScoreRatio +
                ", totalLevelAmount=" + totalLevelAmount +
                ", freeTransfee=" + freeTransfee +
                ", totalTransfee=" + totalTransfee +
                ", isScorePay=" + isScorePay +
                ", dvyType=" + dvyType +
                ", stationId=" + stationId +
                ", shopCityStatus=" + shopCityStatus +
                ", filterShopItems=" + filterShopItems +
                ", shopCartOrders=" + shopCartOrders +
                ", coupons=" + coupons +
                ", seckillSkuId=" + seckillSkuId +
                ", seckillId=" + seckillId +
                ", orderInvoiceList=" + orderInvoiceList +
                ", orderType=" + orderType +
                ", buyStaffId=" + buyStaffId +
                ", tentacleNo='" + tentacleNo + '\'' +
                '}';
    }
}
