package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 商城_订单_官网订单信息_退货信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-10 22:09:01
 */
public class MallSalesOrderRt extends BaseModel implements Serializable{
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
     * 退货门店Id
     */
    private Integer shopid;

    /**
     * 
     */
    private Long buyerid;

    /**
     * 订单编号
     */
    private Long orderid;

    /**
     * 
     */
    private String ordercode;

    /**
     * 
     */
    private String returncode;

    /**
     * 换货原始订单号
     */
    private String exchangeoriginalordercode;

    /**
     * 下单时间
     */
    private Date ordertime;

    /**
     * 退货下单时间
     */
    private Date returntime;

    /**
     * 退货单完成时间
     */
    private Date finishtime;

    /**
     * 核销人
     */
    private Integer verificationuserid;

    /**
     * 核销人
     */
    private String verificationusername;

    /**
     * 退货总量
     */
    private Integer returnqty;

    /**
     * 退货总金额
     */
    private Double returnmoney;

    /**
     * 退积分
     */
    private Integer returnbonus;

    /**
     * 退货原因
     */
    private String returnreason;

    /**
     * 退货状态
     */
    private Integer returnstatus;

    /**
     * 退货类型（仅对商家收货之后） 0 退货退款 1, 换货退货
     */
    private Integer returntype;

    /**
     * 退货方式:0线上，1线下
     */
    private Integer returnway;

    /**
     * 线下退货码
     */
    private String offlinereturncode;

    /**
     * 发货状态 0:未发货 1:已发货
     */
    private Integer shipstate;

    /**
     * 退货取消类型
     */
    private Integer cancelreturntype;

    /**
     * 
     */
    private Double compostage;

    /**
     * 补积分
     */
    private Integer combonus;

    /**
     * 支付凭证
     */
    private String tradeno;

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
    private String returnremark;

    /**
     * 来源系统
     */
    private Integer outsysid;

    /**
     * 
     */
    private String issettle;

    /**
     * 是否进入CRM系统
     */
    private String isimportcrm;

    /**
     * 接入时间戳
     */
    private Long importtimestamp;

    /**
     * 是否删除
     */
    private String isdelete;

    /**
     * 
     */
    private String createuser;

    /**
     * 创建时间
     */
    private Date createdate;

    /**
     * 
     */
    private String lastmodifieduser;

    /**
     * 最后更新时间
     */
    private Date lastmodifieddate;

    /**
     * 退货单分销订单业务是否处理完成，0 未处理 1 已处理
     */
    private Integer fxhandler;

    /**
     * FormId
     */
    private String formid;

    /**
     * 是否为换货退货单
     */
    private String isexchange;

    /**
     * 换货单Id
     */
    private Long exchangeid;

    /**
     * 订单插旗类型(0=无,1=红,2=黄,4=绿,8=蓝,16=紫)
     */
    private Integer flagtype;

    /**
     * 是否整单退货 0 否  1 是
     */
    private String issinglereturn;

    /**
     * 退货待处理自动同意时间
     */
    private Date autoagreeapplydate;

    /**
     * 快递查看方式
     */
    private String expressviewby;

    /**
     * 查询微信物流主键
     */
    private String waybilltoken;

    /**
     * 
     */
    private Long blrtstorehouseid;

    /**
     * 上门取件状态，1-上门取件预约成功 2-快递公司已接单 3-快递员取件完成 NULL-已取消|默认值
     */
    private Integer pickupstatus;

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

	public Integer getShopid() {
		return shopid;
	}

	public void setShopid(Integer shopid) {
		this.shopid = shopid;
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

	public String getReturncode() {
		return returncode;
	}

	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}

	public String getExchangeoriginalordercode() {
		return exchangeoriginalordercode;
	}

	public void setExchangeoriginalordercode(String exchangeoriginalordercode) {
		this.exchangeoriginalordercode = exchangeoriginalordercode;
	}

