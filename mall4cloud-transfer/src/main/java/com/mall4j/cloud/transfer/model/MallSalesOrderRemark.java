package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
/**
 * 商城_订单_官网订单信息_退货信息_退款信息备注信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-07 23:11:22
 */
public class MallSalesOrderRemark extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 集团Id
     */
    private Integer copid;

    /**
     * 品牌Id
     */
    private Integer brandid;

    /**
     * 订单Id
     */
    private Long orderid;

    /**
     * 填写订单备注人员描述
     */
    private String remarkdescription;

    /**
     * 
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createdate;

    /**
     * 订单备注类型（1订单管理2退货管理3退款管理）
     */
    private Integer orderremarktype;

    /**
     * 订单、退货单、退款单ID
     */
    private Long tableid;

    /**
     * 插旗类型
     */
    private Integer flagtype;

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

	public String getRemarkdescription() {
		return remarkdescription;
	}

	public void setRemarkdescription(String remarkdescription) {
		this.remarkdescription = remarkdescription;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Integer getOrderremarktype() {
		return orderremarktype;
	}

	public void setOrderremarktype(Integer orderremarktype) {
		this.orderremarktype = orderremarktype;
	}

	public Long getTableid() {
		return tableid;
	}

	public void setTableid(Long tableid) {
		this.tableid = tableid;
	}

	public Integer getFlagtype() {
		return flagtype;
	}

	public void setFlagtype(Integer flagtype) {
		this.flagtype = flagtype;
	}

	@Override
	public String toString() {
		return "MallSalesOrderRemark{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",orderid=" + orderid +
				",remarkdescription=" + remarkdescription +
				",remark=" + remark +
				",createdate=" + createdate +
				",orderremarktype=" + orderremarktype +
				",tableid=" + tableid +
				",flagtype=" + flagtype +
				'}';
	}
}
