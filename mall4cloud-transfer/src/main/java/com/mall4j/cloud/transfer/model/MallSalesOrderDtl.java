package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 商城_订单_官网订单信息_商品明细

佣金按商品计算
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public class MallSalesOrderDtl implements Serializable{
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
     * 订单编号
     */
    private Long orderid;

    /**
     * 商品货号
     */
    private Long itemid;

    /**
     * 商品条码编号
     */
    private Long barid;

    /**
     * 商品条码
     */
    private String barcode;

    /**
     * 原始单价
     */
    private Double priceoriginal;

    /**
     * 销售单价
     */
    private Double pricesell;

    /**
     * 采购价
     */
    private Double purchaseprice;

    /**
     * 订购数量
     */
    private Integer quantity;

    /**
     * 订购金额
     */
    private Double amount;

    /**
     * 
     */
    private Double avgamount;

    /**
     * 商品所属品牌
     */
    private Integer productbrandid;

    /**
     *  外部平台SkuId
     */
    private Long skuoriginid;

    /**
     * 外部平台商品Id
     */
    private Long itemoriginid;

    /**
     * 外部平台sku尺码Id
     */
    private Long skusizeid;

    /**
     * 折扣金额
     */
    private Double discountmoney;

    /**
     * 支付优惠金额(抵用掉商品券)
     */
    private Double discountamount;

    /**
     * 均摊到的商品券抵扣额
     */
    private Double procouponamount;

    /**
     * 均摊到的订单券抵扣额
     */
    private Double ordercouponamount;

    /**
     * 折扣
     */
    private Double discount;

    /**
     * 导购佣金比率
     */
    private Double salerrewardfeerate;

    /**
     * 导购佣金
     */
    private Double salerrewardfee;

    /**
     * 异业佣金比率
     */
    private Double linkrewardfeerate;

    /**
     * 异业佣金
     */
    private Double linkrewardfee;

    /**
     * 退货异业佣金
     */
    private Double retlinkrewardfee;

    /**
     * 退货导购佣金
     */
    private Double retsalerrewardfee;

    /**
     * 是否赠品
     */
    private String isgift;

    /**
     * 赠品：GS，加价购：EX
     */
    private String gifttype;

    /**
     * 
     */
    private String isgroupbuy;

    /**
     * 
     */
    private Integer groupbuyid;

    /**
     * 折扣活动编号
     */
    private Integer discountactid;

    /**
     * 是否为外部库存
     */
    private String isoutstock;

    /**
     * 退货数量
     */
    private Integer returnquantity;

    /**
     * 
     */
    private Double returnprice;

    /**
     * 
     */
    private Double returnmoney;

    /**
     * 
     */
    private Long returnid;

    /**
     * 
     */
    private Double returnrewardfee;

    /**
     * 计算佣金
     */
    private Double rewardfee;

    /**
     * 计算规则编号
     */
    private Long rewardruleid;

    /**
     * 抵现积分
     */
    private Integer usebonus;

    /**
     * 使用券编号
     */
    private Long usecouponid;

    /**
     * 
     */
    private Integer calcbonus;

    /**
     * 
     */
    private Long commentid;

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
    private Integer expressorgid;

    /**
     * 快递单号
     */
    private String expresscode;

    /**
     * 
     */
    private Long refundid;

    /**
     * 发货时间
     */
    private Date hassendtime;

    /**
     * 订单发货地
     */
    private Integer shiptype;

    /**
     * 
     */
    private Double retailprice;

    /**
     * 分享人Id
     */
    private Long sharepersonid;

    /**
     * 分享人类型
     */
    private Integer sharepersontype;

    /**
     * 外部订单编号
     */
    private String outordercode;

    /**
     * 外部订单编号Ext1
     */
    private String outordercodeext1;

    /**
     * 微信优惠券商品券抵扣额
     */
    private Double wxprocouponamount;

    /**
     * 微信优惠券订单券抵扣额
     */
    private Double wxordercouponamount;

    /**
     * 储值卡均摊金额
     */
    private Double prepaidcardamount;

    /**
     * 可退款金额,sku退款会更新这个字段
     */
    private Double allowrefundamount;

    /**
     * 已发货数量
     */
    private Integer expressqty;

    /**
     * 赠品所在活动Id
     */
    private Integer giftactid;

    /**
     * 均摊的抹零优惠金额
     */
    private Double orderwipezeroamount;

    /**
     * 推广来源：0 默认  1 机器人群推广
     */
    private Integer promotionsource;

    /**
     * 推广来源Id
     */
    private Long psid;

    /**
     * 商品货号
     */
    private String itemno;

    /**
     * 商品名称
     */
    private String itemname;

    /**
     * 会员ID
     */
    private Long vipid;

    /**
     * 销售属性值列表冗余
     */
    private String skuattrvalsex;

    /**
     * 图片url冗余
     */
    private String pictureurl;

    /**
     * 唯代购佣金
     */
    private Double vipdaigourewardfee;

    /**
     * 活动ID
     */
    private Integer actid;

    /**
     * 活动类型
     */
    private Integer acttype;

    /**
     * 会员权益折扣优惠金额
     */
    private Double vipdiscountamount;

    /**
     * 会员权益折扣优惠金额余数
     */
    private Double vipdiscountamountbalance;

    /**
     * 是否推送支付宝分摊
     */
    private String alipaypushsharecoupon;

    /**
     * 支付宝惠金额
     */
    private Double alipaycouponamount;

    /**
     * 限时优惠优惠金额
     */
    private Double timelimiteddiscountamount;

    /**
     * 积分抵现使用积分
     */
    private Integer useintegral;

    /**
     * 积分抵现金额
     */
    private Double useintegralmoney;

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

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public Long getBarid() {
		return barid;
	}

	public void setBarid(Long barid) {
		this.barid = barid;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Double getPriceoriginal() {
		return priceoriginal;
	}

	public void setPriceoriginal(Double priceoriginal) {
		this.priceoriginal = priceoriginal;
	}

	public Double getPricesell() {
		return pricesell;
	}

	public void setPricesell(Double pricesell) {
		this.pricesell = pricesell;
	}

	public Double getPurchaseprice() {
		return purchaseprice;
	}

	public void setPurchaseprice(Double purchaseprice) {
		this.purchaseprice = purchaseprice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAvgamount() {
		return avgamount;
	}

	public void setAvgamount(Double avgamount) {
		this.avgamount = avgamount;
	}

	public Integer getProductbrandid() {
		return productbrandid;
	}

	public void setProductbrandid(Integer productbrandid) {
		this.productbrandid = productbrandid;
	}

	public Long getSkuoriginid() {
		return skuoriginid;
	}

	public void setSkuoriginid(Long skuoriginid) {
		this.skuoriginid = skuoriginid;
	}

	public Long getItemoriginid() {
		return itemoriginid;
	}

	public void setItemoriginid(Long itemoriginid) {
		this.itemoriginid = itemoriginid;
	}

	public Long getSkusizeid() {
		return skusizeid;
	}

	public void setSkusizeid(Long skusizeid) {
		this.skusizeid = skusizeid;
	}

	public Double getDiscountmoney() {
		return discountmoney;
	}

	public void setDiscountmoney(Double discountmoney) {
		this.discountmoney = discountmoney;
	}

	public Double getDiscountamount() {
		return discountamount;
	}

	public void setDiscountamount(Double discountamount) {
		this.discountamount = discountamount;
	}

	public Double getProcouponamount() {
		return procouponamount;
	}

	public void setProcouponamount(Double procouponamount) {
		this.procouponamount = procouponamount;
	}

	public Double getOrdercouponamount() {
		return ordercouponamount;
	}

	public void setOrdercouponamount(Double ordercouponamount) {
		this.ordercouponamount = ordercouponamount;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getSalerrewardfeerate() {
		return salerrewardfeerate;
	}

	public void setSalerrewardfeerate(Double salerrewardfeerate) {
		this.salerrewardfeerate = salerrewardfeerate;
	}

	public Double getSalerrewardfee() {
		return salerrewardfee;
	}

	public void setSalerrewardfee(Double salerrewardfee) {
		this.salerrewardfee = salerrewardfee;
	}

	public Double getLinkrewardfeerate() {
		return linkrewardfeerate;
	}

	public void setLinkrewardfeerate(Double linkrewardfeerate) {
		this.linkrewardfeerate = linkrewardfeerate;
	}

	public Double getLinkrewardfee() {
		return linkrewardfee;
	}

	public void setLinkrewardfee(Double linkrewardfee) {
		this.linkrewardfee = linkrewardfee;
	}

	public Double getRetlinkrewardfee() {
		return retlinkrewardfee;
	}

	public void setRetlinkrewardfee(Double retlinkrewardfee) {
		this.retlinkrewardfee = retlinkrewardfee;
	}

	public Double getRetsalerrewardfee() {
		return retsalerrewardfee;
	}

	public void setRetsalerrewardfee(Double retsalerrewardfee) {
		this.retsalerrewardfee = retsalerrewardfee;
	}

	public String getIsgift() {
		return isgift;
	}

	public void setIsgift(String isgift) {
		this.isgift = isgift;
	}

	public String getGifttype() {
		return gifttype;
	}

	public void setGifttype(String gifttype) {
		this.gifttype = gifttype;
	}

	public String getIsgroupbuy() {
		return isgroupbuy;
	}

	public void setIsgroupbuy(String isgroupbuy) {
		this.isgroupbuy = isgroupbuy;
	}

	public Integer getGroupbuyid() {
		return groupbuyid;
	}

	public void setGroupbuyid(Integer groupbuyid) {
		this.groupbuyid = groupbuyid;
	}

	public Integer getDiscountactid() {
		return discountactid;
	}

	public void setDiscountactid(Integer discountactid) {
		this.discountactid = discountactid;
	}

	public String getIsoutstock() {
		return isoutstock;
	}

	public void setIsoutstock(String isoutstock) {
		this.isoutstock = isoutstock;
	}

	public Integer getReturnquantity() {
		return returnquantity;
	}

	public void setReturnquantity(Integer returnquantity) {
		this.returnquantity = returnquantity;
	}

	public Double getReturnprice() {
		return returnprice;
	}

	public void setReturnprice(Double returnprice) {
		this.returnprice = returnprice;
	}

	public Double getReturnmoney() {
		return returnmoney;
	}

	public void setReturnmoney(Double returnmoney) {
		this.returnmoney = returnmoney;
	}

	public Long getReturnid() {
		return returnid;
	}

	public void setReturnid(Long returnid) {
		this.returnid = returnid;
	}

	public Double getReturnrewardfee() {
		return returnrewardfee;
	}

	public void setReturnrewardfee(Double returnrewardfee) {
		this.returnrewardfee = returnrewardfee;
	}

	public Double getRewardfee() {
		return rewardfee;
	}

	public void setRewardfee(Double rewardfee) {
		this.rewardfee = rewardfee;
	}

	public Long getRewardruleid() {
		return rewardruleid;
	}

	public void setRewardruleid(Long rewardruleid) {
		this.rewardruleid = rewardruleid;
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

	public Integer getCalcbonus() {
		return calcbonus;
	}

	public void setCalcbonus(Integer calcbonus) {
		this.calcbonus = calcbonus;
	}

	public Long getCommentid() {
		return commentid;
	}

	public void setCommentid(Long commentid) {
		this.commentid = commentid;
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

	public Integer getExpressorgid() {
		return expressorgid;
	}

	public void setExpressorgid(Integer expressorgid) {
		this.expressorgid = expressorgid;
	}

	public String getExpresscode() {
		return expresscode;
	}

	public void setExpresscode(String expresscode) {
		this.expresscode = expresscode;
	}

	public Long getRefundid() {
		return refundid;
	}

	public void setRefundid(Long refundid) {
		this.refundid = refundid;
	}

	public Date getHassendtime() {
		return hassendtime;
	}

	public void setHassendtime(Date hassendtime) {
		this.hassendtime = hassendtime;
	}

	public Integer getShiptype() {
		return shiptype;
	}

	public void setShiptype(Integer shiptype) {
		this.shiptype = shiptype;
	}

	public Double getRetailprice() {
		return retailprice;
	}

	public void setRetailprice(Double retailprice) {
		this.retailprice = retailprice;
	}

	public Long getSharepersonid() {
		return sharepersonid;
	}

	public void setSharepersonid(Long sharepersonid) {
		this.sharepersonid = sharepersonid;
	}

	public Integer getSharepersontype() {
		return sharepersontype;
	}

	public void setSharepersontype(Integer sharepersontype) {
		this.sharepersontype = sharepersontype;
	}

	public String getOutordercode() {
		return outordercode;
	}

	public void setOutordercode(String outordercode) {
		this.outordercode = outordercode;
	}

	public String getOutordercodeext1() {
		return outordercodeext1;
	}

	public void setOutordercodeext1(String outordercodeext1) {
		this.outordercodeext1 = outordercodeext1;
	}

	public Double getWxprocouponamount() {
		return wxprocouponamount;
	}

	public void setWxprocouponamount(Double wxprocouponamount) {
		this.wxprocouponamount = wxprocouponamount;
	}

	public Double getWxordercouponamount() {
		return wxordercouponamount;
	}

	public void setWxordercouponamount(Double wxordercouponamount) {
		this.wxordercouponamount = wxordercouponamount;
	}

	public Double getPrepaidcardamount() {
		return prepaidcardamount;
	}

	public void setPrepaidcardamount(Double prepaidcardamount) {
		this.prepaidcardamount = prepaidcardamount;
	}

	public Double getAllowrefundamount() {
		return allowrefundamount;
	}

	public void setAllowrefundamount(Double allowrefundamount) {
		this.allowrefundamount = allowrefundamount;
	}

	public Integer getExpressqty() {
		return expressqty;
	}

	public void setExpressqty(Integer expressqty) {
		this.expressqty = expressqty;
	}

	public Integer getGiftactid() {
		return giftactid;
	}

	public void setGiftactid(Integer giftactid) {
		this.giftactid = giftactid;
	}

	public Double getOrderwipezeroamount() {
		return orderwipezeroamount;
	}

	public void setOrderwipezeroamount(Double orderwipezeroamount) {
		this.orderwipezeroamount = orderwipezeroamount;
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

	public String getItemno() {
		return itemno;
	}

	public void setItemno(String itemno) {
		this.itemno = itemno;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public Long getVipid() {
		return vipid;
	}

	public void setVipid(Long vipid) {
		this.vipid = vipid;
	}

	public String getSkuattrvalsex() {
		return skuattrvalsex;
	}

	public void setSkuattrvalsex(String skuattrvalsex) {
		this.skuattrvalsex = skuattrvalsex;
	}

	public String getPictureurl() {
		return pictureurl;
	}

	public void setPictureurl(String pictureurl) {
		this.pictureurl = pictureurl;
	}

	public Double getVipdaigourewardfee() {
		return vipdaigourewardfee;
	}

	public void setVipdaigourewardfee(Double vipdaigourewardfee) {
		this.vipdaigourewardfee = vipdaigourewardfee;
	}

	public Integer getActid() {
		return actid;
	}

	public void setActid(Integer actid) {
		this.actid = actid;
	}

	public Integer getActtype() {
		return acttype;
	}

	public void setActtype(Integer acttype) {
		this.acttype = acttype;
	}

	public Double getVipdiscountamount() {
		return vipdiscountamount;
	}

	public void setVipdiscountamount(Double vipdiscountamount) {
		this.vipdiscountamount = vipdiscountamount;
	}

	public Double getVipdiscountamountbalance() {
		return vipdiscountamountbalance;
	}

	public void setVipdiscountamountbalance(Double vipdiscountamountbalance) {
		this.vipdiscountamountbalance = vipdiscountamountbalance;
	}

	public String getAlipaypushsharecoupon() {
		return alipaypushsharecoupon;
	}

	public void setAlipaypushsharecoupon(String alipaypushsharecoupon) {
		this.alipaypushsharecoupon = alipaypushsharecoupon;
	}

	public Double getAlipaycouponamount() {
		return alipaycouponamount;
	}

	public void setAlipaycouponamount(Double alipaycouponamount) {
		this.alipaycouponamount = alipaycouponamount;
	}

	public Double getTimelimiteddiscountamount() {
		return timelimiteddiscountamount;
	}

	public void setTimelimiteddiscountamount(Double timelimiteddiscountamount) {
		this.timelimiteddiscountamount = timelimiteddiscountamount;
	}

	public Integer getUseintegral() {
		return useintegral;
	}

	public void setUseintegral(Integer useintegral) {
		this.useintegral = useintegral;
	}

	public Double getUseintegralmoney() {
		return useintegralmoney;
	}

	public void setUseintegralmoney(Double useintegralmoney) {
		this.useintegralmoney = useintegralmoney;
	}

	@Override
	public String toString() {
		return "MallSalesOrderDtl{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",orderid=" + orderid +
				",itemid=" + itemid +
				",barid=" + barid +
				",barcode=" + barcode +
				",priceoriginal=" + priceoriginal +
				",pricesell=" + pricesell +
				",purchaseprice=" + purchaseprice +
				",quantity=" + quantity +
				",amount=" + amount +
				",avgamount=" + avgamount +
				",productbrandid=" + productbrandid +
				",skuoriginid=" + skuoriginid +
				",itemoriginid=" + itemoriginid +
				",skusizeid=" + skusizeid +
				",discountmoney=" + discountmoney +
				",discountamount=" + discountamount +
				",procouponamount=" + procouponamount +
				",ordercouponamount=" + ordercouponamount +
				",discount=" + discount +
				",salerrewardfeerate=" + salerrewardfeerate +
				",salerrewardfee=" + salerrewardfee +
				",linkrewardfeerate=" + linkrewardfeerate +
				",linkrewardfee=" + linkrewardfee +
				",retlinkrewardfee=" + retlinkrewardfee +
				",retsalerrewardfee=" + retsalerrewardfee +
				",isgift=" + isgift +
				",gifttype=" + gifttype +
				",isgroupbuy=" + isgroupbuy +
				",groupbuyid=" + groupbuyid +
				",discountactid=" + discountactid +
				",isoutstock=" + isoutstock +
				",returnquantity=" + returnquantity +
				",returnprice=" + returnprice +
				",returnmoney=" + returnmoney +
				",returnid=" + returnid +
				",returnrewardfee=" + returnrewardfee +
				",rewardfee=" + rewardfee +
				",rewardruleid=" + rewardruleid +
				",usebonus=" + usebonus +
				",usecouponid=" + usecouponid +
				",calcbonus=" + calcbonus +
				",commentid=" + commentid +
				",createdate=" + createdate +
				",lastmodifieddate=" + lastmodifieddate +
				",expressorgid=" + expressorgid +
				",expresscode=" + expresscode +
				",refundid=" + refundid +
				",hassendtime=" + hassendtime +
				",shiptype=" + shiptype +
				",retailprice=" + retailprice +
				",sharepersonid=" + sharepersonid +
				",sharepersontype=" + sharepersontype +
				",outordercode=" + outordercode +
				",outordercodeext1=" + outordercodeext1 +
				",wxprocouponamount=" + wxprocouponamount +
				",wxordercouponamount=" + wxordercouponamount +
				",prepaidcardamount=" + prepaidcardamount +
				",allowrefundamount=" + allowrefundamount +
				",expressqty=" + expressqty +
				",giftactid=" + giftactid +
				",orderwipezeroamount=" + orderwipezeroamount +
				",promotionsource=" + promotionsource +
				",psid=" + psid +
				",itemno=" + itemno +
				",itemname=" + itemname +
				",vipid=" + vipid +
				",skuattrvalsex=" + skuattrvalsex +
				",pictureurl=" + pictureurl +
				",vipdaigourewardfee=" + vipdaigourewardfee +
				",actid=" + actid +
				",acttype=" + acttype +
				",vipdiscountamount=" + vipdiscountamount +
				",vipdiscountamountbalance=" + vipdiscountamountbalance +
				",alipaypushsharecoupon=" + alipaypushsharecoupon +
				",alipaycouponamount=" + alipaycouponamount +
				",timelimiteddiscountamount=" + timelimiteddiscountamount +
				",useintegral=" + useintegral +
				",useintegralmoney=" + useintegralmoney +
				'}';
	}
}
