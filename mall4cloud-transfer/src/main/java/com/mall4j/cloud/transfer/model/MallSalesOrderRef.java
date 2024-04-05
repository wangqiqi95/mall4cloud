package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 商城_订单_官网订单信息_退款信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 15:14:00
 */
public class MallSalesOrderRef implements Serializable{
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
     * 仓库Id
     */
    private Integer ewarehouseid;

    /**
     * 自提门店编号
     */
    private Integer selffetchshopid;

    /**
     * 买家会员号
     */
    private Long buyerid;

    /**
     * 订单编号
     */
    private Long orderid;

    /**
     * 订单编号
     */
    private String ordercode;

    /**
     * 退款单号
     */
    private String refundcode;

    /**
     * 换货原始订单号
     */
    private String exchangeoriginalordercode;

    /**
     * 退货单编号
     */
    private Long returnid;

    /**
     * 下单时间
     */
    private Date ordertime;

    /**
     * 退款单创建时间
     */
    private Date refundtime;

    /**
     * 实际退款完成时间
     */
    private Date finishtime;

    /**
     * 退款总金额
     */
    private Double refundmoney;

    /**
     * 实退金额
     */
    private Double amountmenory;

    /**
     * 储值卡退款状态
     */
    private Integer giftcardrefundstatus;

    /**
     * 储值卡退款金额
     */
    private Double giftcardamountmenory;

    /**
     * 退积分
     */
    private Integer refundbonus;

    /**
     * 退款原因
     */
    private String refundreason;

    /**
     * 退款状态
     */
    private Integer refundstatus;

    /**
     * 退款处理状态（1:处理中，2:处理完成）
     */
    private Integer processstatus;

    /**
     * 退款取消类型（0：全部；1：客户取消；2：客服拒绝）
     */
    private Integer refundcancellationtype;

    /**
     * 微信退款状态
     */
    private String wxrefundstatus;

    /**
     * 微信退款结果
     */
    private String wxrefundmsg;

    /**
     * 退款方式
     */
    private Integer mallrefundway;

    /**
     * 定金退款方式
     */
    private Integer malldepositrefundway;

    /**
     * 退款方式
     */
    private Integer mallfinalrefundway;

    /**
     * 支付凭证
     */
    private String tradeno;

    /**
     * 尾款TradeNo
     */
    private String finaltradeno;

    /**
     * 尾款退款状态
     */
    private String finalwxrefundstatus;

    /**
     * 尾款Msg
     */
    private String finalwxrefundmsg;

    /**
     * 客服是否备注
     */
    private String iscustomerserviceremark;

    /**
     * 退款备注
     */
    private String refundremark;

    /**
     * 是否自动退款
     */
    private String autorefund;

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
     * 退款类型:Order:订单退款，Gift:积分退款
     */
    private String refundtype;

    /**
     * 发货状态 0:未发货 1:已发货
     */
    private Integer shipstate;

    /**
     * 确认退款金额-可为空，最终以实际退款金额为准，主要功能为最终实际退款金额默认值
     */
    private Double confirmrefundmoney;

    /**
     * 确认退款金额如果有值的话，则必填修改原因
     */
    private String confirmrefundmoneyremark;

    /**
     * 应退积分
     */
    private Integer oldrefundbonus;

    /**
     * 退运费（金钱）
     */
    private Double refundexpressfeebymoney;

    /**
     * 退运费（积分）
     */
    private Double refundexpressfeebybonus;

    /**
     * 补充退款（不参与商品金额分摊）
     */
    private Double comrefundmoney;

    /**
     * 补充退积分（不参与商品积分分摊）
     */
    private Integer comrefundbonus;

    /**
     * 退款单分销订单业务是否处理完成，0 未处理 1 已处理
     */
    private Integer fxhandler;

    /**
     * 退款申请是否是系统自动审核通过的
     */
    private String issysrefapproved;

    /**
     * 是否是开启退款列表驳回
     */
    private String isopenrefundreject;

    /**
     * 
     */
    private String isimportcrm;

    /**
     * 订单插旗类型(0=无,1=红,2=黄,4=绿,8=蓝,16=紫)
     */
    private Integer flagtype;

    /**
     * 是否整单退款 0 否  1 是
     */
    private String issinglerefund;

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 推送crm是否不参与积分计算
     */
    private Integer isnotcalcbonus;

