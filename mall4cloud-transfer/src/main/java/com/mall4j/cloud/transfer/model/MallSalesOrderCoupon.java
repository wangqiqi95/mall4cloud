package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 订单销售电子券记录
 *
 * @author FrozenWatermelon
 * @date 2022-04-12 15:28:02
 */
public class MallSalesOrderCoupon implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 编号
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
     * 券编号
     */
    private Long couponid;

    /**
     * 券号
     */
    private String couponno;

    /**
     * 券名称
     */
    private String couponname;

    /**
     * 券类型
     */
    private String coupontype;

    /**
     * 券面额
     */
    private Double couponvalue;

    /**
     * 第三方券的券门槛
     */
    private Double thirdcouponpricelimit;

    /**
     * 订单编号
     */
    private Long orderid;

    /**
     * 会员编号
     */
    private Long vipid;

    /**
     * 是否商品抵扣
     */
    private String ispro;

    /**
     * 0:准备1：完成
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createdate;

    /**
     * 最后修改时间
     */
    private Date lastmodifieddate;

    /**
     * 券来源id
     */
    private Integer couponoriginid;

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

	public Long getCouponid() {
		return couponid;
	}

	public void setCouponid(Long couponid) {
		this.couponid = couponid;
	}

	public String getCouponno() {
		return couponno;
	}

	public void setCouponno(String couponno) {
		this.couponno = couponno;
	}

	public String getCouponname() {
		return couponname;
	}

	public void setCouponname(String couponname) {
		this.couponname = couponname;
	}

	public String getCoupontype() {
		return coupontype;
	}

	public void setCoupontype(String coupontype) {
		this.coupontype = coupontype;
	}

	public Double getCouponvalue() {
		return couponvalue;
	}

	public void setCouponvalue(Double couponvalue) {
		this.couponvalue = couponvalue;
	}

	public Double getThirdcouponpricelimit() {
		return thirdcouponpricelimit;
	}

	public void setThirdcouponpricelimit(Double thirdcouponpricelimit) {
		this.thirdcouponpricelimit = thirdcouponpricelimit;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Long getVipid() {
		return vipid;
	}

	public void setVipid(Long vipid) {
		this.vipid = vipid;
	}

	public String getIspro() {
		return ispro;
	}

	public void setIspro(String ispro) {
		this.ispro = ispro;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getLastmodifieddate() {
		return lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	public Integer getCouponoriginid() {
		return couponoriginid;
	}

	public void setCouponoriginid(Integer couponoriginid) {
		this.couponoriginid = couponoriginid;
	}

	@Override
	public String toString() {
		return "MallSalesOrderCoupon{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",couponid=" + couponid +
				",couponno=" + couponno +
				",couponname=" + couponname +
				",coupontype=" + coupontype +
				",couponvalue=" + couponvalue +
				",thirdcouponpricelimit=" + thirdcouponpricelimit +
				",orderid=" + orderid +
				",vipid=" + vipid +
				",ispro=" + ispro +
				",status=" + status +
				",createdate=" + createdate +
				",lastmodifieddate=" + lastmodifieddate +
				",couponoriginid=" + couponoriginid +
				'}';
	}
}
