package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 客户_券_券库信息

维护某一类型券的库公用信息
类型:代金券、折扣券、礼品券、邀请
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:29
 */
public class CrmCouponGrp extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 系统编号
     */
    private Integer id;

    /**
     * 所属集团
     */
    private Integer copid;

    /**
     * 所属品牌
     */
    private Integer brandid;

    /**
     * 模板券库编号
     */
    private Integer tplid;

    /**
     * 模板券库名
     */
    private String tplname;

    /**
     * 分享图片
     */
    private String sharepic;

    /**
     * 适用系统
     */
    private String syscode;

    /**
     * 积分适用系统
     */
    private Integer outsysid;

    /**
     * 适用品牌范围,逗号分隔,为空表示本单品牌
     */
    private String applybrands;

    /**
     * 券类型
     */
    private String coupontype;

    /**
     * 商品券类型
     */
    private Integer couponprotype;

    /**
     * 微信券编号
     */
    private String wxcouponcardid;

    /**
     * 券名称
     */
    private String couponname;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 券描述
     */
    private String couponremark;

    /**
     * 券面额
     */
    private Double couponvalue;

    /**
     * 有效期类型
     */
    private String validitytype;

    /**
     * 有效期天数
     */
    private Integer validitydays;

    /**
     * 样式模板
     */
    private Integer styleid;

    /**
     * 使用说明
     */
    private String guide;

    /**
     * 状态
     */
    private Integer couponstatus;

    /**
     * 审批人
     */
    private Integer approvaluser;

    /**
     * 审批时间
     */
    private Date approvaldate;

    /**
     * 券总量
     */
    private Integer coupontotal;

    /**
     * 领取总量
     */
    private Integer gettotal;

    /**
     * 核销总量
     */
    private Integer selltotal;

    /**
     * 
     */
    private Integer userid;

    /**
     * 券编辑组织类型
     */
    private String orgtype;

    /**
     * 是否可以转赠(1.可以 0.不可以)
     */
    private String iscangivefriend;

    /**
     * 是否允许多张券(1允许 0不允许)
     */
    private String isallowmultiplecoupons;

    /**
     * 是否有组织限制（0=否，1=是）
     */
    private String applyisexclude;

    /**
     * 外部的券编号（唯一编号）
     */
    private String code;

    /**
     * 商品是否排除（0=否，1=是）
     */
    private String couponproisexclude;

    /**
     * 门槛类型（1=订单，2=商品）
     */
    private String limittype;

    /**
     * 促销类型
     */
    private Integer promotiontype;

    /**
     * 是否微信券
     */
    private String iswxcoupon;

    /**
     * 微信券总量
     */
    private Integer wxcoupontotal;

    /**
     * 微信券号规则(系统生成,手工导入)
     */
    private String wxcouponnotype;

    /**
     * 微信券审核状态(1：通过，2：未通过)
     */
    private Integer wxcouponcheckstatus;

    /**
     * 微信券审核未通过原因
     */
    private String wxcouponrefusereason;

    /**
     * 券编辑组织编号
     */
    private Integer orgid;

    /**
     * 是否支持到单品
     */
    private String ispro;

    /**
     * 
     */
    private String promotioncode;

    /**
     * 
     */
    private Integer promotionvalidtype;

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
    private String applytype;

    /**
     * 
     */
    private Double couponpricelimit;

    /**
     * 创建人
     */
    private String createuser;

    /**
     * 创建时间
     */
    private Date createdate;

    /**
     * 最后修改人
     */
    private String lastmodifieduser;

    /**
     * 最后更新时间
     */
    private Date lastmodifieddate;

    /**
     * 
     */
    private Integer sendtotal;

    /**
     * 是否允许一个订单购买多个商品（0 不允许 1 允许）
     */
    private String isallowbuymore;

    /**
     * 允许一个订单最多购买多少商品
     */
    private Integer allowmaxbuynum;

    /**
     * 是否允许正价商品使用（0 不允许 1 允许）
     */
    private String isallowpositive;

    /**
     * 折扣限制
     */
    private Integer discountlimit;

    /**
     * 券转赠描述
     */
    private String shareremark;

    /**
     * 可消费适用组织类型
     */
    private String consumeapplytype;

    /**
     * 是否选择了券核销门店范围
     */
    private String ishasconsumeshops;

    /**
     * 可消费适用组织类型值
     */
    private Integer consumeapplyvalue;

    /**
     * 制券来源  0 品牌券  1 平台券 2 微信 
     */
    private Integer couponorigin;

    /**
     * 平台券库Id
     */
    private Integer unioncoupongrpid;

    /**
     * 采购单编号
     */
    private Long purchaseorderid;

    /**
     * 驳回原因
     */
    private String remark;

    /**
     * 异业券有效期，0导入时填写，1活动中配置
     */
    private Integer couponvalidtype;

    /**
     * 一个订单最多使用张数
     */
    private Integer allowmultiplecoupons;

    /**
     * 折扣率折扣金额(1最高使用折扣金额上限 0无最高使用金额上限)
     */
    private String iscoupondiscountamount;

    /**
     * 1最高使用折扣金额上限*元
     */
    private Double coupondiscountamount;

    /**
     * 订单折扣限制(1有 0无)
     */
    private String isdiscountlimit;

    /**
     * 供券方品牌编号
     */
    private Integer supplybrandid;

    /**
     * 
     */
    private String wxqrcode;

    /**
     * 0品牌驿业券 1第三方驿业券,默认值0
     */
    private Integer unioncoupontype;

    /**
     * 第三方异业券类型，1:券码   2:url 3:券码+url  4:直充
     */
    private Integer unioncouponorigin;

    /**
     * 第三方异业券充值地址，只有UnionCouponOrigin=3时有效
     */
    private String unioncouponrechargeurl;

    /**
     * 
     */
    private String uniquekey;

    /**
     * 
     */
    private String productpic;

    /**
     * 商城是否支持免邮
     */
    private String isfreepostinmall;

    /**
     * 发放渠道
     */
    private String platformids;

    /**
     * 权益券商品描述
     */
    private String productdesc;

    /**
     * 是否支持退单退券
     */
    private String issinglecoupon;

    /**
     * 
     */
    private Integer isremainvalid;

    /**
     * 新增券有效期天数
     */
    private Integer newaddcouponvaliddays;

    /**
     * 
     */
    private String remarktag;

    /**
     * 是否支持叠加使用优惠券
     */
    private String iscomposited;

    /**
     * 是否支持线下退单退券
     */
    private String offlineissinglecoupon;

    /**
     * 线下是否保持原有效期
     */
    private String offlineisremainvalid;

    /**
     * 线下新增券有效期天数
     */
    private Integer offlinenewaddcouponvaliddays;

    /**
     * 券库关联商品类型 0指定商品  1指定条件
     */
    private Integer grpprocontype;

    /**
     * 0全部品牌  1部分品牌
     */
    private Integer probdtype;

    /**
     * 1包含  0排除
     */
    private String iscontainbd;

    /**
     * 0全部分类  1部分分类
     */
    private Integer procatetype;

    /**
     * 1包含  0排除
     */
    private String iscontaincate;

    /**
     * 0不排除  1排除sku 2排除货号
     */
    private Integer removeprotype;

    /**
     * 商品折扣限制(1有 0无)
     */
    private String isprodiscountlimit;

    /**
     * 商品折扣限制
     */
    private Integer prodiscountlimit;

    /**
     * 允许一个订单最少购买多少商品
     */
    private Integer allowminbuynum;

    /**
     * 是否限制一个订单最少购买商品数（0-不限制，1-限制）
     */
    private String isallowbuyless;

    /**
     * 是否发送短信
     */
    private String issendsms;

    /**
     * 
     */
    private String h5address;

    /**
     * 小程序AppId
     */
    private String miniappid;

    /**
     * 
     */
    private String miniappaddress;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getTplid() {
		return tplid;
	}

	public void setTplid(Integer tplid) {
		this.tplid = tplid;
	}

	public String getTplname() {
		return tplname;
	}

	public void setTplname(String tplname) {
		this.tplname = tplname;
	}

	public String getSharepic() {
		return sharepic;
	}

	public void setSharepic(String sharepic) {
		this.sharepic = sharepic;
	}

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public Integer getOutsysid() {
		return outsysid;
	}

	public void setOutsysid(Integer outsysid) {
		this.outsysid = outsysid;
	}

	public String getApplybrands() {
		return applybrands;
	}

	public void setApplybrands(String applybrands) {
		this.applybrands = applybrands;
	}

	public String getCoupontype() {
		return coupontype;
	}

	public void setCoupontype(String coupontype) {
		this.coupontype = coupontype;
	}

	public Integer getCouponprotype() {
		return couponprotype;
	}

	public void setCouponprotype(Integer couponprotype) {
		this.couponprotype = couponprotype;
	}

	public String getWxcouponcardid() {
		return wxcouponcardid;
	}

	public void setWxcouponcardid(String wxcouponcardid) {
		this.wxcouponcardid = wxcouponcardid;
	}

	public String getCouponname() {
		return couponname;
	}

	public void setCouponname(String couponname) {
		this.couponname = couponname;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getCouponremark() {
		return couponremark;
	}

	public void setCouponremark(String couponremark) {
		this.couponremark = couponremark;
	}

	public Double getCouponvalue() {
		return couponvalue;
	}

	public void setCouponvalue(Double couponvalue) {
		this.couponvalue = couponvalue;
	}

	public String getValiditytype() {
		return validitytype;
	}

	public void setValiditytype(String validitytype) {
		this.validitytype = validitytype;
	}

	public Integer getValiditydays() {
		return validitydays;
	}

	public void setValiditydays(Integer validitydays) {
		this.validitydays = validitydays;
	}

	public Integer getStyleid() {
		return styleid;
	}

	public void setStyleid(Integer styleid) {
		this.styleid = styleid;
	}

	public String getGuide() {
		return guide;
	}

	public void setGuide(String guide) {
		this.guide = guide;
	}

	public Integer getCouponstatus() {
		return couponstatus;
	}

	public void setCouponstatus(Integer couponstatus) {
		this.couponstatus = couponstatus;
	}

	public Integer getApprovaluser() {
		return approvaluser;
	}

	public void setApprovaluser(Integer approvaluser) {
		this.approvaluser = approvaluser;
	}

	public Date getApprovaldate() {
		return approvaldate;
	}

	public void setApprovaldate(Date approvaldate) {
		this.approvaldate = approvaldate;
	}

	public Integer getCoupontotal() {
		return coupontotal;
	}

	public void setCoupontotal(Integer coupontotal) {
		this.coupontotal = coupontotal;
	}

	public Integer getGettotal() {
		return gettotal;
	}

	public void setGettotal(Integer gettotal) {
		this.gettotal = gettotal;
	}

	public Integer getSelltotal() {
		return selltotal;
	}

	public void setSelltotal(Integer selltotal) {
		this.selltotal = selltotal;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getOrgtype() {
		return orgtype;
	}

	public void setOrgtype(String orgtype) {
		this.orgtype = orgtype;
	}

	public String getIscangivefriend() {
		return iscangivefriend;
	}

	public void setIscangivefriend(String iscangivefriend) {
		this.iscangivefriend = iscangivefriend;
	}

	public String getIsallowmultiplecoupons() {
		return isallowmultiplecoupons;
	}

	public void setIsallowmultiplecoupons(String isallowmultiplecoupons) {
		this.isallowmultiplecoupons = isallowmultiplecoupons;
	}

	public String getApplyisexclude() {
		return applyisexclude;
	}

	public void setApplyisexclude(String applyisexclude) {
		this.applyisexclude = applyisexclude;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCouponproisexclude() {
		return couponproisexclude;
	}

	public void setCouponproisexclude(String couponproisexclude) {
		this.couponproisexclude = couponproisexclude;
	}

	public String getLimittype() {
		return limittype;
	}

	public void setLimittype(String limittype) {
		this.limittype = limittype;
	}

	public Integer getPromotiontype() {
		return promotiontype;
	}

	public void setPromotiontype(Integer promotiontype) {
		this.promotiontype = promotiontype;
	}

	public String getIswxcoupon() {
		return iswxcoupon;
	}

	public void setIswxcoupon(String iswxcoupon) {
		this.iswxcoupon = iswxcoupon;
	}

	public Integer getWxcoupontotal() {
		return wxcoupontotal;
	}

	public void setWxcoupontotal(Integer wxcoupontotal) {
		this.wxcoupontotal = wxcoupontotal;
	}

	public String getWxcouponnotype() {
		return wxcouponnotype;
	}

	public void setWxcouponnotype(String wxcouponnotype) {
		this.wxcouponnotype = wxcouponnotype;
	}

	public Integer getWxcouponcheckstatus() {
		return wxcouponcheckstatus;
	}

	public void setWxcouponcheckstatus(Integer wxcouponcheckstatus) {
		this.wxcouponcheckstatus = wxcouponcheckstatus;
	}

	public String getWxcouponrefusereason() {
		return wxcouponrefusereason;
	}

	public void setWxcouponrefusereason(String wxcouponrefusereason) {
		this.wxcouponrefusereason = wxcouponrefusereason;
	}

	public Integer getOrgid() {
		return orgid;
	}

	public void setOrgid(Integer orgid) {
		this.orgid = orgid;
	}

	public String getIspro() {
		return ispro;
	}

	public void setIspro(String ispro) {
		this.ispro = ispro;
	}

	public String getPromotioncode() {
		return promotioncode;
	}

	public void setPromotioncode(String promotioncode) {
		this.promotioncode = promotioncode;
	}

	public Integer getPromotionvalidtype() {
		return promotionvalidtype;
	}

	public void setPromotionvalidtype(Integer promotionvalidtype) {
		this.promotionvalidtype = promotionvalidtype;
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

	public String getApplytype() {
		return applytype;
	}

	public void setApplytype(String applytype) {
		this.applytype = applytype;
	}

	public Double getCouponpricelimit() {
		return couponpricelimit;
	}

	public void setCouponpricelimit(Double couponpricelimit) {
		this.couponpricelimit = couponpricelimit;
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

	public Integer getSendtotal() {
		return sendtotal;
	}

	public void setSendtotal(Integer sendtotal) {
		this.sendtotal = sendtotal;
	}

	public String getIsallowbuymore() {
		return isallowbuymore;
	}

	public void setIsallowbuymore(String isallowbuymore) {
		this.isallowbuymore = isallowbuymore;
	}

	public Integer getAllowmaxbuynum() {
		return allowmaxbuynum;
	}

	public void setAllowmaxbuynum(Integer allowmaxbuynum) {
		this.allowmaxbuynum = allowmaxbuynum;
	}

	public String getIsallowpositive() {
		return isallowpositive;
	}

	public void setIsallowpositive(String isallowpositive) {
		this.isallowpositive = isallowpositive;
	}

	public Integer getDiscountlimit() {
		return discountlimit;
	}

	public void setDiscountlimit(Integer discountlimit) {
		this.discountlimit = discountlimit;
	}

	public String getShareremark() {
		return shareremark;
	}

	public void setShareremark(String shareremark) {
		this.shareremark = shareremark;
	}

	public String getConsumeapplytype() {
		return consumeapplytype;
	}

	public void setConsumeapplytype(String consumeapplytype) {
		this.consumeapplytype = consumeapplytype;
	}

	public String getIshasconsumeshops() {
		return ishasconsumeshops;
	}

	public void setIshasconsumeshops(String ishasconsumeshops) {
		this.ishasconsumeshops = ishasconsumeshops;
	}

	public Integer getConsumeapplyvalue() {
		return consumeapplyvalue;
	}

	public void setConsumeapplyvalue(Integer consumeapplyvalue) {
		this.consumeapplyvalue = consumeapplyvalue;
	}

	public Integer getCouponorigin() {
		return couponorigin;
	}

	public void setCouponorigin(Integer couponorigin) {
		this.couponorigin = couponorigin;
	}

	public Integer getUnioncoupongrpid() {
		return unioncoupongrpid;
	}

	public void setUnioncoupongrpid(Integer unioncoupongrpid) {
		this.unioncoupongrpid = unioncoupongrpid;
	}

	public Long getPurchaseorderid() {
		return purchaseorderid;
	}

	public void setPurchaseorderid(Long purchaseorderid) {
		this.purchaseorderid = purchaseorderid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCouponvalidtype() {
		return couponvalidtype;
	}

	public void setCouponvalidtype(Integer couponvalidtype) {
		this.couponvalidtype = couponvalidtype;
	}

	public Integer getAllowmultiplecoupons() {
		return allowmultiplecoupons;
	}

	public void setAllowmultiplecoupons(Integer allowmultiplecoupons) {
		this.allowmultiplecoupons = allowmultiplecoupons;
	}

	public String getIscoupondiscountamount() {
		return iscoupondiscountamount;
	}

	public void setIscoupondiscountamount(String iscoupondiscountamount) {
		this.iscoupondiscountamount = iscoupondiscountamount;
	}

	public Double getCoupondiscountamount() {
		return coupondiscountamount;
	}

	public void setCoupondiscountamount(Double coupondiscountamount) {
		this.coupondiscountamount = coupondiscountamount;
	}

	public String getIsdiscountlimit() {
		return isdiscountlimit;
	}

	public void setIsdiscountlimit(String isdiscountlimit) {
		this.isdiscountlimit = isdiscountlimit;
	}

	public Integer getSupplybrandid() {
		return supplybrandid;
	}

	public void setSupplybrandid(Integer supplybrandid) {
		this.supplybrandid = supplybrandid;
	}

	public String getWxqrcode() {
		return wxqrcode;
	}

	public void setWxqrcode(String wxqrcode) {
		this.wxqrcode = wxqrcode;
	}

	public Integer getUnioncoupontype() {
		return unioncoupontype;
	}

	public void setUnioncoupontype(Integer unioncoupontype) {
		this.unioncoupontype = unioncoupontype;
	}

	public Integer getUnioncouponorigin() {
		return unioncouponorigin;
	}

	public void setUnioncouponorigin(Integer unioncouponorigin) {
		this.unioncouponorigin = unioncouponorigin;
	}

	public String getUnioncouponrechargeurl() {
		return unioncouponrechargeurl;
	}

	public void setUnioncouponrechargeurl(String unioncouponrechargeurl) {
		this.unioncouponrechargeurl = unioncouponrechargeurl;
	}

	public String getUniquekey() {
		return uniquekey;
	}

	public void setUniquekey(String uniquekey) {
		this.uniquekey = uniquekey;
	}

	public String getProductpic() {
		return productpic;
	}

	public void setProductpic(String productpic) {
		this.productpic = productpic;
	}

	public String getIsfreepostinmall() {
		return isfreepostinmall;
	}

	public void setIsfreepostinmall(String isfreepostinmall) {
		this.isfreepostinmall = isfreepostinmall;
	}

	public String getPlatformids() {
		return platformids;
	}

	public void setPlatformids(String platformids) {
		this.platformids = platformids;
	}

	public String getProductdesc() {
		return productdesc;
	}

	public void setProductdesc(String productdesc) {
		this.productdesc = productdesc;
	}

	public String getIssinglecoupon() {
		return issinglecoupon;
	}

	public void setIssinglecoupon(String issinglecoupon) {
		this.issinglecoupon = issinglecoupon;
	}

	public Integer getIsremainvalid() {
		return isremainvalid;
	}

	public void setIsremainvalid(Integer isremainvalid) {
		this.isremainvalid = isremainvalid;
	}

	public Integer getNewaddcouponvaliddays() {
		return newaddcouponvaliddays;
	}

	public void setNewaddcouponvaliddays(Integer newaddcouponvaliddays) {
		this.newaddcouponvaliddays = newaddcouponvaliddays;
	}

	public String getRemarktag() {
		return remarktag;
	}

	public void setRemarktag(String remarktag) {
		this.remarktag = remarktag;
	}

	public String getIscomposited() {
		return iscomposited;
	}

	public void setIscomposited(String iscomposited) {
		this.iscomposited = iscomposited;
	}

	public String getOfflineissinglecoupon() {
		return offlineissinglecoupon;
	}

	public void setOfflineissinglecoupon(String offlineissinglecoupon) {
		this.offlineissinglecoupon = offlineissinglecoupon;
	}

	public String getOfflineisremainvalid() {
		return offlineisremainvalid;
	}

	public void setOfflineisremainvalid(String offlineisremainvalid) {
		this.offlineisremainvalid = offlineisremainvalid;
	}

	public Integer getOfflinenewaddcouponvaliddays() {
		return offlinenewaddcouponvaliddays;
	}

	public void setOfflinenewaddcouponvaliddays(Integer offlinenewaddcouponvaliddays) {
		this.offlinenewaddcouponvaliddays = offlinenewaddcouponvaliddays;
	}

	public Integer getGrpprocontype() {
		return grpprocontype;
	}

	public void setGrpprocontype(Integer grpprocontype) {
		this.grpprocontype = grpprocontype;
	}

	public Integer getProbdtype() {
		return probdtype;
	}

	public void setProbdtype(Integer probdtype) {
		this.probdtype = probdtype;
	}

	public String getIscontainbd() {
		return iscontainbd;
	}

	public void setIscontainbd(String iscontainbd) {
		this.iscontainbd = iscontainbd;
	}

	public Integer getProcatetype() {
		return procatetype;
	}

	public void setProcatetype(Integer procatetype) {
		this.procatetype = procatetype;
	}

	public String getIscontaincate() {
		return iscontaincate;
	}

	public void setIscontaincate(String iscontaincate) {
		this.iscontaincate = iscontaincate;
	}

	public Integer getRemoveprotype() {
		return removeprotype;
	}

	public void setRemoveprotype(Integer removeprotype) {
		this.removeprotype = removeprotype;
	}

	public String getIsprodiscountlimit() {
		return isprodiscountlimit;
	}

	public void setIsprodiscountlimit(String isprodiscountlimit) {
		this.isprodiscountlimit = isprodiscountlimit;
	}

	public Integer getProdiscountlimit() {
		return prodiscountlimit;
	}

	public void setProdiscountlimit(Integer prodiscountlimit) {
		this.prodiscountlimit = prodiscountlimit;
	}

	public Integer getAllowminbuynum() {
		return allowminbuynum;
	}

	public void setAllowminbuynum(Integer allowminbuynum) {
		this.allowminbuynum = allowminbuynum;
	}

	public String getIsallowbuyless() {
		return isallowbuyless;
	}

	public void setIsallowbuyless(String isallowbuyless) {
		this.isallowbuyless = isallowbuyless;
	}

	public String getIssendsms() {
		return issendsms;
	}

	public void setIssendsms(String issendsms) {
		this.issendsms = issendsms;
	}

	public String getH5address() {
		return h5address;
	}

	public void setH5address(String h5address) {
		this.h5address = h5address;
	}

	public String getMiniappid() {
		return miniappid;
	}

	public void setMiniappid(String miniappid) {
		this.miniappid = miniappid;
	}

	public String getMiniappaddress() {
		return miniappaddress;
	}

	public void setMiniappaddress(String miniappaddress) {
		this.miniappaddress = miniappaddress;
	}

	@Override
	public String toString() {
		return "CrmCouponGrp{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",tplid=" + tplid +
				",tplname=" + tplname +
				",sharepic=" + sharepic +
				",syscode=" + syscode +
				",outsysid=" + outsysid +
				",applybrands=" + applybrands +
				",coupontype=" + coupontype +
				",couponprotype=" + couponprotype +
				",wxcouponcardid=" + wxcouponcardid +
				",couponname=" + couponname +
				",subtitle=" + subtitle +
				",couponremark=" + couponremark +
				",couponvalue=" + couponvalue +
				",validitytype=" + validitytype +
				",validitydays=" + validitydays +
				",styleid=" + styleid +
				",guide=" + guide +
				",couponstatus=" + couponstatus +
				",approvaluser=" + approvaluser +
				",approvaldate=" + approvaldate +
				",coupontotal=" + coupontotal +
				",gettotal=" + gettotal +
				",selltotal=" + selltotal +
				",userid=" + userid +
				",orgtype=" + orgtype +
				",iscangivefriend=" + iscangivefriend +
				",isallowmultiplecoupons=" + isallowmultiplecoupons +
				",applyisexclude=" + applyisexclude +
				",code=" + code +
				",couponproisexclude=" + couponproisexclude +
				",limittype=" + limittype +
				",promotiontype=" + promotiontype +
				",iswxcoupon=" + iswxcoupon +
				",wxcoupontotal=" + wxcoupontotal +
				",wxcouponnotype=" + wxcouponnotype +
				",wxcouponcheckstatus=" + wxcouponcheckstatus +
				",wxcouponrefusereason=" + wxcouponrefusereason +
				",orgid=" + orgid +
				",ispro=" + ispro +
				",promotioncode=" + promotioncode +
				",promotionvalidtype=" + promotionvalidtype +
				",begdate=" + begdate +
				",enddate=" + enddate +
				",applytype=" + applytype +
				",couponpricelimit=" + couponpricelimit +
				",createuser=" + createuser +
				",createdate=" + createdate +
				",lastmodifieduser=" + lastmodifieduser +
				",lastmodifieddate=" + lastmodifieddate +
				",sendtotal=" + sendtotal +
				",isallowbuymore=" + isallowbuymore +
				",allowmaxbuynum=" + allowmaxbuynum +
				",isallowpositive=" + isallowpositive +
				",discountlimit=" + discountlimit +
				",shareremark=" + shareremark +
				",consumeapplytype=" + consumeapplytype +
				",ishasconsumeshops=" + ishasconsumeshops +
				",consumeapplyvalue=" + consumeapplyvalue +
				",couponorigin=" + couponorigin +
				",unioncoupongrpid=" + unioncoupongrpid +
				",purchaseorderid=" + purchaseorderid +
				",remark=" + remark +
				",couponvalidtype=" + couponvalidtype +
				",allowmultiplecoupons=" + allowmultiplecoupons +
				",iscoupondiscountamount=" + iscoupondiscountamount +
				",coupondiscountamount=" + coupondiscountamount +
				",isdiscountlimit=" + isdiscountlimit +
				",supplybrandid=" + supplybrandid +
				",wxqrcode=" + wxqrcode +
				",unioncoupontype=" + unioncoupontype +
				",unioncouponorigin=" + unioncouponorigin +
				",unioncouponrechargeurl=" + unioncouponrechargeurl +
				",uniquekey=" + uniquekey +
				",productpic=" + productpic +
				",isfreepostinmall=" + isfreepostinmall +
				",platformids=" + platformids +
				",productdesc=" + productdesc +
				",issinglecoupon=" + issinglecoupon +
				",isremainvalid=" + isremainvalid +
				",newaddcouponvaliddays=" + newaddcouponvaliddays +
				",remarktag=" + remarktag +
				",iscomposited=" + iscomposited +
				",offlineissinglecoupon=" + offlineissinglecoupon +
				",offlineisremainvalid=" + offlineisremainvalid +
				",offlinenewaddcouponvaliddays=" + offlinenewaddcouponvaliddays +
				",grpprocontype=" + grpprocontype +
				",probdtype=" + probdtype +
				",iscontainbd=" + iscontainbd +
				",procatetype=" + procatetype +
				",iscontaincate=" + iscontaincate +
				",removeprotype=" + removeprotype +
				",isprodiscountlimit=" + isprodiscountlimit +
				",prodiscountlimit=" + prodiscountlimit +
				",allowminbuynum=" + allowminbuynum +
				",isallowbuyless=" + isallowbuyless +
				",issendsms=" + issendsms +
				",h5address=" + h5address +
				",miniappid=" + miniappid +
				",miniappaddress=" + miniappaddress +
				'}';
	}
}
