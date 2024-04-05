package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 客户_会员_会员基础信息

入会时间段：数字，0,1,2,......12,13....23, 
 *
 * @author FrozenWatermelon
 * @date 2022-04-05 14:44:13
 */
public class CrmVipInfo1 extends BaseModel implements Serializable{
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
     * 会员等级
     */
    private Integer gradeid;

    /**
     * 卡号
     */
    private String code;

    /**
     * 微信会员卡的原始code
     */
    private String wxcode;

    /**
     * 领取微信会员卡时间
     */
    private Date wxcodegettime;

    /**
     * 老卡号
     */
    private String oldcode;

    /**
     * 老卡关联绑定新卡编号
     */
    private Long oldbindnewid;

    /**
     * 老卡绑定时间
     */
    private Date oldbinddate;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 姓名
     */
    private String name;

    /**
     * 
     */
    private String mobileno;

    /**
     * 
     */
    private Date mobilebindtime;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别
     */
    private String sex;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 微信号码
     */
    private String wxno;

    /**
     * 
     */
    private String wxunionid;

    /**
     * 会员头像
     */
    private String wxheadimg;

    /**
     * 微博号码
     */
    private String weibno;

    /**
     * QQ号码
     */
    private String qqno;

    /**
     * 淘宝号码
     */
    private String tbno;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 证件号码
     */
    private String idcardno;

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
     * 
     */
    private Date wxregtime;

    /**
     * 
     */
    private String wxprovince;

    /**
     * 服务渠道
     */
    private Integer servicechannel;

    /**
     * 服务导购
     */
    private Integer servicesaler;

    /**
     * 
     */
    private Date servicebindtime;

    /**
     * 入会时间
     */
    private Date regtime;

    /**
     * 入会渠道
     */
    private Integer regchannel;

    /**
     * 入会年
     */
    private Integer regyearint;

    /**
     * 入会月
     */
    private Integer regmonthint;

    /**
     * 入会周
     */
    private Integer regweekint;

    /**
     * 
     */
    private Integer regsource;

    /**
     * 
     */
    private Long referee;

    /**
     * 邀请时间
     */
    private Date invitedate;

    /**
     * 邀请门店
     */
    private Integer inviteshopid;

    /**
     * 邀请导购
     */
    private Integer inviteuserid;

    /**
     * 当前积分
     */
    private Integer bonus;

    /**
     * 累计积分
     */
    private Integer bonustotal;

    /**
     * 
     */
    private String isforbid;

    /**
     * 创建时间
     */
    private Date createdate;

    /**
     * 最后更新时间
     */
    private Date lastmodifieddate;

    /**
     * 推荐人（邀请会员）
     */
    private Long invitevipid;

    /**
     * 邀请场景
     */
    private Integer invitescene;

    /**
     * 开卡导购
     */
    private Integer regsaler;

    /**
     * 粉丝最后服务关系变更时间
     */
    private Date fansuptime;

    /**
     * 注册会员是否微客
     */
    private String regisveeker;

    /**
     * 绑卡导购
     */
    private Integer bindsaler;

    /**
     * 绑卡门店
     */
    private Integer bindchannel;

    /**
     * 绑卡场景
     */
    private Integer bindscene;

    /**
     * 无主流量来源标识 No owner flow Source
     */
    private Integer nofsource;

    /**
     * 无主流量首次到达时间
     */
    private Date nofarrivetime;

    /**
     * 无主流量分配门店
     */
    private Integer nofassignshopid;

    /**
     * 无主流量分配导购
     */
    private Integer nofassignsaler;

    /**
     * 无主流量分配时间
     */
    private Date nofassigntime;

    /**
     * 无主流量分配会员等级
     */
    private Integer nofassignvipgradeid;

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

	public Integer getGradeid() {
		return gradeid;
	}