    /**
     * 退款待处理自动同意时间
     */
    private Date autoagreeapplydate;

    /**
     * canal同步用字段
     */
    private Integer canalsyncversion;

    /**
     * 实际退积分
     */
    private Integer useintegral;

    /**
     * 应退积分
     */
    private Integer olduseintegral;

    /**
     * 积分抵现退款状态0：未退，1：已退
     */
    private Integer useintegraltype;

    /**
     * 积分抵现运费积分
     */
    private Integer expressfeeuseintegral;

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

	public Integer getEwarehouseid() {
		return ewarehouseid;
	}

	public void setEwarehouseid(Integer ewarehouseid) {
		this.ewarehouseid = ewarehouseid;
	}

	public Integer getSelffetchshopid() {
		return selffetchshopid;
	}

	public void setSelffetchshopid(Integer selffetchshopid) {
		this.selffetchshopid = selffetchshopid;
	}

	public Long getBuyerid() {
		return buyerid;
	}

	public void setBuyerid(Long buyerid) {
		this.buyerid = buyerid;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public String getRefundcode() {
		return refundcode;
	}

	public void setRefundcode(String refundcode) {
		this.refundcode = refundcode;
	}

	public String getExchangeoriginalordercode() {
		return exchangeoriginalordercode;
	}

	public void setExchangeoriginalordercode(String exchangeoriginalordercode) {
		this.exchangeoriginalordercode = exchangeoriginalordercode;
	}

	public Long getReturnid() {
		return returnid;
	}

	public void setReturnid(Long returnid) {
		this.returnid = returnid;
	}

	public Date getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}

	public Date getRefundtime() {
		return refundtime;
	}

	public void setRefundtime(Date refundtime) {
		this.refundtime = refundtime;
	}

