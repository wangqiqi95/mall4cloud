package com.mall4j.cloud.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 待发货订单信息
 * @author Pineapple
 * @date 2021/7/19 9:28
 */
public class UnDeliveryOrderExcelVO {
    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "待发货订单信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {0,1,2,3,4,5,6,7};

    @ExcelProperty(value = {"订单信息", "序号"}, index = 0)
    private String seq;

    @ExcelProperty(value = {"订单信息", "订单ID"}, index = 1)
    private String orderId;

    @ExcelProperty(value = {"物流信息", "配送方式*"}, index = 2)
    private String deliveryType;

    @ExcelProperty(value = {"物流信息", "快递公司名称*"}, index = 3)
    private String deliveryCompanyName;

    @ExcelProperty(value = {"物流信息","快递单号*"}, index = 4)
    private String deliveryNo;

    @ExcelProperty(value = {"物流信息", "收货人姓名"}, index = 5)
    private String consignee;

    @ExcelProperty(value = {"物流信息", "收货人手机"}, index = 6)
    private String mobile;

    @ExcelProperty(value = {"物流信息", "收货地址"}, index = 7)
    private String receivingAddr;

    @ExcelProperty(value = {"订单项信息", "商品名称"})
    private String spuName;

    @ExcelProperty(value = {"订单项信息", "sku名称"})
    private String skuName;

    @ExcelProperty(value = {"订单项信息", "商品待发货数量"})
    private Integer count;

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryCompanyName() {
        return deliveryCompanyName;
    }

    public void setDeliveryCompanyName(String deliveryCompanyName) {
        this.deliveryCompanyName = deliveryCompanyName;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
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

    public String getReceivingAddr() {
        return receivingAddr;
    }

    public void setReceivingAddr(String receivingAddr) {
        this.receivingAddr = receivingAddr;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "UnDeliveryOrderExcelVO{" +
                "seq='" + seq + '\'' +
                ", orderId=" + orderId +
                ", deliveryType='" + deliveryType + '\'' +
                ", deliveryCompanyName='" + deliveryCompanyName + '\'' +
                ", deliveryNo='" + deliveryNo + '\'' +
                ", consignee='" + consignee + '\'' +
                ", mobile='" + mobile + '\'' +
                ", receivingAddr='" + receivingAddr + '\'' +
                ", spuName='" + spuName + '\'' +
                ", skuName='" + skuName + '\'' +
                ", count=" + count +
                '}';
    }
}
