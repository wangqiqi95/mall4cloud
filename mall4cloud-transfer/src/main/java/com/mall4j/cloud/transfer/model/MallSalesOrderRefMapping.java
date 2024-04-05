package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public class MallSalesOrderRefMapping extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 系统编号
     */
    private Long id;

    /**
     * 所属集团
     */
    private Integer copid;

    /**
     * 所属品牌
     */
    private Integer brandid;

    /**
     * 订单主表ID
     */
    private Long orderid;

    /**
     * 订单明细ID
     */
    private Long orderdtlid;

    /**
     * 退货单Id,退货退款这个字段会有值
     */
    private Long returnid;

    /**
     * 退款单Id
     */
    private Long refundid;

    /**
     * 商品条码
     */
    private String barcode;

    /**
     * 退款数量
     */
    private Integer qty;

    /**
     * 退款类型，0未发货退款，1已发货退款（pc端已发货直接退款）
     */
    private Integer reftype;

    /**
     * 发货包裹编号（已发货商品直接退款，未发货商品该值为空）
     */
    private String packageno;

    /**
     * 创建人
     */
    private String createuser;

    /**
     * 创建时间
     */
    private Date createdate;

    /**
     * 修改人
     */
    private String lastmodifieduser;

    /**
     * 最后更新时间
     */
    private Date lastmodifieddate;

    /**
     * 应退金额
     */
    private Double refundmoney;

    /**
     * 实退金额
     */
    private Double amountmenory;

    /**
     * 尾款已退金额
     */
    private Double finalamount;

    /**
     * 定金已退金额
     */
    private Double depositamount;

    /**
     * 应退积分
     */
    private Integer oldrefundbonus;

    /**
     * 实退积分
     */
    private Integer refundbonus;

    /**
     * 实际退积分
     */
    private Integer useintegral;

    /**
     * 应退积分
     */
    private Integer olduseintegral;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCopid() {
		return copid;
	}

	public void setCopid(Integer copid) {
		this.copid = copid;
	}

	public Integer getBrandid() {
		return brandid;
	}

	public void setBrandid(Integer brandid) {
		this.brandid = brandid;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Long getOrderdtlid() {
		return orderdtlid;
	}

	public void setOrderdtlid(Long orderdtlid) {
		this.orderdtlid = orderdtlid;
	}

	public Long getReturnid() {
		return returnid;
	}

	public void setReturnid(Long returnid) {
		this.returnid = returnid;
	}

	public Long getRefundid() {
		return refundid;
	}

	public void setRefundid(Long refundid) {
		this.refundid = refundid;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Integer getReftype() {
		return reftype;
	}

	public void setReftype(Integer reftype) {
		this.reftype = reftype;
	}

	public String getPackageno() {
		return packageno;
	}

	public void setPackageno(String packageno) {
		this.packageno = packageno;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getLastmodifieduser() {
		return lastmodifieduser;
	}

	public void setLastmodifieduser(String lastmodifieduser) {
		this.lastmodifieduser = lastmodifieduser;
	}

	public Date getLastmodifieddate() {
		return lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	public Double getRefundmoney() {
		return refundmoney;
	}

	public void setRefundmoney(Double refundmoney) {
		this.refundmoney = refundmoney;
	}

	public Double getAmountmenory() {
		return amountmenory;
	}

	public void setAmountmenory(Double amountmenory) {
		this.amountmenory = amountmenory;
	}

	public Double getFinalamount() {
		return finalamount;
	}

	public void setFinalamount(Double finalamount) {
		this.finalamount = finalamount;
	}

	public Double getDepositamount() {
		return depositamount;
	}

	public void setDepositamount(Double depositamount) {
		this.depositamount = depositamount;
	}

	public Integer getOldrefundbonus() {
		return oldrefundbonus;
	}

	public void setOldrefundbonus(Integer oldrefundbonus) {
		this.oldrefundbonus = oldrefundbonus;
	}

	public Integer getRefundbonus() {
		return refundbonus;
	}

	public void setRefundbonus(Integer refundbonus) {
		this.refundbonus = refundbonus;
	}

	public Integer getUseintegral() {
		return useintegral;
	}

	public void setUseintegral(Integer useintegral) {
		this.useintegral = useintegral;
	}

	public Integer getOlduseintegral() {
		return olduseintegral;
	}

	public void setOlduseintegral(Integer olduseintegral) {
		this.olduseintegral = olduseintegral;
	}

	@Override
	public String toString() {
		return "MallSalesOrderRefMapping{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",orderid=" + orderid +
				",orderdtlid=" + orderdtlid +
				",returnid=" + returnid +
				",refundid=" + refundid +
				",barcode=" + barcode +
				",qty=" + qty +
				",reftype=" + reftype +
				",packageno=" + packageno +
				",createuser=" + createuser +
				",createdate=" + createdate +
				",lastmodifieduser=" + lastmodifieduser +
				",lastmodifieddate=" + lastmodifieddate +
				",refundmoney=" + refundmoney +
				",amountmenory=" + amountmenory +
				",finalamount=" + finalamount +
				",depositamount=" + depositamount +
				",oldrefundbonus=" + oldrefundbonus +
				",refundbonus=" + refundbonus +
				",useintegral=" + useintegral +
				",olduseintegral=" + olduseintegral +
				'}';
	}
}