	public Date getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}

	public Double getRefundmoney() {
		return refundmoney;
	}

	public void setRefundmoney(Double refundmoney) {
		this.refundmoney = refundmoney;
	}

	public Double getAmountmenory() {
		return amountmenory;
	}

	public void setAmountmenory(Double amountmenory) {
		this.amountmenory = amountmenory;
	}

	public Integer getGiftcardrefundstatus() {
		return giftcardrefundstatus;
	}

	public void setGiftcardrefundstatus(Integer giftcardrefundstatus) {
		this.giftcardrefundstatus = giftcardrefundstatus;
	}

	public Double getGiftcardamountmenory() {
		return giftcardamountmenory;
	}

	public void setGiftcardamountmenory(Double giftcardamountmenory) {
		this.giftcardamountmenory = giftcardamountmenory;
	}

	public Integer getRefundbonus() {
		return refundbonus;
	}

	public void setRefundbonus(Integer refundbonus) {
		this.refundbonus = refundbonus;
	}

	public String getRefundreason() {
		return refundreason;
	}

	public void setRefundreason(String refundreason) {
		this.refundreason = refundreason;
	}

	public Integer getRefundstatus() {
		return refundstatus;
	}

	public void setRefundstatus(Integer refundstatus) {
		this.refundstatus = refundstatus;
	}

	public Integer getProcessstatus() {
		return processstatus;
	}

	public void setProcessstatus(Integer processstatus) {
		this.processstatus = processstatus;
	}

	public Integer getRefundcancellationtype() {
		return refundcancellationtype;
	}

	public void setRefundcancellationtype(Integer refundcancellationtype) {
		this.refundcancellationtype = refundcancellationtype;
	}

	public String getWxrefundstatus() {
		return wxrefundstatus;
	}

	public void setWxrefundstatus(String wxrefundstatus) {
		this.wxrefundstatus = wxrefundstatus;
	}

	public String getWxrefundmsg() {
		return wxrefundmsg;
	}

	public void setWxrefundmsg(String wxrefundmsg) {
		this.wxrefundmsg = wxrefundmsg;
	}

	public Integer getMallrefundway() {
		return mallrefundway;
	}

	public void setMallrefundway(Integer mallrefundway) {
		this.mallrefundway = mallrefundway;
	}

	public Integer getMalldepositrefundway() {
		return malldepositrefundway;
	}

	public void setMalldepositrefundway(Integer malldepositrefundway) {
		this.malldepositrefundway = malldepositrefundway;
	}

	public Integer getMallfinalrefundway() {
		return mallfinalrefundway;
	}

	public void setMallfinalrefundway(Integer mallfinalrefundway) {
		this.mallfinalrefundway = mallfinalrefundway;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getFinaltradeno() {
		return finaltradeno;
	}

	public void setFinaltradeno(String finaltradeno) {
		this.finaltradeno = finaltradeno;
	}

	public String getFinalwxrefundstatus() {
		return finalwxrefundstatus;
	}

	public void setFinalwxrefundstatus(String finalwxrefundstatus) {
		this.finalwxrefundstatus = finalwxrefundstatus;
	}

	public String getFinalwxrefundmsg() {
		return finalwxrefundmsg;
	}

	public void setFinalwxrefundmsg(String finalwxrefundmsg) {
		this.finalwxrefundmsg = finalwxrefundmsg;
	}

	public String getIscustomerserviceremark() {
		return iscustomerserviceremark;
	}

	public void setIscustomerserviceremark(String iscustomerserviceremark) {
		this.iscustomerserviceremark = iscustomerserviceremark;
	}

	public String getRefundremark() {
		return refundremark;
	}

	public void setRefundremark(String refundremark) {
		this.refundremark = refundremark;
	}

	public String getAutorefund() {
		return autorefund;
	}

	public void setAutorefund(String autorefund) {
		this.autorefund = autorefund;
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

	public String getRefundtype() {
		return refundtype;
	}

	public void setRefundtype(String refundtype) {
		this.refundtype = refundtype;
	}

	public Integer getShipstate() {
		return shipstate;
	}

	public void setShipstate(Integer shipstate) {
		this.shipstate = shipstate;
	}

	public Double getConfirmrefundmoney() {
		return confirmrefundmoney;
	}

	public void setConfirmrefundmoney(Double confirmrefundmoney) {
		this.confirmrefundmoney = confirmrefundmoney;
	}

	public String getConfirmrefundmoneyremark() {
		return confirmrefundmoneyremark;
	}

	public void setConfirmrefundmoneyremark(String confirmrefundmoneyremark) {
		this.confirmrefundmoneyremark = confirmrefundmoneyremark;
	}

	public Integer getOldrefundbonus() {
		return oldrefundbonus;
	}

	public void setOldrefundbonus(Integer oldrefundbonus) {
		this.oldrefundbonus = oldrefundbonus;
	}

	public Double getRefundexpressfeebymoney() {
		return refundexpressfeebymoney;
	}

	public void setRefundexpressfeebymoney(Double refundexpressfeebymoney) {
		this.refundexpressfeebymoney = refundexpressfeebymoney;
	}

	public Double getRefundexpressfeebybonus() {
		return refundexpressfeebybonus;
	}

	public void setRefundexpressfeebybonus(Double refundexpressfeebybonus) {
		this.refundexpressfeebybonus = refundexpressfeebybonus;
	}

	public Double getComrefundmoney() {
		return comrefundmoney;
	}

	public void setComrefundmoney(Double comrefundmoney) {
		this.comrefundmoney = comrefundmoney;
	}

	public Integer getComrefundbonus() {
		return comrefundbonus;
	}

	public void setComrefundbonus(Integer comrefundbonus) {
		this.comrefundbonus = comrefundbonus;
	}

	public Integer getFxhandler() {
		return fxhandler;
	}

	public void setFxhandler(Integer fxhandler) {
		this.fxhandler = fxhandler;
	}

	public String getIssysrefapproved() {
		return issysrefapproved;
	}

	public void setIssysrefapproved(String issysrefapproved) {
		this.issysrefapproved = issysrefapproved;
	}

	public String getIsopenrefundreject() {
		return isopenrefundreject;
	}

	public void setIsopenrefundreject(String isopenrefundreject) {
		this.isopenrefundreject = isopenrefundreject;
	}

	public String getIsimportcrm() {
		return isimportcrm;
	}

	public void setIsimportcrm(String isimportcrm) {
		this.isimportcrm = isimportcrm;
	}

	public Integer getFlagtype() {
		return flagtype;
	}

	public void setFlagtype(Integer flagtype) {
		this.flagtype = flagtype;
	}

	public String getIssinglerefund() {
		return issinglerefund;
	}

	public void setIssinglerefund(String issinglerefund) {
		this.issinglerefund = issinglerefund;
	}

	public String getMchid() {
		return mchid;
	}

	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	public Integer getIsnotcalcbonus() {
		return isnotcalcbonus;
	}

	public void setIsnotcalcbonus(Integer isnotcalcbonus) {
		this.isnotcalcbonus = isnotcalcbonus;
	}

	public Date getAutoagreeapplydate() {
		return autoagreeapplydate;
	}

	public void setAutoagreeapplydate(Date autoagreeapplydate) {
		this.autoagreeapplydate = autoagreeapplydate;
	}

	public Integer getCanalsyncversion() {
		return canalsyncversion;
	}

	public void setCanalsyncversion(Integer canalsyncversion) {
		this.canalsyncversion = canalsyncversion;
	}

	public Integer getUseintegral() {
		return useintegral;
	}

	public void setUseintegral(Integer useintegral) {
		this.useintegral = useintegral;
	}

	public Integer getOlduseintegral() {
		return olduseintegral;
	}

	public void setOlduseintegral(Integer olduseintegral) {
		this.olduseintegral = olduseintegral;
	}

	public Integer getUseintegraltype() {
		return useintegraltype;
	}

	public void setUseintegraltype(Integer useintegraltype) {
		this.useintegraltype = useintegraltype;
	}

	public Integer getExpressfeeuseintegral() {
		return expressfeeuseintegral;
	}

	public void setExpressfeeuseintegral(Integer expressfeeuseintegral) {
		this.expressfeeuseintegral = expressfeeuseintegral;
	}

	@Override
	public String toString() {
		return "MallSalesOrderRef{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",ewarehouseid=" + ewarehouseid +
				",selffetchshopid=" + selffetchshopid +
				",buyerid=" + buyerid +
				",orderid=" + orderid +
				",ordercode=" + ordercode +
				",refundcode=" + refundcode +
				",exchangeoriginalordercode=" + exchangeoriginalordercode +
				",returnid=" + returnid +
				",ordertime=" + ordertime +
				",refundtime=" + refundtime +
				",finishtime=" + finishtime +
				",refundmoney=" + refundmoney +
				",amountmenory=" + amountmenory +
				",giftcardrefundstatus=" + giftcardrefundstatus +
				",giftcardamountmenory=" + giftcardamountmenory +
				",refundbonus=" + refundbonus +
				",refundreason=" + refundreason +
				",refundstatus=" + refundstatus +
				",processstatus=" + processstatus +
				",refundcancellationtype=" + refundcancellationtype +
				",wxrefundstatus=" + wxrefundstatus +
				",wxrefundmsg=" + wxrefundmsg +
				",mallrefundway=" + mallrefundway +
				",malldepositrefundway=" + malldepositrefundway +
				",mallfinalrefundway=" + mallfinalrefundway +
				",tradeno=" + tradeno +
				",finaltradeno=" + finaltradeno +
				",finalwxrefundstatus=" + finalwxrefundstatus +
				",finalwxrefundmsg=" + finalwxrefundmsg +
				",iscustomerserviceremark=" + iscustomerserviceremark +
				",refundremark=" + refundremark +
				",autorefund=" + autorefund +
				",createuser=" + createuser +
				",createdate=" + createdate +
				",lastmodifieduser=" + lastmodifieduser +
				",lastmodifieddate=" + lastmodifieddate +
				",refundtype=" + refundtype +
				",shipstate=" + shipstate +
				",confirmrefundmoney=" + confirmrefundmoney +
				",confirmrefundmoneyremark=" + confirmrefundmoneyremark +
				",oldrefundbonus=" + oldrefundbonus +
				",refundexpressfeebymoney=" + refundexpressfeebymoney +
				",refundexpressfeebybonus=" + refundexpressfeebybonus +
				",comrefundmoney=" + comrefundmoney +
				",comrefundbonus=" + comrefundbonus +
				",fxhandler=" + fxhandler +
				",issysrefapproved=" + issysrefapproved +
				",isopenrefundreject=" + isopenrefundreject +
				",isimportcrm=" + isimportcrm +
				",flagtype=" + flagtype +
				",issinglerefund=" + issinglerefund +
				",mchid=" + mchid +
				",isnotcalcbonus=" + isnotcalcbonus +
				",autoagreeapplydate=" + autoagreeapplydate +
				",canalsyncversion=" + canalsyncversion +
				",useintegral=" + useintegral +
				",olduseintegral=" + olduseintegral +
				",useintegraltype=" + useintegraltype +
				",expressfeeuseintegral=" + expressfeeuseintegral +
				'}';
	}
}
