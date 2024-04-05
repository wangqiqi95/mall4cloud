package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
/**
 * 数据中心_基础_渠道信息

品牌下属的线上下门店需要先维护

所属商圈，
 *
 * @author FrozenWatermelon
 * @date 2022-04-10 10:16:40
 */
public class EdBaseShop extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 门店编号
     */
    private Integer id;

    /**
     * 集团编号
     */
    private Integer copid;

    /**
     * 品牌编号
     */
    private Integer brandid;

    /**
     * 渠道代码
     */
    private String code;

    /**
     * 渠道名称
     */
    private String name;

    /**
     * 别名
     */
    private String altname;

    /**
     * 
     */
    private Integer channeltype;

    /**
     * 渠道分类
     */
    private String category;

    /**
     * 服务电话
     */
    private String servicetel;

    /**
     * 服务电话2
     */
    private String servicetel2;

    /**
     * 仓库联系人
     */
    private String contact;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区(县)
     */
    private String county;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 面积
     */
    private Integer area;

    /**
     * 营业时间
     */
    private String opendate;

    /**
     * 营业人数
     */
    private Integer openpeople;

    /**
     * 所属商圈
     */
    private Integer busizoneid;

    /**
     * 是否删除
     */
    private String isdelete;

    /**
     * 渠道Logo
     */
    private String logourl;

    /**
     * 渠道简介
     */
    private String remark;

    /**
     * 
     */
    private Integer dianpingid;

    /**
     * 
     */
    private String isfree;

    /**
     * 
     */
    private String isactive;

    /**
     * 标签列表
     */
    private String tagids;

    /**
     * 门店是否可用状态。1 表示系统错误、2 表示审核中、3 审核通过、4 审核驳回。当该字段为1、2、4 状态时，poi_id 为空
     */
    private Integer wxpoistate;

    /**
     * 微信POI_ID
     */
    private String wxpoiid;

    /**
     * 微信门店审核返回的消息
     */
    private String wxpoicheckmsg;

    /**
     * 微信门店永久二维码
     */
    private String wxsceneqr;

    /**
     * 微信门店会员卡二维码
     */
    private String wxcardqr;

    /**
     * 微信会员卡开卡组件二维码
     */
    private String wxcardrgurlqr;

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
     * 微商城门店上架状态
     */
    private String mallisactive;

    /**
     * 门店类型 0门店，1仓库
     */
    private Integer orgtype;

    /**
     * Pos店上架状态 默认未上架
     */
    private String isposactive;

    /**
     * 分销店上架状态 默认未上架
     */
    private String isretailactive;

    /**
     * 门店类型(0:门店 1:分销)
     */
    private Integer shoptype;

    /**
     * Pos店上架类型（大店/标准店)
     */
    private Integer posshoptypeid;

    /**
     * 分销店上架类型（大店/标准店)
     */
    private Integer retailshoptypeid;

    /**
     * Crm会员门店上架类型（大店/标准店)
     */
    private Integer crmshoptypeid;

    /**
     * Mall门店上架类型（大店/标准店)
     */
    private Integer mallshoptypeid;

    /**
     * 是否虚拟总店 默认0
     */
    private String isvirtualshop;

    /**
     * CRM下架时间
     */
    private Date offshelvesdate;

    /**
     * 门店码
     */
    private String qrcodepath;

    /**
     * 1-自营 2-加盟 3-联营 4-代理 5-代销 6-托管
     */
    private Integer shopclass;

    /**
     * 消费行为标签列表
     */
    private String saletagids;

    /**
     * 是否商城门店
     */
    private String ismarketshop;

    /**
     * 门店楼层Id
     */
    private Integer floorid;

    /**
     * 门店的门牌号
     */
    private String housenum;

    /**
     * 门店关联品牌
     */
    private String associateproductbrand;

    /**
     * 门店LOGO
     */
    private String logo;

    /**
     * 微信支付商户号
     */
    private String merchantcode;

    /**
     * 微信支付商户名称
     */
    private String merchantname;

    /**
     * 是否内购门店
     */
    private String isng;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAltname() {
		return altname;
	}

	public void setAltname(String altname) {
		this.altname = altname;
	}

	public Integer getChanneltype() {
		return channeltype;
	}

	public void setChanneltype(Integer channeltype) {
		this.channeltype = channeltype;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getServicetel() {
		return servicetel;
	}

	public void setServicetel(String servicetel) {
		this.servicetel = servicetel;
	}

	public String getServicetel2() {
		return servicetel2;
	}

	public void setServicetel2(String servicetel2) {
		this.servicetel2 = servicetel2;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
	}

	public String getOpendate() {
		return opendate;
	}

	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}

	public Integer getOpenpeople() {
		return openpeople;
	}

	public void setOpenpeople(Integer openpeople) {
		this.openpeople = openpeople;
	}

	public Integer getBusizoneid() {
		return busizoneid;
	}

	public void setBusizoneid(Integer busizoneid) {
		this.busizoneid = busizoneid;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDianpingid() {
		return dianpingid;
	}

	public void setDianpingid(Integer dianpingid) {
		this.dianpingid = dianpingid;
	}

	public String getIsfree() {
		return isfree;
	}

	public void setIsfree(String isfree) {
		this.isfree = isfree;
	}

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getTagids() {
		return tagids;
	}

	public void setTagids(String tagids) {
		this.tagids = tagids;
	}

	public Integer getWxpoistate() {
		return wxpoistate;
	}

	public void setWxpoistate(Integer wxpoistate) {
		this.wxpoistate = wxpoistate;
	}

	public String getWxpoiid() {
		return wxpoiid;
	}

	public void setWxpoiid(String wxpoiid) {
		this.wxpoiid = wxpoiid;
	}

	public String getWxpoicheckmsg() {
		return wxpoicheckmsg;
	}

	public void setWxpoicheckmsg(String wxpoicheckmsg) {
		this.wxpoicheckmsg = wxpoicheckmsg;
	}

	public String getWxsceneqr() {
		return wxsceneqr;
	}

	public void setWxsceneqr(String wxsceneqr) {
		this.wxsceneqr = wxsceneqr;
	}

	public String getWxcardqr() {
		return wxcardqr;
	}

	public void setWxcardqr(String wxcardqr) {
		this.wxcardqr = wxcardqr;
	}

	public String getWxcardrgurlqr() {
		return wxcardrgurlqr;
	}

	public void setWxcardrgurlqr(String wxcardrgurlqr) {
		this.wxcardrgurlqr = wxcardrgurlqr;
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

	public String getMallisactive() {
		return mallisactive;
	}

	public void setMallisactive(String mallisactive) {
		this.mallisactive = mallisactive;
	}

	public Integer getOrgtype() {
		return orgtype;
	}

	public void setOrgtype(Integer orgtype) {
		this.orgtype = orgtype;
	}

	public String getIsposactive() {
		return isposactive;
	}

	public void setIsposactive(String isposactive) {
		this.isposactive = isposactive;
	}

	public String getIsretailactive() {
		return isretailactive;
	}

	public void setIsretailactive(String isretailactive) {
		this.isretailactive = isretailactive;
	}

	public Integer getShoptype() {
		return shoptype;
	}

	public void setShoptype(Integer shoptype) {
		this.shoptype = shoptype;
	}

	public Integer getPosshoptypeid() {
		return posshoptypeid;
	}

	public void setPosshoptypeid(Integer posshoptypeid) {
		this.posshoptypeid = posshoptypeid;
	}

	public Integer getRetailshoptypeid() {
		return retailshoptypeid;
	}

	public void setRetailshoptypeid(Integer retailshoptypeid) {
		this.retailshoptypeid = retailshoptypeid;
	}

	public Integer getCrmshoptypeid() {
		return crmshoptypeid;
	}

	public void setCrmshoptypeid(Integer crmshoptypeid) {
		this.crmshoptypeid = crmshoptypeid;
	}

	public Integer getMallshoptypeid() {
		return mallshoptypeid;
	}

	public void setMallshoptypeid(Integer mallshoptypeid) {
		this.mallshoptypeid = mallshoptypeid;
	}

	public String getIsvirtualshop() {
		return isvirtualshop;
	}

	public void setIsvirtualshop(String isvirtualshop) {
		this.isvirtualshop = isvirtualshop;
	}

	public Date getOffshelvesdate() {
		return offshelvesdate;
	}

	public void setOffshelvesdate(Date offshelvesdate) {
		this.offshelvesdate = offshelvesdate;
	}

	public String getQrcodepath() {
		return qrcodepath;
	}

	public void setQrcodepath(String qrcodepath) {
		this.qrcodepath = qrcodepath;
	}

	public Integer getShopclass() {
		return shopclass;
	}

	public void setShopclass(Integer shopclass) {
		this.shopclass = shopclass;
	}

	public String getSaletagids() {
		return saletagids;
	}

	public void setSaletagids(String saletagids) {
		this.saletagids = saletagids;
	}

	public String getIsmarketshop() {
		return ismarketshop;
	}

	public void setIsmarketshop(String ismarketshop) {
		this.ismarketshop = ismarketshop;
	}

	public Integer getFloorid() {
		return floorid;
	}

	public void setFloorid(Integer floorid) {
		this.floorid = floorid;
	}

	public String getHousenum() {
		return housenum;
	}

	public void setHousenum(String housenum) {
		this.housenum = housenum;
	}

	public String getAssociateproductbrand() {
		return associateproductbrand;
	}

	public void setAssociateproductbrand(String associateproductbrand) {
		this.associateproductbrand = associateproductbrand;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getMerchantcode() {
		return merchantcode;
	}

	public void setMerchantcode(String merchantcode) {
		this.merchantcode = merchantcode;
	}

	public String getMerchantname() {
		return merchantname;
	}

	public void setMerchantname(String merchantname) {
		this.merchantname = merchantname;
	}

	public String getIsng() {
		return isng;
	}

	public void setIsng(String isng) {
		this.isng = isng;
	}

	@Override
	public String toString() {
		return "EdBaseShop{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",code=" + code +
				",name=" + name +
				",altname=" + altname +
				",channeltype=" + channeltype +
				",category=" + category +
				",servicetel=" + servicetel +
				",servicetel2=" + servicetel2 +
				",contact=" + contact +
				",country=" + country +
				",province=" + province +
				",city=" + city +
				",county=" + county +
				",address=" + address +
				",longitude=" + longitude +
				",latitude=" + latitude +
				",area=" + area +
				",opendate=" + opendate +
				",openpeople=" + openpeople +
				",busizoneid=" + busizoneid +
				",isdelete=" + isdelete +
				",logourl=" + logourl +
				",remark=" + remark +
				",dianpingid=" + dianpingid +
				",isfree=" + isfree +
				",isactive=" + isactive +
				",tagids=" + tagids +
				",wxpoistate=" + wxpoistate +
				",wxpoiid=" + wxpoiid +
				",wxpoicheckmsg=" + wxpoicheckmsg +
				",wxsceneqr=" + wxsceneqr +
				",wxcardqr=" + wxcardqr +
				",wxcardrgurlqr=" + wxcardrgurlqr +
				",createuser=" + createuser +
				",createdate=" + createdate +
				",lastmodifieduser=" + lastmodifieduser +
				",lastmodifieddate=" + lastmodifieddate +
				",mallisactive=" + mallisactive +
				",orgtype=" + orgtype +
				",isposactive=" + isposactive +
				",isretailactive=" + isretailactive +
				",shoptype=" + shoptype +
				",posshoptypeid=" + posshoptypeid +
				",retailshoptypeid=" + retailshoptypeid +
				",crmshoptypeid=" + crmshoptypeid +
				",mallshoptypeid=" + mallshoptypeid +
				",isvirtualshop=" + isvirtualshop +
				",offshelvesdate=" + offshelvesdate +
				",qrcodepath=" + qrcodepath +
				",shopclass=" + shopclass +
				",saletagids=" + saletagids +
				",ismarketshop=" + ismarketshop +
				",floorid=" + floorid +
				",housenum=" + housenum +
				",associateproductbrand=" + associateproductbrand +
				",logo=" + logo +
				",merchantcode=" + merchantcode +
				",merchantname=" + merchantname +
				",isng=" + isng +
				'}';
	}
}
