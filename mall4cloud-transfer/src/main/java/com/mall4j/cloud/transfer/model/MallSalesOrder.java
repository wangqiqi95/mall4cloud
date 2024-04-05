package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 商城_订单_官网订单信息

未付款，成交，发货

关联单号，如果是退货
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public class MallSalesOrder implements Serializable{
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
     * 订单类型
     */
    private String ordertype;

    /**
     * 外部渠道类型
     */
    private String outchanneltype;

    /**
     * 
     */
    private String code;

    /**
     * 外部渠道单号
     */
    private String outchannelcode;

    /**
     * 仓库Id
     */
    private Integer ewarehouseid;

    /**
     * 门店Id
     */
    private Integer eshopid;

    /**
     * 商品总量
     */
    private Integer totalqty;

    /**
     * 商品总金额
     */
    private Double totalmoney;

    /**
     * 优惠金额
     */
    private Double discountmoney;

    /**
     * 使用优惠券的总金额(订单券+商品券实际抵扣的金额)
     */
    private Double couponuseamount;

    /**
     * 
     */
    private Double discountamount;

    /**
     * 
     */
    private Integer returnquantity;

    /**
     * 
     */
    private Double returnmoney;

    /**
     * 下单时间
     */
    private Date ordertime;

    /**
     * 是否付款完成
     */
    private String ispayed;

    /**
     * 付款时间
     */
    private Date paytime;

    /**
     * 
     */
    private String prepayid;

    /**
     * 
     */
    private Integer paystatus;

    /**
     * 
     */
    private String payerrmsg;

    /**
     * 
     */
    private String tradeno;

    /**
     * 支付金额
     */
    private Double payamount;

    /**
     * 
     */
    private Integer refundstate;

    /**
     * 快递费用
     */
    private Double expressfee;

    /**
     * 运费类型(现金，积分)
     */
    private String expressfeetype;

    /**
     * 快递成本
     */
    private Double expresscost;

    /**
     * 
     */
    private String expresscode;

    /**
     * 
     */
    private Integer expressorgid;

    /**
     * 
     */
    private Integer delivtype;

    /**
     * 发货地
     */
    private Integer shiptype;

    /**
     * 自提门店编号
     */
    private Integer selffetchshopid;

    /**
     * 是否货到付款
     */
    private String iscod;

    /**
     * 货到付款服务费
     */
    private Double codservicefee;

    /**
     * 微信小程序appid（只记录小程序的appid，因为一个品牌可能有多个小程序）
     */
    private String wxopenappid;

    /**
     * 买家会员号
     */
    private Long buyerid;

    /**
     * 会员等级编号
     */
    private Integer buyergradeid;

    /**
     * 买家账号
     */
    private String buyercode;

    /**
     * 买家email
     */
    private String buyeremail;

    /**
     * 买家昵称
     */
    private String buyernick;

    /**
     * 
     */
    private String buyerremark;

    /**
     * 服务渠道
     */
    private Integer servicechannel;

    /**
     * 服务导购
     */
    private Integer servicesaler;

    /**
     * 导购总佣金
     */
    private Double salertotalrewardfee;

    /**
     * 是否统计导购销售额计算，用于统计导购销售额标记
     */
    private String issalersalecompute;

    /**
     * 异业总佣金
     */
    private Double linktotalrewardfee;

    /**
     * 异业分享人
     */
    private Integer linkshareuser;

    /**
     * 导购佣金类型
     */
    private String salerrewardfeetype;

    /**
     * 分享导购
     */
    private Integer sharesaler;

    /**
     * 收货联系人
     */
    private String recvconsignee;

    /**
     * 收货人手机
     */
    private String recvmobile;

    /**
     * 收货人电话
     */
    private String recvtel;

    /**
     * 收货人地址
     */
    private String recvaddress;

    /**
     * 收货省份
     */
    private String recvprovince;

    /**
     * 收货城市
     */
    private String recvcity;

    /**
     * 收货区县
     */
    private String recvcounty;

    /**
     * 是否分销结算
     */
    private String issettle;

    /**
     * 
     */
    private String isfeecount;

    /**
     * 订单状态
     */
    private Integer orderstatus;

    /**
     * 发货时间
     */
    private Date hassendtime;

    /**
     * 完成时间
     */
    private Date finishtime;

    /**
     * 
     */
    private String orderremark;

    /**
     * 数据来源
     */
    private Integer dataorigin;

    /**
     * 来源系统
     */
    private Integer outsysid;

    /**
     * 来源系统
     */
    private String fromsystem;

    /**
     * 分销员编号
     */
    private Long sellerid;

    /**
     * 分销员所属系统门店
     */
    private Integer sellersysshopid;

    /**
     * 计算佣金
     */
    private Double rewardfee;

    /**
     * 
     */
    private Integer usebonus;

    /**
     * 使用券编号
     */
    private Long usecouponid;

    /**
     * 订单商品积分兑换活动（只有商城订单记录）
     */
    private Integer bonusactid;

    /**
     * 套餐一口价活动Id
     */
    private Integer packactid;

    /**
     * 
     */
    private Integer calcbonus;

    /**
     * 
     */
    private String iscommented;

    /**
     * 是否进入CRM系统
     */
    private String isimportcrm;

    /**
     * 接入时间戳
     */
    private Long importtimestamp;

    /**
     * 创建时间
     */
    private Date createdate;

    /**
     * 最后更新时间
     */
    private Date lastmodifieddate;

    /**
     * 
     */
    private String issplit;

    /**
     * 订单评价状态：0：未评价；1：已初评；2：已追评
     */
    private Integer ratestatus;

    /**
     * canal同步用字段
     */
    private Integer canalsyncversion;

    /**
     * 运费积分
     */
    private Integer expressbonus;

    /**
     * 开票记录状态 0：没有记录，1：已产生开票记录
     */
    private Integer applyinvoicerecord;

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

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public String getOutchanneltype() {
		return outchanneltype;
	}

	public void setOutchanneltype(String outchanneltype) {
		this.outchanneltype = outchanneltype;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOutchannelcode() {
		return outchannelcode;
	}

	public void setOutchannelcode(String outchannelcode) {
		this.outchannelcode = outchannelcode;
	}

	public Integer getEwarehouseid() {
		return ewarehouseid;
	}

	public void setEwarehouseid(Integer ewarehouseid) {
		this.ewarehouseid = ewarehouseid;
	}

	public Integer getEshopid() {
		return eshopid;
	}

	public void setEshopid(Integer eshopid) {
		this.eshopid = eshopid;
	}

	public Integer getTotalqty() {
		return totalqty;
	}

	public void setTotalqty(Integer totalqty) {
		this.totalqty = totalqty;
	}

	public Double getTotalmoney() {
		return totalmoney;
	}

	public void setTotalmoney(Double totalmoney) {
		this.totalmoney = totalmoney;
	}

	public Double getDiscountmoney() {
		return discountmoney;
	}

	public void setDiscountmoney(Double discountmoney) {
		this.discountmoney = discountmoney;
	}

	public Double getCouponuseamount() {
		return couponuseamount;
	}

	public void setCouponuseamount(Double couponuseamount) {
		this.couponuseamount = couponuseamount;
	}

	public Double getDiscountamount() {
		return discountamount;
	}

	public void setDiscountamount(Double discountamount) {
		this.discountamount = discountamount;
	}

	public Integer getReturnquantity() {
		return returnquantity;
	}

	public void setReturnquantity(Integer returnquantity) {
		this.returnquantity = returnquantity;
	}

	public Double getReturnmoney() {
		return returnmoney;
	}

	public void setReturnmoney(Double returnmoney) {
		this.returnmoney = returnmoney;
	}

	public Date getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}

	public String getIspayed() {
		return ispayed;
	}

	public void setIspayed(String ispayed) {
		this.ispayed = ispayed;
	}

	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public Integer getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(Integer paystatus) {
		this.paystatus = paystatus;
	}

	public String getPayerrmsg() {
		return payerrmsg;
	}

	public void setPayerrmsg(String payerrmsg) {
		this.payerrmsg = payerrmsg;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public Double getPayamount() {
		return payamount;
	}

	public void setPayamount(Double payamount) {
		this.payamount = payamount;
	}

	public Integer getRefundstate() {
		return refundstate;
	}

	public void setRefundstate(Integer refundstate) {
		this.refundstate = refundstate;
	}

	public Double getExpressfee() {
		return expressfee;
	}

	public void setExpressfee(Double expressfee) {
		this.expressfee = expressfee;
	}

	public String getExpressfeetype() {
		return expressfeetype;
	}

	public void setExpressfeetype(String expressfeetype) {
		this.expressfeetype = expressfeetype;
	}

	public Double getExpresscost() {
		return expresscost;
	}

	public void setExpresscost(Double expresscost) {
		this.expresscost = expresscost;
	}

	public String getExpresscode() {
		return expresscode;
	}

	public void setExpresscode(String expresscode) {
		this.expresscode = expresscode;
	}

	public Integer getExpressorgid() {
		return expressorgid;
	}

	public void setExpressorgid(Integer expressorgid) {
		this.expressorgid = expressorgid;
	}

	public Integer getDelivtype() {
		return delivtype;
	}

	public void setDelivtype(Integer delivtype) {
		this.delivtype = delivtype;
	}

	public Integer getShiptype() {
		return shiptype;
	}

	public void setShiptype(Integer shiptype) {
		this.shiptype = shiptype;
	}

	public Integer getSelffetchshopid() {
		return selffetchshopid;
	}

	public void setSelffetchshopid(Integer selffetchshopid) {
		this.selffetchshopid = selffetchshopid;
	}

	public String getIscod() {
		return iscod;
	}

	public void setIscod(String iscod) {
		this.iscod = iscod;
	}

	public Double getCodservicefee() {
		return codservicefee;
	}

	public void setCodservicefee(Double codservicefee) {
		this.codservicefee = codservicefee;
	}

	public String getWxopenappid() {
		return wxopenappid;
	}

	public void setWxopenappid(String wxopenappid) {
		this.wxopenappid = wxopenappid;
	}

	public Long getBuyerid() {
		return buyerid;
	}

	public void setBuyerid(Long buyerid) {
		this.buyerid = buyerid;
	}

	public Integer getBuyergradeid() {
		return buyergradeid;
	}

	public void setBuyergradeid(Integer buyergradeid) {
		this.buyergradeid = buyergradeid;
	}

	public String getBuyercode() {
		return buyercode;
	}

	public void setBuyercode(String buyercode) {
		this.buyercode = buyercode;
	}

	public String getBuyeremail() {
		return buyeremail;
	}

	public void setBuyeremail(String buyeremail) {
		this.buyeremail = buyeremail;
	}

	public String getBuyernick() {
		return buyernick;
	}

	public void setBuyernick(String buyernick) {
		this.buyernick = buyernick;
	}

	public String getBuyerremark() {
		return buyerremark;
	}

	public void setBuyerremark(String buyerremark) {
		this.buyerremark = buyerremark;
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

	public Double getSalertotalrewardfee() {
		return salertotalrewardfee;
	}

	public void setSalertotalrewardfee(Double salertotalrewardfee) {
		this.salertotalrewardfee = salertotalrewardfee;
	}

	public String getIssalersalecompute() {
		return issalersalecompute;
	}

	public void setIssalersalecompute(String issalersalecompute) {
		this.issalersalecompute = issalersalecompute;
	}

	public Double getLinktotalrewardfee() {
		return linktotalrewardfee;
	}

	public void setLinktotalrewardfee(Double linktotalrewardfee) {
		this.linktotalrewardfee = linktotalrewardfee;
	}

	public Integer getLinkshareuser() {
		return linkshareuser;
	}

	public void setLinkshareuser(Integer linkshareuser) {
		this.linkshareuser = linkshareuser;
	}

	public String getSalerrewardfeetype() {
		return salerrewardfeetype;
	}

	public void setSalerrewardfeetype(String salerrewardfeetype) {
		this.salerrewardfeetype = salerrewardfeetype;
	}

	public Integer getSharesaler() {
		return sharesaler;
	}

	public void setSharesaler(Integer sharesaler) {
		this.sharesaler = sharesaler;
	}

	public String getRecvconsignee() {
		return recvconsignee;
	}

	public void setRecvconsignee(String recvconsignee) {
		this.recvconsignee = recvconsignee;
	}

	public String getRecvmobile() {
		return recvmobile;
	}

	public void setRecvmobile(String recvmobile) {
		this.recvmobile = recvmobile;
	}

	public String getRecvtel() {
		return recvtel;
	}

	public void setRecvtel(String recvtel) {
		this.recvtel = recvtel;
	}

	public String getRecvaddress() {
		return recvaddress;
	}

	public void setRecvaddress(String recvaddress) {
		this.recvaddress = recvaddress;
	}

	public String getRecvprovince() {
		return recvprovince;
	}

	public void setRecvprovince(String recvprovince) {
		this.recvprovince = recvprovince;
	}

	public String getRecvcity() {
		return recvcity;
	}

	public void setRecvcity(String recvcity) {
		this.recvcity = recvcity;
	}

	public String getRecvcounty() {
		return recvcounty;
	}

	public void setRecvcounty(String recvcounty) {
		this.recvcounty = recvcounty;
	}

	public String getIssettle() {
		return issettle;
	}

	public void setIssettle(String issettle) {
		this.issettle = issettle;
	}

	public String getIsfeecount() {
		return isfeecount;
	}

	public void setIsfeecount(String isfeecount) {
		this.isfeecount = isfeecount;
	}

	public Integer getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(Integer orderstatus) {
		this.orderstatus = orderstatus;
	}

	public Date getHassendtime() {
		return hassendtime;
	}

	public void setHassendtime(Date hassendtime) {
		this.hassendtime = hassendtime;
	}

	public Date getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}

	public String getOrderremark() {
		return orderremark;
	}

	public void setOrderremark(String orderremark) {
		this.orderremark = orderremark;
	}

	public Integer getDataorigin() {
		return dataorigin;
	}

	public void setDataorigin(Integer dataorigin) {
		this.dataorigin = dataorigin;
	}

	public Integer getOutsysid() {
		return outsysid;
	}

	public void setOutsysid(Integer outsysid) {
		this.outsysid = outsysid;
	}

	public String getFromsystem() {
		return fromsystem;
	}

	public void setFromsystem(String fromsystem) {
		this.fromsystem = fromsystem;
	}

	public Long getSellerid() {
		return sellerid;
	}

	public void setSellerid(Long sellerid) {
		this.sellerid = sellerid;
	}

	public Integer getSellersysshopid() {
		return sellersysshopid;
	}

	public void setSellersysshopid(Integer sellersysshopid) {
		this.sellersysshopid = sellersysshopid;
	}

	public Double getRewardfee() {
		return rewardfee;
	}

	public void setRewardfee(Double rewardfee) {
		this.rewardfee = rewardfee;
	}

	public Integer getUsebonus() {
		return usebonus;
	}

	public void setUsebonus(Integer usebonus) {
		this.usebonus = usebonus;
	}

	public Long getUsecouponid() {
		return usecouponid;
	}

	public void setUsecouponid(Long usecouponid) {
		this.usecouponid = usecouponid;
	}

	public Integer getBonusactid() {
		return bonusactid;
	}

	public void setBonusactid(Integer bonusactid) {
		this.bonusactid = bonusactid;
	}

	public Integer getPackactid() {
		return packactid;
	}

	public void setPackactid(Integer packactid) {
		this.packactid = packactid;
	}

	public Integer getCalcbonus() {
		return calcbonus;
	}

	public void setCalcbonus(Integer calcbonus) {
		this.calcbonus = calcbonus;
	}

	public String getIscommented() {
		return iscommented;
	}

	public void setIscommented(String iscommented) {
		this.iscommented = iscommented;
	}

	public String getIsimportcrm() {
		return isimportcrm;
	}

	public void setIsimportcrm(String isimportcrm) {
		this.isimportcrm = isimportcrm;
	}

	public Long getImporttimestamp() {
		return importtimestamp;
	}

	public void setImporttimestamp(Long importtimestamp) {
		this.importtimestamp = importtimestamp;
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

	public String getIssplit() {
		return issplit;
	}

	public void setIssplit(String issplit) {
		this.issplit = issplit;
	}

	public Integer getRatestatus() {
		return ratestatus;
	}

	public void setRatestatus(Integer ratestatus) {
		this.ratestatus = ratestatus;
	}

	public Integer getCanalsyncversion() {
		return canalsyncversion;
	}

	public void setCanalsyncversion(Integer canalsyncversion) {
		this.canalsyncversion = canalsyncversion;
	}

	public Integer getExpressbonus() {
		return expressbonus;
	}

	public void setExpressbonus(Integer expressbonus) {
		this.expressbonus = expressbonus;
	}

	public Integer getApplyinvoicerecord() {
		return applyinvoicerecord;
	}

	public void setApplyinvoicerecord(Integer applyinvoicerecord) {
		this.applyinvoicerecord = applyinvoicerecord;
	}

	@Override
	public String toString() {
		return "MallSalesOrder{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",ordertype=" + ordertype +
				",outchanneltype=" + outchanneltype +
				",code=" + code +
				",outchannelcode=" + outchannelcode +
				",ewarehouseid=" + ewarehouseid +
				",eshopid=" + eshopid +
				",totalqty=" + totalqty +
				",totalmoney=" + totalmoney +
				",discountmoney=" + discountmoney +
				",couponuseamount=" + couponuseamount +
				",discountamount=" + discountamount +
				",returnquantity=" + returnquantity +
				",returnmoney=" + returnmoney +
				",ordertime=" + ordertime +
				",ispayed=" + ispayed +
				",paytime=" + paytime +
				",prepayid=" + prepayid +
				",paystatus=" + paystatus +
				",payerrmsg=" + payerrmsg +
				",tradeno=" + tradeno +
				",payamount=" + payamount +
				",refundstate=" + refundstate +
				",expressfee=" + expressfee +
				",expressfeetype=" + expressfeetype +
				",expresscost=" + expresscost +
				",expresscode=" + expresscode +
				",expressorgid=" + expressorgid +
				",delivtype=" + delivtype +
				",shiptype=" + shiptype +
				",selffetchshopid=" + selffetchshopid +
				",iscod=" + iscod +
				",codservicefee=" + codservicefee +
				",wxopenappid=" + wxopenappid +
				",buyerid=" + buyerid +
				",buyergradeid=" + buyergradeid +
				",buyercode=" + buyercode +
				",buyeremail=" + buyeremail +
				",buyernick=" + buyernick +
				",buyerremark=" + buyerremark +
				",servicechannel=" + servicechannel +
				",servicesaler=" + servicesaler +
				",salertotalrewardfee=" + salertotalrewardfee +
				",issalersalecompute=" + issalersalecompute +
				",linktotalrewardfee=" + linktotalrewardfee +
				",linkshareuser=" + linkshareuser +
				",salerrewardfeetype=" + salerrewardfeetype +
				",sharesaler=" + sharesaler +
				",recvconsignee=" + recvconsignee +
				",recvmobile=" + recvmobile +
				",recvtel=" + recvtel +
				",recvaddress=" + recvaddress +
				",recvprovince=" + recvprovince +
				",recvcity=" + recvcity +
				",recvcounty=" + recvcounty +
				",issettle=" + issettle +
				",isfeecount=" + isfeecount +
				",orderstatus=" + orderstatus +
				",hassendtime=" + hassendtime +
				",finishtime=" + finishtime +
				",orderremark=" + orderremark +
				",dataorigin=" + dataorigin +
				",outsysid=" + outsysid +
				",fromsystem=" + fromsystem +
				",sellerid=" + sellerid +
				",sellersysshopid=" + sellersysshopid +
				",rewardfee=" + rewardfee +
				",usebonus=" + usebonus +
				",usecouponid=" + usecouponid +
				",bonusactid=" + bonusactid +
				",packactid=" + packactid +
				",calcbonus=" + calcbonus +
				",iscommented=" + iscommented +
				",isimportcrm=" + isimportcrm +
				",importtimestamp=" + importtimestamp +
				",createdate=" + createdate +
				",lastmodifieddate=" + lastmodifieddate +
				",issplit=" + issplit +
				",ratestatus=" + ratestatus +
				",canalsyncversion=" + canalsyncversion +
				",expressbonus=" + expressbonus +
				",applyinvoicerecord=" + applyinvoicerecord +
				'}';
	}
}
