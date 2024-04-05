package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 客户_券_券明细
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:30
 */
public class CrmCouponList extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private Integer copid;

    /**
     * 
     */
    private Integer brandid;

    /**
     * 
     */
    private Integer coupongrpid;

    /**
     * 
     */
    private Integer giftbagid;

    /**
     * 券号
     */
    private String couponno;

    /**
     * 
     */
    private String couponname;

    /**
     * 
     */
    private String coupontype;

    /**
     * 
     */
    private Double couponvalue;

    /**
     * 
     */
    private Double couponprice;

    /**
     * 
     */
    private String validitytype;

    /**
     * 
     */
    private Date begdate;

    /**
     * 
     */
    private Date enddate;

    /**
     * 
     */
    private Integer enddateint;

    /**
     * 
     */
    private Date gendate;

    /**
     * 
     */
    private Integer genuserid;

    /**
     * 
     */
    private Integer genshopid;

    /**
     * 
     */
    private String actorigin;

    /**
     * 
     */
    private Integer actid;

    /**
     * 
     */
    private Long vipid;

    /**
     * 
     */
    private Date vipbinddate;

    /**
     * 
     */
    private Integer servicechannel;

    /**
     * 
     */
    private Integer status;

    /**
     * 
     */
    private Integer sellchannel;

    /**
     * 
     */
    private Date selldate;

    /**
     * 
     */
    private Long sellid;

    /**
     * 
     */
    private String sellno;

    /**
     * 
     */
    private Double sellmoney;

    /**
     * 
     */
    private Integer outsysid;

    /**
     * 驿业平台券是否已同步给制券方
     */
    private String unioncouponsend;

    /**
     * 
     */
    private Date lastmodifieddate;

    /**
     * 驿业平台券点击立即领取的时间
     */
    private Date unioncouponsenddate;

    /**
     * 发券方品牌ID
     */
    private Integer purchasebrandid;

    /**
     * 发券方采购单Id
     */
    private Integer purchaseorderid;

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

	public Integer getCoupongrpid() {
		return coupongrpid;
	}

	public void setCoupongrpid(Integer coupongrpid) {
		this.coupongrpid = coupongrpid;
	}

	public Integer getGiftbagid() {
		return giftbagid;
	}

	public void setGiftbagid(Integer giftbagid) {
		this.giftbagid = giftbagid;
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

	public Double getCouponprice() {
		return couponprice;
	}

	public void setCouponprice(Double couponprice) {
		this.couponprice = couponprice;
	}

	public String getValiditytype() {
		return validitytype;
	}

	public void setValiditytype(String validitytype) {
		this.validitytype = validitytype;
	}

	public Date getBegdate() {
		return begdate;
	}

	public void setBegdate(Date begdate) {
		this.begdate = begdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Integer getEnddateint() {
		return enddateint;
	}

	public void setEnddateint(Integer enddateint) {
		this.enddateint = enddateint;
	}

	public Date getGendate() {
		return gendate;
	}

	public void setGendate(Date gendate) {
		this.gendate = gendate;
	}

	public Integer getGenuserid() {
		return genuserid;
	}

	public void setGenuserid(Integer genuserid) {
		this.genuserid = genuserid;
	}

	public Integer getGenshopid() {
		return genshopid;
	}

	public void setGenshopid(Integer genshopid) {
		this.genshopid = genshopid;
	}

	public String getActorigin() {
		return actorigin;
	}

	public void setActorigin(String actorigin) {
		this.actorigin = actorigin;
	}

	public Integer getActid() {
		return actid;
	}

	public void setActid(Integer actid) {
		this.actid = actid;
	}

	public Long getVipid() {
		return vipid;
	}

	public void setVipid(Long vipid) {
		this.vipid = vipid;
	}

	public Date getVipbinddate() {
		return vipbinddate;
	}

	public void setVipbinddate(Date vipbinddate) {
		this.vipbinddate = vipbinddate;
	}

	public Integer getServicechannel() {
		return servicechannel;
	}

	public void setServicechannel(Integer servicechannel) {
		this.servicechannel = servicechannel;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSellchannel() {
		return sellchannel;
	}

	public void setSellchannel(Integer sellchannel) {
		this.sellchannel = sellchannel;
	}

	public Date getSelldate() {
		return selldate;
	}

	public void setSelldate(Date selldate) {
		this.selldate = selldate;
	}

	public Long getSellid() {
		return sellid;
	}

	public void setSellid(Long sellid) {
		this.sellid = sellid;
	}

	public String getSellno() {
		return sellno;
	}

	public void setSellno(String sellno) {
		this.sellno = sellno;
	}

	public Double getSellmoney() {
		return sellmoney;
	}

	public void setSellmoney(Double sellmoney) {
		this.sellmoney = sellmoney;
	}

	public Integer getOutsysid() {
		return outsysid;
	}

	public void setOutsysid(Integer outsysid) {
		this.outsysid = outsysid;
	}

	public String getUnioncouponsend() {
		return unioncouponsend;
	}

	public void setUnioncouponsend(String unioncouponsend) {
		this.unioncouponsend = unioncouponsend;
	}

	public Date getLastmodifieddate() {
		return lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	public Date getUnioncouponsenddate() {
		return unioncouponsenddate;
	}

	public void setUnioncouponsenddate(Date unioncouponsenddate) {
		this.unioncouponsenddate = unioncouponsenddate;
	}

	public Integer getPurchasebrandid() {
		return purchasebrandid;
	}

	public void setPurchasebrandid(Integer purchasebrandid) {
		this.purchasebrandid = purchasebrandid;
	}

	public Integer getPurchaseorderid() {
		return purchaseorderid;
	}

	public void setPurchaseorderid(Integer purchaseorderid) {
		this.purchaseorderid = purchaseorderid;
	}

	@Override
	public String toString() {
		return "CrmCouponList{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",coupongrpid=" + coupongrpid +
				",giftbagid=" + giftbagid +
				",couponno=" + couponno +
				",couponname=" + couponname +
				",coupontype=" + coupontype +
				",couponvalue=" + couponvalue +
				",couponprice=" + couponprice +
				",validitytype=" + validitytype +
				",begdate=" + begdate +
				",enddate=" + enddate +
				",enddateint=" + enddateint +
				",gendate=" + gendate +
				",genuserid=" + genuserid +
				",genshopid=" + genshopid +
				",actorigin=" + actorigin +
				",actid=" + actid +
				",vipid=" + vipid +
				",vipbinddate=" + vipbinddate +
				",servicechannel=" + servicechannel +
				",status=" + status +
				",sellchannel=" + sellchannel +
				",selldate=" + selldate +
				",sellid=" + sellid +
				",sellno=" + sellno +
				",sellmoney=" + sellmoney +
				",outsysid=" + outsysid +
				",unioncouponsend=" + unioncouponsend +
				",lastmodifieddate=" + lastmodifieddate +
				",unioncouponsenddate=" + unioncouponsenddate +
				",purchasebrandid=" + purchasebrandid +
				",purchaseorderid=" + purchaseorderid +
				'}';
	}
}
