package com.mall4j.cloud.common.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


/**
 * @author FrozenWatermelon
 */
public class OrderSearchDTO {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 下单代购ID
     */
    private Long buyStaffId;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("订单状态集合")
    private List<Integer> statusList;

    @ApiModelProperty("订单类型参考orderType ,0普通商品 1团购订单 2秒杀订单,3积分订单")
    private Integer orderType;

    @ApiModelProperty("是否已经支付，1：已经支付过，0：，没有支付过")
    private Integer isPayed;

    @ApiModelProperty("联营分佣状态 联营分佣状态 0-待分佣 1-完成分佣")
    private Integer jointVentureCommissionStatus;

    @ApiModelProperty("联营客户id")
    private Long jointVentureCommissionCustomerId;

    /**
     * 订购流水号
     */
    @ApiModelProperty("订单号")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("下单的时间范围:开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("下单的时间范围:结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("收货人姓名")
    private String consignee;

    @ApiModelProperty("收货人手机号")
    private String mobile;

    @ApiModelProperty("物流类型  1:快递 2:自提 3：无需快递 4：同城快递")
    private Integer deliveryType;

    @ApiModelProperty("订单退款状态参考refundStatus（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）")
    private Integer refundStatus;

    @ApiModelProperty("自提点名称")
    private String stationName;

    @ApiModelProperty("支付类型  1:积分支付 2:微信支付 3：支付宝支付")
    private Integer payType;

    @ApiModelProperty("是否已评论")
    private Integer isComm;

    @ApiModelProperty("订单来源 0普通订单 1直播订单")
    private Integer orderSource;
    /**
     * 来源id
     */
    private String sourceId;

    @ApiModelProperty("门店id")
    private Long storeId;

    @ApiModelProperty("门店id列表")
    private List<Long> storeIdList;

    @ApiModelProperty("导购查询值 姓名/工号/手机号")
    private String distributionSearchKey;

    @ApiModelProperty("分销发展订单类型 1-分销 2-发展")
    private Integer distributionType;

    @ApiModelProperty("分销用户id列表")
    private List<Long> distributionUserIdList;

    @ApiModelProperty("发展用户id列表")
    private List<Long> developingUserIdList;

    @ApiModelProperty("开始页")
    private Integer pageNum;


    @ApiModelProperty("每页大小")
    private Integer pageSize;

    @ApiModelProperty("支付的时间范围:开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payStartTime;

    @ApiModelProperty("支付的时间范围:结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payEndTime;

    @ApiModelProperty("查询关键字 同时查询商品名称和orderNumber")
    private String keyWord;
    @ApiModelProperty("微信端订单编号")
    private Long wechatOrderId;

    public Long getWechatOrderId() {
        return wechatOrderId;
    }

    public void setWechatOrderId(Long wechatOrderId) {
        this.wechatOrderId = wechatOrderId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Date getPayStartTime() {
        return payStartTime;
    }

    public void setPayStartTime(Date payStartTime) {
        this.payStartTime = payStartTime;
    }

    public Date getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(Date payEndTime) {
        this.payEndTime = payEndTime;
    }

    /**
     * 支付类型列表
     */
    private List<Integer> payTypeList;

    /**
     * es开始的索引
     */
    private Integer begin;

    /**
     * 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
     */
    private Integer deleteStatus;
    /**
     * 实际总值
     */
    private Long actualTotal;
    /** 实际总值区间，下线 */
    private Long actualTotalMin;
    /** 实际总值区间，上线 */
    private Long actualTotalMax;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Integer getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(Integer orderSource) {
        this.orderSource = orderSource;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getBuyStaffId() {
        return buyStaffId;
    }

    public void setBuyStaffId(Long buyStaffId) {
        this.buyStaffId = buyStaffId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(Integer isPayed) {
        this.isPayed = isPayed;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getIsComm() {
        return isComm;
    }

    public void setIsComm(Integer isComm) {
        this.isComm = isComm;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<Integer> getPayTypeList() {
        return payTypeList;
    }

    public void setPayTypeList(List<Integer> payTypeList) {
        this.payTypeList = payTypeList;
    }

    public Integer getBegin() {
        return begin;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Long getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Long actualTotal) {
        this.actualTotal = actualTotal;
    }

    public Long getActualTotalMin() {
        return actualTotalMin;
    }

    public void setActualTotalMin(Long actualTotalMin) {
        this.actualTotalMin = actualTotalMin;
    }

    public Long getActualTotalMax() {
        return actualTotalMax;
    }

    public void setActualTotalMax(Long actualTotalMax) {
        this.actualTotalMax = actualTotalMax;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getJointVentureCommissionStatus() {
        return jointVentureCommissionStatus;
    }

    public void setJointVentureCommissionStatus(Integer jointVentureCommissionStatus) {
        this.jointVentureCommissionStatus = jointVentureCommissionStatus;
    }

    public List<Long> getStoreIdList() {
        return storeIdList;
    }

    public void setStoreIdList(List<Long> storeIdList) {
        this.storeIdList = storeIdList;
    }

    public Long getJointVentureCommissionCustomerId() {
        return jointVentureCommissionCustomerId;
    }

    public void setJointVentureCommissionCustomerId(Long jointVentureCommissionCustomerId) {
        this.jointVentureCommissionCustomerId = jointVentureCommissionCustomerId;
    }

    public String getDistributionSearchKey() {
        return distributionSearchKey;
    }

    public void setDistributionSearchKey(String distributionSearchKey) {
        this.distributionSearchKey = distributionSearchKey;
    }

    public Integer getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(Integer distributionType) {
        this.distributionType = distributionType;
    }

    public List<Long> getDistributionUserIdList() {
        return distributionUserIdList;
    }

    public void setDistributionUserIdList(List<Long> distributionUserIdList) {
        this.distributionUserIdList = distributionUserIdList;
    }

    public List<Long> getDevelopingUserIdList() {
        return developingUserIdList;
    }

    public void setDevelopingUserIdList(List<Long> developingUserIdList) {
        this.developingUserIdList = developingUserIdList;
    }

    @Override
    public String toString() {
        return "OrderSearchDTO{" +
                "userId=" + userId +
                ", shopId=" + shopId +
                ", buyStaffId=" + buyStaffId +
                ", status=" + status +
                ", statusList=" + statusList +
                ", orderType=" + orderType +
                ", isPayed=" + isPayed +
                ", jointVentureCommissionStatus=" + jointVentureCommissionStatus +
                ", jointVentureCommissionCustomerId=" + jointVentureCommissionCustomerId +
                ", orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", shopName='" + shopName + '\'' +
                ", spuName='" + spuName + '\'' +
                ", consignee='" + consignee + '\'' +
                ", mobile='" + mobile + '\'' +
                ", deliveryType=" + deliveryType +
                ", refundStatus=" + refundStatus +
                ", stationName='" + stationName + '\'' +
                ", payType=" + payType +
                ", isComm=" + isComm +
                ", orderSource=" + orderSource +
                ", sourceId='" + sourceId + '\'' +
                ", storeId=" + storeId +
                ", storeIdList=" + storeIdList +
                ", distributionSearchKey='" + distributionSearchKey + '\'' +
                ", distributionType=" + distributionType +
                ", distributionUserIdList=" + distributionUserIdList +
                ", developingUserIdList=" + developingUserIdList +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", payStartTime=" + payStartTime +
                ", payEndTime=" + payEndTime +
                ", payTypeList=" + payTypeList +
                ", begin=" + begin +
                ", deleteStatus=" + deleteStatus +
                ", actualTotal=" + actualTotal +
                ", actualTotalMin=" + actualTotalMin +
                ", actualTotalMax=" + actualTotalMax +
                '}';
    }
}
