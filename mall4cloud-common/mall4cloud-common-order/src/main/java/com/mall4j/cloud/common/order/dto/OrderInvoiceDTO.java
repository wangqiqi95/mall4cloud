package com.mall4j.cloud.common.order.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author Pineapple
 * @date 2021/8/2 8:52
 */
public class OrderInvoiceDTO {
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

    @Override
    public String toString() {
        return "OrderInvoiceDTO{" +
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
                '}';
    }
}
