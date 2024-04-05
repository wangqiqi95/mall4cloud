package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public class MallFxPerson extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键Id,分销人Id
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
     * 分销类型:0 导购;1 会员;2 合伙人
     */
    private Integer fxtype;

    /**
     * 门店Id:分销类型为导购对应导购所在的门店，如果为合伙人，则为合伙人所在的门店,会员则为0
     */
    private Integer shopid;

    /**
     * 分销等级Id
     */
    private Integer fxlevel;

    /**
     * 导购Id
     */
    private Integer sgid;

    /**
     * 导购登录Id，导购改手机号重新注册分销员账号
     */
    private Integer sgloginid;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 会员UnionId
     */
    private String wxunionid;

    /**
     * 上级分销员Id
     */
    private Long parentid;

    /**
     * 状态：分销类型为会员、合伙人  0：申请中 1：已同意  2：已拒绝
     */
    private Integer status;

    /**
     * 账户总金额
     */
    private Double totalamount;

    /**
     * 待入账金额
     */
    private Double waitamount;

    /**
     * 剩余金额
     */
    private Double remainamount;

    /**
     * 提现占用金额
     */
    private Double useamount;

    /**
     * 账户变更时间
     */
    private Date accountchangedate;

    /**
     * 是否启用禁用
     */
    private String isenable;

    /**
     * 创建时间
     */
    private Date createdate;

    /**
     * 最后更新时间
     */
    private Date lastmodifieddate;

    /**
     * -1:未知；0：导购；1：微客
     */
    private Integer invitefxpersontype;

    /**
     * 发展活动Id
     */
    private Integer developactid;

    /**
     * 导购是否已经加会员微信
     */
    private String isaddguidewechat;

    /**
     *  分销员身份信息状态   0 待完善 1 待审核 3 已拒绝 4 以通过 
     */
    private Integer identityinforstatus;

    /**
     * 是否是系统设置禁用
     */
    private String issyssetdisable;

    /**
     * 是否分销导购
     */
    private String isretailsaler;

    /**
     * 是否被拉黑
     */
    private String blacklisted;

    /**
     * 下级人数
     */
    private Integer subcount;

    /**
     * 新增下级微客数
     */
    private Integer addsubwkcount;

    /**
     * 启用分销社群运营 true 启用 false 不启用
     */
    private String isfxcommunityoperation;

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

	public Integer getFxtype() {
		return fxtype;
	}

	public void setFxtype(Integer fxtype) {
		this.fxtype = fxtype;
	}

	public Integer getShopid() {
		return shopid;
	}

	public void setShopid(Integer shopid) {
		this.shopid = shopid;
	}

	public Integer getFxlevel() {
		return fxlevel;
	}

	public void setFxlevel(Integer fxlevel) {
		this.fxlevel = fxlevel;
	}

	public Integer getSgid() {
		return sgid;
	}

	public void setSgid(Integer sgid) {
		this.sgid = sgid;
	}

	public Integer getSgloginid() {
		return sgloginid;
	}

	public void setSgloginid(Integer sgloginid) {
		this.sgloginid = sgloginid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWxunionid() {
		return wxunionid;
	}

	public void setWxunionid(String wxunionid) {
		this.wxunionid = wxunionid;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(Double totalamount) {
		this.totalamount = totalamount;
	}

	public Double getWaitamount() {
		return waitamount;
	}

	public void setWaitamount(Double waitamount) {
		this.waitamount = waitamount;
	}

	public Double getRemainamount() {
		return remainamount;
	}

	public void setRemainamount(Double remainamount) {
		this.remainamount = remainamount;
	}

	public Double getUseamount() {
		return useamount;
	}

	public void setUseamount(Double useamount) {
		this.useamount = useamount;
	}

	public Date getAccountchangedate() {
		return accountchangedate;
	}

	public void setAccountchangedate(Date accountchangedate) {
		this.accountchangedate = accountchangedate;
	}

	public String getIsenable() {
		return isenable;
	}

	public void setIsenable(String isenable) {
		this.isenable = isenable;
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

	public Integer getInvitefxpersontype() {
		return invitefxpersontype;
	}

	public void setInvitefxpersontype(Integer invitefxpersontype) {
		this.invitefxpersontype = invitefxpersontype;
	}

	public Integer getDevelopactid() {
		return developactid;
	}

	public void setDevelopactid(Integer developactid) {
		this.developactid = developactid;
	}

	public String getIsaddguidewechat() {
		return isaddguidewechat;
	}

	public void setIsaddguidewechat(String isaddguidewechat) {
		this.isaddguidewechat = isaddguidewechat;
	}

	public Integer getIdentityinforstatus() {
		return identityinforstatus;
	}

	public void setIdentityinforstatus(Integer identityinforstatus) {
		this.identityinforstatus = identityinforstatus;
	}

	public String getIssyssetdisable() {
		return issyssetdisable;
	}

	public void setIssyssetdisable(String issyssetdisable) {
		this.issyssetdisable = issyssetdisable;
	}

	public String getIsretailsaler() {
		return isretailsaler;
	}

	public void setIsretailsaler(String isretailsaler) {
		this.isretailsaler = isretailsaler;
	}

	public String getBlacklisted() {
		return blacklisted;
	}

	public void setBlacklisted(String blacklisted) {
		this.blacklisted = blacklisted;
	}

	public Integer getSubcount() {
		return subcount;
	}

	public void setSubcount(Integer subcount) {
		this.subcount = subcount;
	}

	public Integer getAddsubwkcount() {
		return addsubwkcount;
	}

	public void setAddsubwkcount(Integer addsubwkcount) {
		this.addsubwkcount = addsubwkcount;
	}

	public String getIsfxcommunityoperation() {
		return isfxcommunityoperation;
	}

	public void setIsfxcommunityoperation(String isfxcommunityoperation) {
		this.isfxcommunityoperation = isfxcommunityoperation;
	}

	@Override
	public String toString() {
		return "MallFxPerson{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",fxtype=" + fxtype +
				",shopid=" + shopid +
				",fxlevel=" + fxlevel +
				",sgid=" + sgid +
				",sgloginid=" + sgloginid +
				",phone=" + phone +
				",wxunionid=" + wxunionid +
				",parentid=" + parentid +
				",status=" + status +
				",totalamount=" + totalamount +
				",waitamount=" + waitamount +
				",remainamount=" + remainamount +
				",useamount=" + useamount +
				",accountchangedate=" + accountchangedate +
				",isenable=" + isenable +
				",createdate=" + createdate +
				",lastmodifieddate=" + lastmodifieddate +
				",invitefxpersontype=" + invitefxpersontype +
				",developactid=" + developactid +
				",isaddguidewechat=" + isaddguidewechat +
				",identityinforstatus=" + identityinforstatus +
				",issyssetdisable=" + issyssetdisable +
				",isretailsaler=" + isretailsaler +
				",blacklisted=" + blacklisted +
				",subcount=" + subcount +
				",addsubwkcount=" + addsubwkcount +
				",isfxcommunityoperation=" + isfxcommunityoperation +
				'}';
	}
}
