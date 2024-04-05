package com.mall4j.cloud.order.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author Pineapple
 * @date 2021/8/2 8:50
 */
public class OrderInvoiceVO extends BaseVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单发票ID")
    private Long orderInvoiceId;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("发票类型 1.电子普通发票")
    private Integer invoiceType;

    @ApiModelProperty("抬头类型 1.单位 2.个人")
    private Integer headerType;

    @ApiModelProperty("抬头名称")
    private String headerName;

    @ApiModelProperty("发票税号")
    private String invoiceTaxNumber;

    @ApiModelProperty("发票内容 1.商品明细")
    private Integer invoiceContext;

    @ApiModelProperty("发票状态 1.申请中 2.已开票")
    private Integer invoiceState;

    @ApiModelProperty("文件id")
    private Long fileId;

    @ApiModelProperty("申请时间")
    private Date applicationTime;

    @ApiModelProperty("上传时间")
    private Date uploadTime;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("订单项列表")
    private List<MyOrderItemVO> orderItemVOList;

    @ApiModelProperty("商品总数")
    private Integer count;

    @ApiModelProperty("商品总金额")
    private Long amount;

    @ApiModelProperty("订单状态 1:待付款 2:待发货(待自提) 3:待收货(已发货) 5:成功 6:失败 7:待成团")
    private Integer orderState;

    @ApiModelProperty("订单创建时间")
    private Date orderCreateTime;

    @ApiModelProperty("用户id")
    private Long userId;

    public Long getOrderInvoiceId() {
        return orderInvoiceId;
    }

    public void setOrderInvoiceId(Long orderInvoiceId) {
        this.orderInvoiceId = orderInvoiceId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Integer getHeaderType() {
        return headerType;
    }

    public void setHeaderType(Integer headerType) {
        this.headerType = headerType;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getInvoiceTaxNumber() {
        return invoiceTaxNumber;
    }

    public void setInvoiceTaxNumber(String invoiceTaxNumber) {
        this.invoiceTaxNumber = invoiceTaxNumber;
    }

    public Integer getInvoiceContext() {
        return invoiceContext;
    }

    public void setInvoiceContext(Integer invoiceContext) {
        this.invoiceContext = invoiceContext;
    }

    public Integer getInvoiceState() {
        return invoiceState;
    }

    public void setInvoiceState(Integer invoiceState) {
        this.invoiceState = invoiceState;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Date getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<MyOrderItemVO> getOrderItemVOList() {
        return orderItemVOList;
    }

    public void setOrderItemVOList(List<MyOrderItemVO> orderItemVOList) {
        this.orderItemVOList = orderItemVOList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    @Override
    public String toString() {
        return "OrderInvoiceVO{" +
                "orderInvoiceId=" + orderInvoiceId +
                ", orderId=" + orderId +
                ", invoiceType=" + invoiceType +
                ", headerType=" + headerType +
                ", headerName='" + headerName + '\'' +
                ", invoiceTaxNumber=" + invoiceTaxNumber +
                ", invoiceContext=" + invoiceContext +
                ", invoiceState=" + invoiceState +
                ", fileId=" + fileId +
                ", applicationTime=" + applicationTime +
                ", uploadTime=" + uploadTime +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", orderItemVOList=" + orderItemVOList +
                ", count=" + count +
                ", amount=" + amount +
                ", orderState=" + orderState +
                ", orderCreateTime=" + orderCreateTime +
                ", userId=" + userId +
                '}';
    }
}
