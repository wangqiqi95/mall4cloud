package com.mall4j.cloud.order.dto.app;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author FrozenWatermelon
 * @date 2021/3/9
 */
public class OrderRefundDeliveryDTO {

    @ApiModelProperty(value = "退款编号名称", required = true)
    @NotNull(message = "退款编号不能为空")
    private Long refundId;

    @ApiModelProperty(value = "物流公司ID", required = true)
    @NotNull
    private Long deliveryCompanyId;

    @ApiModelProperty(value = "物流公司名称", required = true)
    @NotEmpty
    private String deliveryName;

    @ApiModelProperty(value = "物流单号", required = true)
    @NotEmpty
    @Length(max = 50, message = "物流单号长度不能超过50")
    private String deliveryNo;

    @ApiModelProperty(value = "手机号码")
    @Length(max = 11, message = "手机号码长度不能超过11")
    private String mobile;

    @ApiModelProperty(value = "备注信息")
    @Length(max = 255, message = "备注信息长度不能超过255")
    private String senderRemarks;

    @ApiModelProperty(value = "图片举证")
    private String imgs;

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public Long getDeliveryCompanyId() {
        return deliveryCompanyId;
    }

    public void setDeliveryCompanyId(Long deliveryCompanyId) {
        this.deliveryCompanyId = deliveryCompanyId;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSenderRemarks() {
        return senderRemarks;
    }

    public void setSenderRemarks(String senderRemarks) {
        this.senderRemarks = senderRemarks;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    @Override
    public String toString() {
        return "OrderRefundDeliveryDTO{" +
                "refundId=" + refundId +
                ", deliveryCompanyId=" + deliveryCompanyId +
                ", deliveryName='" + deliveryName + '\'' +
                ", deliveryNo='" + deliveryNo + '\'' +
                ", mobile='" + mobile + '\'' +
                ", senderRemarks='" + senderRemarks + '\'' +
                ", imgs='" + imgs + '\'' +
                '}';
    }
}
