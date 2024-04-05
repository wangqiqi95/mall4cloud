package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表
 *
 * @author FrozenWatermelon
 * @date 2022-04-10 22:09:01
 */
public class MallSalesOrderRtMapping extends BaseModel implements Serializable{
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
     * 退货单Id
     */
    private Long returnid;

    /**
     * 
     */
    private Integer returnqty;

    /**
     * 商品条码
     */
    private String barcode;

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

	public Integer getReturnqty() {
		return returnqty;
	}

	public void setReturnqty(Integer returnqty) {
		this.returnqty = returnqty;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	@Override
	public String toString() {
		return "MallSalesOrderRtMapping{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",orderid=" + orderid +
				",orderdtlid=" + orderdtlid +
				",returnid=" + returnid +
				",returnqty=" + returnqty +
				",barcode=" + barcode +
				",createuser=" + createuser +
				",createdate=" + createdate +
				",lastmodifieduser=" + lastmodifieduser +
				",lastmodifieddate=" + lastmodifieddate +
				'}';
	}
}
