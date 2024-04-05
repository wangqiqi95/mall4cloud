package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
/**
 * 分销订单佣金表
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:30
 */
public class MallFxOrderCommssion extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 集团编号
     */
    private Integer copid;

    /**
     * 品牌编号
     */
    private Integer brandid;

    /**
     * 人群类型：0，导购，1，会员
     */
    private Integer populationtype;

    /**
     * 人Id
     */
    private Long populationid;

    /**
     * 得佣金人的手机号
     */
    private String populationphone;

    /**
     * 订单Id
     */
    private Long orderid;

    /**
     * 订单类型
     */
    private String ordertype;

    /**
     * 订单存储类型：0 商城订单表  1 储值卡订单表
     */
    private Integer ordersavetype;

    /**
     * 订单号
     */
    private String ordercode;

    /**
     * 订单是否支付
     */
    private String ispayed;

    /**
     * 订单支付时间
     */
    private Date orderpaytime;

    /**
     * 下单时间
     */
    private Date addtime;

    /**
     * 服务门店
     */
    private Integer servicechannel;

    /**
     * 佣金状态:0,已撤销，1,取消待扣佣金，10，待入账，15，待扣佣金，30，已入账，35，待扣佣金已入账
     */
    private Integer commissionstatus;

    /**
     * 关系类型：0 会员分享关系   1  会员邀请关系     2  服务导购关系     3 导购分享链接关系
     */
    private Integer relationtype;

    /**
     * 下单人Id
     */
    private Long buyerid;

    /**
     * 收货人手机号
     */
    private String recvmobile;

    /**
     * 收货联系人
     */
    private String recvconsignee;

    /**
     * 佣金类型：0 ：正向佣金  ，1：逆向佣金
     */
    private Integer octype;

    /**
     * 关联退款单Id
     */
    private Long refundid;

    /**
     * 关联退款单号
     */
    private String refundno;

    /**
     * 退货Id
     */
    private Long returnid;

    /**
     * 关联退货单号
     */
    private String returnno;

    /**
     * 正向佣金订单Id
     */
    private Long addocid;

    /**
     * 预计到账时间
     */
    private Date inaccounttime;

    /**
     * 创建时间
     */
    private Date createdate;

    /**
     * 最后更新时间
     */
    private Date lastmodifieddate;

    /**
     * 佣金产生人Id
     */
    private Long provideid;

    /**
     * 分享记录ID
     */
    private Long fxshareid;

    /**
     * 下单门店
     */
    private Integer shopid;

    /**
     * 是否自购
     */
    private String buymyself;

    /**
     * 来源佣金订单Id
     */
    private Long sourceocid;

    /**
     * 产生佣金时的上级分销员Id
     */
    private Long parentfxid;

    /**
     * 数据来源： 0 小程序下单  1 导入订单
     */
    private Integer datasource;

    /**
     * 推广来源：0 默认  1 机器人群推广
     */
    private Integer promotionsource;

    /**
     * 推广来源Id
     */
    private Long psid;

    /**
     * 推广来源编号
     */
    private String pscode;

    /**
     * 推广来源名称
     */
    private String psname;

    /**
     * 推广媒介Id
     */
    private Long promotionid;

    /**
     * 推广媒介编号
     */
    private String promotioncode;

    /**
     * 推广媒介名称
     */
    private String promotionname;

    /**
     * 退单是否成功
     */
    private String rtsuccess;

    /**
     * 退单是否同意
     */
    private String rtagree;

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

	public Integer getPopulationtype() {
		return populationtype;
	}

	public void setPopulationtype(Integer populationtype) {
		this.populationtype = populationtype;
	}

	public Long getPopulationid() {
		return populationid;
	}

	public void setPopulationid(Long populationid) {
		this.populationid = populationid;
	}

	public String getPopulationphone() {
		return populationphone;
	}

	public void setPopulationphone(String populationphone) {
		this.populationphone = populationphone;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public Integer getOrdersavetype() {
		return ordersavetype;
	}

	public void setOrdersavetype(Integer ordersavetype) {
		this.ordersavetype = ordersavetype;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public String getIspayed() {
		return ispayed;
	}

	public void setIspayed(String ispayed) {
		this.ispayed = ispayed;
	}

	public Date getOrderpaytime() {
		return orderpaytime;
	}

	public void setOrderpaytime(Date orderpaytime) {
		this.orderpaytime = orderpaytime;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Integer getServicechannel() {
		return servicechannel;
	}

	public void setServicechannel(Integer servicechannel) {
		this.servicechannel = servicechannel;
	}

	public Integer getCommissionstatus() {
		return commissionstatus;
	}

	public void setCommissionstatus(Integer commissionstatus) {
		this.commissionstatus = commissionstatus;
	}

	public Integer getRelationtype() {
		return relationtype;
	}

	public void setRelationtype(Integer relationtype) {
		this.relationtype = relationtype;
	}

	public Long getBuyerid() {
		return buyerid;
	}

	public void setBuyerid(Long buyerid) {
		this.buyerid = buyerid;
	}

	public String getRecvmobile() {
		return recvmobile;
	}

	public void setRecvmobile(String recvmobile) {
		this.recvmobile = recvmobile;
	}

	public String getRecvconsignee() {
		return recvconsignee;
	}

	public void setRecvconsignee(String recvconsignee) {
		this.recvconsignee = recvconsignee;
	}

	public Integer getOctype() {
		return octype;
	}

	public void setOctype(Integer octype) {
		this.octype = octype;
	}

	public Long getRefundid() {
		return refundid;
	}

	public void setRefundid(Long refundid) {
		this.refundid = refundid;
	}

	public String getRefundno() {
		return refundno;
	}

	public void setRefundno(String refundno) {
		this.refundno = refundno;
	}

	public Long getReturnid() {
		return returnid;
	}

	public void setReturnid(Long returnid) {
		this.returnid = returnid;
	}

	public String getReturnno() {
		return returnno;
	}

	public void setReturnno(String returnno) {
		this.returnno = returnno;
	}

	public Long getAddocid() {
		return addocid;
	}

	public void setAddocid(Long addocid) {
		this.addocid = addocid;
	}

	public Date getInaccounttime() {
		return inaccounttime;
	}

	public void setInaccounttime(Date inaccounttime) {
		this.inaccounttime = inaccounttime;
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

	public Long getProvideid() {
		return provideid;
	}

	public void setProvideid(Long provideid) {
		this.provideid = provideid;
	}

	public Long getFxshareid() {
		return fxshareid;
	}

	public void setFxshareid(Long fxshareid) {
		this.fxshareid = fxshareid;
	}

	public Integer getShopid() {
		return shopid;
	}

	public void setShopid(Integer shopid) {
		this.shopid = shopid;
	}

	public String getBuymyself() {
		return buymyself;
	}

	public void setBuymyself(String buymyself) {
		this.buymyself = buymyself;
	}

	public Long getSourceocid() {
		return sourceocid;
	}

	public void setSourceocid(Long sourceocid) {
		this.sourceocid = sourceocid;
	}

	public Long getParentfxid() {
		return parentfxid;
	}

	public void setParentfxid(Long parentfxid) {
		this.parentfxid = parentfxid;
	}

	public Integer getDatasource() {
		return datasource;
	}

	public void setDatasource(Integer datasource) {
		this.datasource = datasource;
	}

	public Integer getPromotionsource() {
		return promotionsource;
	}

	public void setPromotionsource(Integer promotionsource) {
		this.promotionsource = promotionsource;
	}

	public Long getPsid() {
		return psid;
	}

	public void setPsid(Long psid) {
		this.psid = psid;
	}

	public String getPscode() {
		return pscode;
	}

	public void setPscode(String pscode) {
		this.pscode = pscode;
	}

	public String getPsname() {
		return psname;
	}

	public void setPsname(String psname) {
		this.psname = psname;
	}

	public Long getPromotionid() {
		return promotionid;
	}

	public void setPromotionid(Long promotionid) {
		this.promotionid = promotionid;
	}

	public String getPromotioncode() {
		return promotioncode;
	}

	public void setPromotioncode(String promotioncode) {
		this.promotioncode = promotioncode;
	}

	public String getPromotionname() {
		return promotionname;
	}

	public void setPromotionname(String promotionname) {
		this.promotionname = promotionname;
	}

	public String getRtsuccess() {
		return rtsuccess;
	}

	public void setRtsuccess(String rtsuccess) {
		this.rtsuccess = rtsuccess;
	}

	public String getRtagree() {
		return rtagree;
	}

	public void setRtagree(String rtagree) {
		this.rtagree = rtagree;
	}

	@Override
	public String toString() {
		return "MallFxOrderCommssion{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",populationtype=" + populationtype +
				",populationid=" + populationid +
				",populationphone=" + populationphone +
				",orderid=" + orderid +
				",ordertype=" + ordertype +
				",ordersavetype=" + ordersavetype +
				",ordercode=" + ordercode +
				",ispayed=" + ispayed +
				",orderpaytime=" + orderpaytime +
				",addtime=" + addtime +
				",servicechannel=" + servicechannel +
				",commissionstatus=" + commissionstatus +
				",relationtype=" + relationtype +
				",buyerid=" + buyerid +
				",recvmobile=" + recvmobile +
				",recvconsignee=" + recvconsignee +
				",octype=" + octype +
				",refundid=" + refundid +
				",refundno=" + refundno +
				",returnid=" + returnid +
				",returnno=" + returnno +
				",addocid=" + addocid +
				",inaccounttime=" + inaccounttime +
				",createdate=" + createdate +
				",lastmodifieddate=" + lastmodifieddate +
				",provideid=" + provideid +
				",fxshareid=" + fxshareid +
				",shopid=" + shopid +
				",buymyself=" + buymyself +
				",sourceocid=" + sourceocid +
				",parentfxid=" + parentfxid +
				",datasource=" + datasource +
				",promotionsource=" + promotionsource +
				",psid=" + psid +
				",pscode=" + pscode +
				",psname=" + psname +
				",promotionid=" + promotionid +
				",promotioncode=" + promotioncode +
				",promotionname=" + promotionname +
				",rtsuccess=" + rtsuccess +
				",rtagree=" + rtagree +
				'}';
	}
}
