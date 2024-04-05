package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
/**
 * 分销员身份信息表_对接第三方
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public class MallFxPersonIdentityThird extends BaseModel implements Serializable{
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
     * 分销员所在门店
     */
    private Integer shopid;

    /**
     * 分销人Id
     */
    private Long fxid;

    /**
     * 分销类型:0 导购,1 会员,2 合伙人
     */
    private Integer fxtype;

    /**
     * 益世会员申请Id
     */
    private String yishirequestid;

    /**
     * 纳税信息审核状态  0 待申请 10 待审核  20 审核成功  30 审核失败
     */
    private Integer taxauditstatus;

    /**
     * 身份信息审核状态（线下转账）  0 待申请 10 待审核  20 审核成功  30 审核失败
     */
    private Integer identityauditstatus;

    /**
     * 审核失败原因
     */
    private String identityauditfailure;

    /**
     * 最后一次提交信息时转账方式 0 益世灵活用工提交  5 线下转账提交
     */
    private Integer lasttradetype;

    /**
     * 审核失败原因
     */
    private String auditfailureremark;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 身份证号
     */
    private String cardid;

    /**
     * 身份证正面照片（国徽面）
     */
    private String cardpositive;

    /**
     * 身份证反面（人像面）
     */
    private String cardreverse;

    /**
     * 
     */
    private String provincenmae;

    /**
     * 省编号
     */
    private String provincecode;

    /**
     * 
     */
    private String cityname;

    /**
     * 市编号
     */
    private String citycode;

    /**
     * 
     */
    private String countyname;

    /**
     * 区编号
     */
    private String countycode;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 银行卡号
     */
    private String bankcardno;

    /**
     * 持卡人名称
     */
    private String cardholdername;

    /**
     * 最后更新时间
     */
    private Date lastmodifieddate;

    /**
     * 创建人
     */
    private String createuser;

    /**
     * 创建时间
     */
    private Date createdate;

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

	public Integer getShopid() {
		return shopid;
	}

	public void setShopid(Integer shopid) {
		this.shopid = shopid;
	}

	public Long getFxid() {
		return fxid;
	}

	public void setFxid(Long fxid) {
		this.fxid = fxid;
	}

	public Integer getFxtype() {
		return fxtype;
	}

	public void setFxtype(Integer fxtype) {
		this.fxtype = fxtype;
	}

	public String getYishirequestid() {
		return yishirequestid;
	}

	public void setYishirequestid(String yishirequestid) {
		this.yishirequestid = yishirequestid;
	}

	public Integer getTaxauditstatus() {
		return taxauditstatus;
	}

	public void setTaxauditstatus(Integer taxauditstatus) {
		this.taxauditstatus = taxauditstatus;
	}

	public Integer getIdentityauditstatus() {
		return identityauditstatus;
	}

	public void setIdentityauditstatus(Integer identityauditstatus) {
		this.identityauditstatus = identityauditstatus;
	}

	public String getIdentityauditfailure() {
		return identityauditfailure;
	}

	public void setIdentityauditfailure(String identityauditfailure) {
		this.identityauditfailure = identityauditfailure;
	}

	public Integer getLasttradetype() {
		return lasttradetype;
	}

	public void setLasttradetype(Integer lasttradetype) {
		this.lasttradetype = lasttradetype;
	}

	public String getAuditfailureremark() {
		return auditfailureremark;
	}

	public void setAuditfailureremark(String auditfailureremark) {
		this.auditfailureremark = auditfailureremark;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getCardpositive() {
		return cardpositive;
	}

	public void setCardpositive(String cardpositive) {
		this.cardpositive = cardpositive;
	}

	public String getCardreverse() {
		return cardreverse;
	}

	public void setCardreverse(String cardreverse) {
		this.cardreverse = cardreverse;
	}

	public String getProvincenmae() {
		return provincenmae;
	}

	public void setProvincenmae(String provincenmae) {
		this.provincenmae = provincenmae;
	}

	public String getProvincecode() {
		return provincecode;
	}

	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getCountyname() {
		return countyname;
	}

	public void setCountyname(String countyname) {
		this.countyname = countyname;
	}

	public String getCountycode() {
		return countycode;
	}

	public void setCountycode(String countycode) {
		this.countycode = countycode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBankcardno() {
		return bankcardno;
	}

	public void setBankcardno(String bankcardno) {
		this.bankcardno = bankcardno;
	}

	public String getCardholdername() {
		return cardholdername;
	}

	public void setCardholdername(String cardholdername) {
		this.cardholdername = cardholdername;
	}

	public Date getLastmodifieddate() {
		return lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
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

	@Override
	public String toString() {
		return "MallFxPersonIdentityThird{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",shopid=" + shopid +
				",fxid=" + fxid +
				",fxtype=" + fxtype +
				",yishirequestid=" + yishirequestid +
				",taxauditstatus=" + taxauditstatus +
				",identityauditstatus=" + identityauditstatus +
				",identityauditfailure=" + identityauditfailure +
				",lasttradetype=" + lasttradetype +
				",auditfailureremark=" + auditfailureremark +
				",realname=" + realname +
				",cardid=" + cardid +
				",cardpositive=" + cardpositive +
				",cardreverse=" + cardreverse +
				",provincenmae=" + provincenmae +
				",provincecode=" + provincecode +
				",cityname=" + cityname +
				",citycode=" + citycode +
				",countyname=" + countyname +
				",countycode=" + countycode +
				",address=" + address +
				",bankcardno=" + bankcardno +
				",cardholdername=" + cardholdername +
				",lastmodifieddate=" + lastmodifieddate +
				",createuser=" + createuser +
				",createdate=" + createdate +
				'}';
	}
}