	public void setGradeid(Integer gradeid) {
		this.gradeid = gradeid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWxcode() {
		return wxcode;
	}

	public void setWxcode(String wxcode) {
		this.wxcode = wxcode;
	}

	public Date getWxcodegettime() {
		return wxcodegettime;
	}

	public void setWxcodegettime(Date wxcodegettime) {
		this.wxcodegettime = wxcodegettime;
	}

	public String getOldcode() {
		return oldcode;
	}

	public void setOldcode(String oldcode) {
		this.oldcode = oldcode;
	}

	public Long getOldbindnewid() {
		return oldbindnewid;
	}

	public void setOldbindnewid(Long oldbindnewid) {
		this.oldbindnewid = oldbindnewid;
	}

	public Date getOldbinddate() {
		return oldbinddate;
	}

	public void setOldbinddate(Date oldbinddate) {
		this.oldbinddate = oldbinddate;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public Date getMobilebindtime() {
		return mobilebindtime;
	}

	public void setMobilebindtime(Date mobilebindtime) {
		this.mobilebindtime = mobilebindtime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getWxno() {
		return wxno;
	}

	public void setWxno(String wxno) {
		this.wxno = wxno;
	}

	public String getWxunionid() {
		return wxunionid;
	}

	public void setWxunionid(String wxunionid) {
		this.wxunionid = wxunionid;
	}

	public String getWxheadimg() {
		return wxheadimg;
	}

	public void setWxheadimg(String wxheadimg) {
		this.wxheadimg = wxheadimg;
	}

	public String getWeibno() {
		return weibno;
	}

	public void setWeibno(String weibno) {
		this.weibno = weibno;
	}

	public String getQqno() {
		return qqno;
	}

	public void setQqno(String qqno) {
		this.qqno = qqno;
	}

	public String getTbno() {
		return tbno;
	}

	public void setTbno(String tbno) {
		this.tbno = tbno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdcardno() {
		return idcardno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
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

	public Date getWxregtime() {
		return wxregtime;
	}

	public void setWxregtime(Date wxregtime) {
		this.wxregtime = wxregtime;
	}

	public String getWxprovince() {
		return wxprovince;
	}

	public void setWxprovince(String wxprovince) {
		this.wxprovince = wxprovince;
	}

	public Integer getServicechannel() {
		return servicechannel;
	}

	public void setServicechannel(Integer servicechannel) {
		this.servicechannel = servicechannel;
	}

	public Integer getServicesaler() {
		return servicesaler;
	}

	public void setServicesaler(Integer servicesaler) {
		this.servicesaler = servicesaler;
	}

	public Date getServicebindtime() {
		return servicebindtime;
	}

	public void setServicebindtime(Date servicebindtime) {
		this.servicebindtime = servicebindtime;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public Integer getRegchannel() {
		return regchannel;
	}

	public void setRegchannel(Integer regchannel) {
		this.regchannel = regchannel;
	}

	public Integer getRegyearint() {
		return regyearint;
	}

	public void setRegyearint(Integer regyearint) {
		this.regyearint = regyearint;
	}

	public Integer getRegmonthint() {
		return regmonthint;
	}

	public void setRegmonthint(Integer regmonthint) {
		this.regmonthint = regmonthint;
	}

	public Integer getRegweekint() {
		return regweekint;
	}

	public void setRegweekint(Integer regweekint) {
		this.regweekint = regweekint;
	}

	public Integer getRegsource() {
		return regsource;
	}

	public void setRegsource(Integer regsource) {
		this.regsource = regsource;
	}

	public Long getReferee() {
		return referee;
	}

	public void setReferee(Long referee) {
		this.referee = referee;
	}

	public Date getInvitedate() {
		return invitedate;
	}

	public void setInvitedate(Date invitedate) {
		this.invitedate = invitedate;
	}

	public Integer getInviteshopid() {
		return inviteshopid;
	}

	public void setInviteshopid(Integer inviteshopid) {
		this.inviteshopid = inviteshopid;
	}

	public Integer getInviteuserid() {
		return inviteuserid;
	}

	public void setInviteuserid(Integer inviteuserid) {
		this.inviteuserid = inviteuserid;
	}

	public Integer getBonus() {
		return bonus;
	}

	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	public Integer getBonustotal() {
		return bonustotal;
	}

	public void setBonustotal(Integer bonustotal) {
		this.bonustotal = bonustotal;
	}

	public String getIsforbid() {
		return isforbid;
	}

	public void setIsforbid(String isforbid) {
		this.isforbid = isforbid;
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

	public Long getInvitevipid() {
		return invitevipid;
	}

	public void setInvitevipid(Long invitevipid) {
		this.invitevipid = invitevipid;
	}

	public Integer getInvitescene() {
		return invitescene;
	}

	public void setInvitescene(Integer invitescene) {
		this.invitescene = invitescene;
	}

	public Integer getRegsaler() {
		return regsaler;
	}

	public void setRegsaler(Integer regsaler) {
		this.regsaler = regsaler;
	}

	public Date getFansuptime() {
		return fansuptime;
	}

	public void setFansuptime(Date fansuptime) {
		this.fansuptime = fansuptime;
	}

	public String getRegisveeker() {
		return regisveeker;
	}

	public void setRegisveeker(String regisveeker) {
		this.regisveeker = regisveeker;
	}

	public Integer getBindsaler() {
		return bindsaler;
	}

	public void setBindsaler(Integer bindsaler) {
		this.bindsaler = bindsaler;
	}

	public Integer getBindchannel() {
		return bindchannel;
	}

	public void setBindchannel(Integer bindchannel) {
		this.bindchannel = bindchannel;
	}

	public Integer getBindscene() {
		return bindscene;
	}

	public void setBindscene(Integer bindscene) {
		this.bindscene = bindscene;
	}

	public Integer getNofsource() {
		return nofsource;
	}

	public void setNofsource(Integer nofsource) {
		this.nofsource = nofsource;
	}

	public Date getNofarrivetime() {
		return nofarrivetime;
	}

	public void setNofarrivetime(Date nofarrivetime) {
		this.nofarrivetime = nofarrivetime;
	}

	public Integer getNofassignshopid() {
		return nofassignshopid;
	}

	public void setNofassignshopid(Integer nofassignshopid) {
		this.nofassignshopid = nofassignshopid;
	}

	public Integer getNofassignsaler() {
		return nofassignsaler;
	}

	public void setNofassignsaler(Integer nofassignsaler) {
		this.nofassignsaler = nofassignsaler;
	}

	public Date getNofassigntime() {
		return nofassigntime;
	}

	public void setNofassigntime(Date nofassigntime) {
		this.nofassigntime = nofassigntime;
	}

	public Integer getNofassignvipgradeid() {
		return nofassignvipgradeid;
	}

	public void setNofassignvipgradeid(Integer nofassignvipgradeid) {
		this.nofassignvipgradeid = nofassignvipgradeid;
	}

	@Override
	public String toString() {
		return "CrmVipInfo1{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",gradeid=" + gradeid +
				",code=" + code +
				",wxcode=" + wxcode +
				",wxcodegettime=" + wxcodegettime +
				",oldcode=" + oldcode +
				",oldbindnewid=" + oldbindnewid +
				",oldbinddate=" + oldbinddate +
				",nickname=" + nickname +
				",name=" + name +
				",mobileno=" + mobileno +
				",mobilebindtime=" + mobilebindtime +
				",password=" + password +
				",sex=" + sex +
				",birthday=" + birthday +
				",wxno=" + wxno +
				",wxunionid=" + wxunionid +
				",wxheadimg=" + wxheadimg +
				",weibno=" + weibno +
				",qqno=" + qqno +
				",tbno=" + tbno +
				",email=" + email +
				",idcardno=" + idcardno +
				",country=" + country +
				",province=" + province +
				",city=" + city +
				",county=" + county +
				",wxregtime=" + wxregtime +
				",wxprovince=" + wxprovince +
				",servicechannel=" + servicechannel +
				",servicesaler=" + servicesaler +
				",servicebindtime=" + servicebindtime +
				",regtime=" + regtime +
				",regchannel=" + regchannel +
				",regyearint=" + regyearint +
				",regmonthint=" + regmonthint +
				",regweekint=" + regweekint +
				",regsource=" + regsource +
				",referee=" + referee +
				",invitedate=" + invitedate +
				",inviteshopid=" + inviteshopid +
				",inviteuserid=" + inviteuserid +
				",bonus=" + bonus +
				",bonustotal=" + bonustotal +
				",isforbid=" + isforbid +
				",createdate=" + createdate +
				",lastmodifieddate=" + lastmodifieddate +
				",invitevipid=" + invitevipid +
				",invitescene=" + invitescene +
				",regsaler=" + regsaler +
				",fansuptime=" + fansuptime +
				",regisveeker=" + regisveeker +
				",bindsaler=" + bindsaler +
				",bindchannel=" + bindchannel +
				",bindscene=" + bindscene +
				",nofsource=" + nofsource +
				",nofarrivetime=" + nofarrivetime +
				",nofassignshopid=" + nofassignshopid +
				",nofassignsaler=" + nofassignsaler +
				",nofassigntime=" + nofassigntime +
				",nofassignvipgradeid=" + nofassignvipgradeid +
				'}';
	}
}
