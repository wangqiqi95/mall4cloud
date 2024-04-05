package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销人员提现记录表
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:30
 */
public class MallFxCashRequest extends BaseModel implements Serializable{
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
     * 分销人员Id
     */
    private Long fxid;

    /**
     * 分销类型:0 导购;1 会员;2 合伙人
     */
    private Integer fxtype;

    /**
     * 提现金额
     */
    private Double amount;

    /**
     * 状态：0，待审核，10,未通过，20，待转账，30，转账成功
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 提现单号
     */
    private String requestno;

    /**
     * 提现时间
     */
    private Date requesttime;

    /**
     * 审核人
     */
    private String audituser;

    /**
     * 审核时间
     */
    private Date audittime;

    /**
     * 转账操作人
     */
    private String transferuser;

    /**
     * 转账时间
     */
    private Date transfertime;

    /**
     * 微信转账流水号
     */
    private String transno;

    /**
     * 转账类型:0：线上转账，1：线下转账
     */
    private Integer tradetype;

    /**
     * 
     */
    private String tradeerrorcode;

    /**
     * 提现来源
     */
    private Integer requestsource;

    /**
     * 金额类型：0 金钱，1 积分
     */
    private Integer commissiontype;

    /**
     * 积分兑换比例
     */
    private Double pointspercent;

    /**
     * 积分流水号
     */
    private Long vipbonusid;

    /**
     * 第三方转账扣税比例（服务费）
     */
    private Integer servicefeepoint;

    /**
     * 提现单类型 0 默认 系统自带提现方式   1 第三方 提现 （益世代开）
     */
    private Integer cashpaytype;

    /**
     * 转账类型 0 提现到银行卡 1 提现到微信
     */
    private Integer transfertype;

    /**
     * 益世转账状态：0 待发佣  1 发佣中 2发佣成功  3 发佣失败 13 退票 
     */
    private Integer yishitransferstatus;

    /**
     * 转账银行卡卡号
     */
    private String bankcardno;

    /**
     * 是否需要修个
     */
    private String ismodifybankcardno;

    /**
     * 下次执行时间 
     */
    private Date nextexcutetime;

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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRequestno() {
		return requestno;
	}

	public void setRequestno(String requestno) {
		this.requestno = requestno;
	}

	public Date getRequesttime() {
		return requesttime;
	}

	public void setRequesttime(Date requesttime) {
		this.requesttime = requesttime;
	}

	public String getAudituser() {
		return audituser;
	}

	public void setAudituser(String audituser) {
		this.audituser = audituser;
	}

	public Date getAudittime() {
		return audittime;
	}

	public void setAudittime(Date audittime) {
		this.audittime = audittime;
	}

	public String getTransferuser() {
		return transferuser;
	}

	public void setTransferuser(String transferuser) {
		this.transferuser = transferuser;
	}

	public Date getTransfertime() {
		return transfertime;
	}

	public void setTransfertime(Date transfertime) {
		this.transfertime = transfertime;
	}

	public String getTransno() {
		return transno;
	}

	public void setTransno(String transno) {
		this.transno = transno;
	}

	public Integer getTradetype() {
		return tradetype;
	}

	public void setTradetype(Integer tradetype) {
		this.tradetype = tradetype;
	}

	public String getTradeerrorcode() {
		return tradeerrorcode;
	}

	public void setTradeerrorcode(String tradeerrorcode) {
		this.tradeerrorcode = tradeerrorcode;
	}

	public Integer getRequestsource() {
		return requestsource;
	}

	public void setRequestsource(Integer requestsource) {
		this.requestsource = requestsource;
	}

	public Integer getCommissiontype() {
		return commissiontype;
	}

	public void setCommissiontype(Integer commissiontype) {
		this.commissiontype = commissiontype;
	}

	public Double getPointspercent() {
		return pointspercent;
	}

	public void setPointspercent(Double pointspercent) {
		this.pointspercent = pointspercent;
	}

	public Long getVipbonusid() {
		return vipbonusid;
	}

	public void setVipbonusid(Long vipbonusid) {
		this.vipbonusid = vipbonusid;
	}

	public Integer getServicefeepoint() {
		return servicefeepoint;
	}

	public void setServicefeepoint(Integer servicefeepoint) {
		this.servicefeepoint = servicefeepoint;
	}

	public Integer getCashpaytype() {
		return cashpaytype;
	}

	public void setCashpaytype(Integer cashpaytype) {
		this.cashpaytype = cashpaytype;
	}

	public Integer getTransfertype() {
		return transfertype;
	}

	public void setTransfertype(Integer transfertype) {
		this.transfertype = transfertype;
	}

	public Integer getYishitransferstatus() {
		return yishitransferstatus;
	}

	public void setYishitransferstatus(Integer yishitransferstatus) {
		this.yishitransferstatus = yishitransferstatus;
	}

	public String getBankcardno() {
		return bankcardno;
	}

	public void setBankcardno(String bankcardno) {
		this.bankcardno = bankcardno;
	}

	public String getIsmodifybankcardno() {
		return ismodifybankcardno;
	}

	public void setIsmodifybankcardno(String ismodifybankcardno) {
		this.ismodifybankcardno = ismodifybankcardno;
	}

	public Date getNextexcutetime() {
		return nextexcutetime;
	}

	public void setNextexcutetime(Date nextexcutetime) {
		this.nextexcutetime = nextexcutetime;
	}

	@Override
	public String toString() {
		return "MallFxCashRequest{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",fxid=" + fxid +
				",fxtype=" + fxtype +
				",amount=" + amount +
				",status=" + status +
				",remark=" + remark +
				",requestno=" + requestno +
				",requesttime=" + requesttime +
				",audituser=" + audituser +
				",audittime=" + audittime +
				",transferuser=" + transferuser +
				",transfertime=" + transfertime +
				",transno=" + transno +
				",tradetype=" + tradetype +
				",tradeerrorcode=" + tradeerrorcode +
				",requestsource=" + requestsource +
				",commissiontype=" + commissiontype +
				",pointspercent=" + pointspercent +
				",vipbonusid=" + vipbonusid +
				",servicefeepoint=" + servicefeepoint +
				",cashpaytype=" + cashpaytype +
				",transfertype=" + transfertype +
				",yishitransferstatus=" + yishitransferstatus +
				",bankcardno=" + bankcardno +
				",ismodifybankcardno=" + ismodifybankcardno +
				",nextexcutetime=" + nextexcutetime +
				'}';
	}
}