	public Date getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}

	public Date getReturntime() {
		return returntime;
	}

	public void setReturntime(Date returntime) {
		this.returntime = returntime;
	}

	public Date getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}

	public Integer getVerificationuserid() {
		return verificationuserid;
	}

	public void setVerificationuserid(Integer verificationuserid) {
		this.verificationuserid = verificationuserid;
	}

	public String getVerificationusername() {
		return verificationusername;
	}

	public void setVerificationusername(String verificationusername) {
		this.verificationusername = verificationusername;
	}

	public Integer getReturnqty() {
		return returnqty;
	}

	public void setReturnqty(Integer returnqty) {
		this.returnqty = returnqty;
	}

	public Double getReturnmoney() {
		return returnmoney;
	}

	public void setReturnmoney(Double returnmoney) {
		this.returnmoney = returnmoney;
	}

	public Integer getReturnbonus() {
		return returnbonus;
	}

	public void setReturnbonus(Integer returnbonus) {
		this.returnbonus = returnbonus;
	}

	public String getReturnreason() {
		return returnreason;
	}

	public void setReturnreason(String returnreason) {
		this.returnreason = returnreason;
	}

	public Integer getReturnstatus() {
		return returnstatus;
	}

	public void setReturnstatus(Integer returnstatus) {
		this.returnstatus = returnstatus;
	}

	public Integer getReturntype() {
		return returntype;
	}

	public void setReturntype(Integer returntype) {
		this.returntype = returntype;
	}

	public Integer getReturnway() {
		return returnway;
	}

	public void setReturnway(Integer returnway) {
		this.returnway = returnway;
	}

	public String getOfflinereturncode() {
		return offlinereturncode;
	}

	public void setOfflinereturncode(String offlinereturncode) {
		this.offlinereturncode = offlinereturncode;
	}

	public Integer getShipstate() {
		return shipstate;
	}

	public void setShipstate(Integer shipstate) {
		this.shipstate = shipstate;
	}

	public Integer getCancelreturntype() {
		return cancelreturntype;
	}

	public void setCancelreturntype(Integer cancelreturntype) {
		this.cancelreturntype = cancelreturntype;
	}

	public Double getCompostage() {
		return compostage;
	}

	public void setCompostage(Double compostage) {
		this.compostage = compostage;
	}

	public Integer getCombonus() {
		return combonus;
	}

	public void setCombonus(Integer combonus) {
		this.combonus = combonus;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
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

	public String getReturnremark() {
		return returnremark;
	}

	public void setReturnremark(String returnremark) {
		this.returnremark = returnremark;
	}

	public Integer getOutsysid() {
		return outsysid;
	}

	public void setOutsysid(Integer outsysid) {
		this.outsysid = outsysid;
	}

	public String getIssettle() {
		return issettle;
	}

	public void setIssettle(String issettle) {
		this.issettle = issettle;
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

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
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

	public Integer getFxhandler() {
		return fxhandler;
	}

	public void setFxhandler(Integer fxhandler) {
		this.fxhandler = fxhandler;
	}

	public String getFormid() {
		return formid;
	}

	public void setFormid(String formid) {
		this.formid = formid;
	}

	public String getIsexchange() {
		return isexchange;
	}

	public void setIsexchange(String isexchange) {
		this.isexchange = isexchange;
	}

	public Long getExchangeid() {
		return exchangeid;
	}

	public void setExchangeid(Long exchangeid) {
		this.exchangeid = exchangeid;
	}

	public Integer getFlagtype() {
		return flagtype;
	}

	public void setFlagtype(Integer flagtype) {
		this.flagtype = flagtype;
	}

	public String getIssinglereturn() {
		return issinglereturn;
	}

	public void setIssinglereturn(String issinglereturn) {
		this.issinglereturn = issinglereturn;
	}

	public Date getAutoagreeapplydate() {
		return autoagreeapplydate;
	}

	public void setAutoagreeapplydate(Date autoagreeapplydate) {
		this.autoagreeapplydate = autoagreeapplydate;
	}

	public String getExpressviewby() {
		return expressviewby;
	}

	public void setExpressviewby(String expressviewby) {
		this.expressviewby = expressviewby;
	}

	public String getWaybilltoken() {
		return waybilltoken;
	}

	public void setWaybilltoken(String waybilltoken) {
		this.waybilltoken = waybilltoken;
	}

	public Long getBlrtstorehouseid() {
		return blrtstorehouseid;
	}

	public void setBlrtstorehouseid(Long blrtstorehouseid) {
		this.blrtstorehouseid = blrtstorehouseid;
	}

	public Integer getPickupstatus() {
		return pickupstatus;
	}

	public void setPickupstatus(Integer pickupstatus) {
		this.pickupstatus = pickupstatus;
	}

	@Override
	public String toString() {
		return "MallSalesOrderRt{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",ewarehouseid=" + ewarehouseid +
				",shopid=" + shopid +
				",buyerid=" + buyerid +
				",orderid=" + orderid +
				",ordercode=" + ordercode +
				",returncode=" + returncode +
				",exchangeoriginalordercode=" + exchangeoriginalordercode +
				",ordertime=" + ordertime +
				",returntime=" + returntime +
				",finishtime=" + finishtime +
				",verificationuserid=" + verificationuserid +
				",verificationusername=" + verificationusername +
				",returnqty=" + returnqty +
				",returnmoney=" + returnmoney +
				",returnbonus=" + returnbonus +
				",returnreason=" + returnreason +
				",returnstatus=" + returnstatus +
				",returntype=" + returntype +
				",returnway=" + returnway +
				",offlinereturncode=" + offlinereturncode +
				",shipstate=" + shipstate +
				",cancelreturntype=" + cancelreturntype +
				",compostage=" + compostage +
				",combonus=" + combonus +
				",tradeno=" + tradeno +
				",expresscode=" + expresscode +
				",expressorgid=" + expressorgid +
				",returnremark=" + returnremark +
				",outsysid=" + outsysid +
				",issettle=" + issettle +
				",isimportcrm=" + isimportcrm +
				",importtimestamp=" + importtimestamp +
				",isdelete=" + isdelete +
				",createuser=" + createuser +
				",createdate=" + createdate +
				",lastmodifieduser=" + lastmodifieduser +
				",lastmodifieddate=" + lastmodifieddate +
				",fxhandler=" + fxhandler +
				",formid=" + formid +
				",isexchange=" + isexchange +
				",exchangeid=" + exchangeid +
				",flagtype=" + flagtype +
				",issinglereturn=" + issinglereturn +
				",autoagreeapplydate=" + autoagreeapplydate +
				",expressviewby=" + expressviewby +
				",waybilltoken=" + waybilltoken +
				",blrtstorehouseid=" + blrtstorehouseid +
				",pickupstatus=" + pickupstatus +
				'}';
	}
}
