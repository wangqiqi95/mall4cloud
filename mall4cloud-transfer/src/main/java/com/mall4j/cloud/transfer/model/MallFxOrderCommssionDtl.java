package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销订单佣金详情表
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public class MallFxOrderCommssionDtl extends BaseModel implements Serializable{
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
     * 佣金订单Id
     */
    private Long ocid;

    /**
     * 订单Id
     */
    private Long orderid;

    /**
     * 订单存储类型：0 商城订单表  1 储值卡订单表
     */
    private Integer ordersavetype;

    /**
     * 订单详情Id
     */
    private Long orderdtlid;

    /**
     * 商品Id
     */
    private Long itemid;

    /**
     * 商品名称
     */
    private String itemname;

    /**
     * 商品货号
     */
    private String itemno;

    /**
     * 商品图片地址
     */
    private String imgurl;

    /**
     * 条码Id
     */
    private Long barid;

    /**
     * 商品条码
     */
    private String barcode;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 售价
     */
    private Double saleprice;

    /**
     * 实付金额
     */
    private Double realpayamount;

    /**
     * 佣金
     */
    private Double commission;

    /**
     * 预付佣金
     */
    private Double precommission;

    /**
     * 扣除佣金
     */
    private Double dedcommission;

    /**
     * 佣金设置
     */
    private Double rate;

    /**
     * 佣金单位  0 %  1 元
     */
    private Integer ratetype;

    /**
     * 
     */
    private String caculateargs;

    /**
     * 创建时间
     */
    private Date createdate;

    /**
     * 最后更新时间
     */
    private Date lastmodifieddate;

    /**
     * 推广来源：0 默认  1 机器人群推广
     */
    private Integer promotionsource;

    /**
     * 推广来源Id
     */
    private Long psid;

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

	public Long getOcid() {
		return ocid;
	}

	public void setOcid(Long ocid) {
		this.ocid = ocid;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Integer getOrdersavetype() {
		return ordersavetype;
	}

	public void setOrdersavetype(Integer ordersavetype) {
		this.ordersavetype = ordersavetype;
	}

	public Long getOrderdtlid() {
		return orderdtlid;
	}

	public void setOrderdtlid(Long orderdtlid) {
		this.orderdtlid = orderdtlid;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getItemno() {
		return itemno;
	}

	public void setItemno(String itemno) {
		this.itemno = itemno;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(Double saleprice) {
		this.saleprice = saleprice;
	}

	public Double getRealpayamount() {
		return realpayamount;
	}

	public void setRealpayamount(Double realpayamount) {
		this.realpayamount = realpayamount;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Double getPrecommission() {
		return precommission;
	}

	public void setPrecommission(Double precommission) {
		this.precommission = precommission;
	}

	public Double getDedcommission() {
		return dedcommission;
	}

	public void setDedcommission(Double dedcommission) {
		this.dedcommission = dedcommission;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getRatetype() {
		return ratetype;
	}

	public void setRatetype(Integer ratetype) {
		this.ratetype = ratetype;
	}

	public String getCaculateargs() {
		return caculateargs;
	}

	public void setCaculateargs(String caculateargs) {
		this.caculateargs = caculateargs;
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

	@Override
	public String toString() {
		return "MallFxOrderCommssionDtl{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",ocid=" + ocid +
				",orderid=" + orderid +
				",ordersavetype=" + ordersavetype +
				",orderdtlid=" + orderdtlid +
				",itemid=" + itemid +
				",itemname=" + itemname +
				",itemno=" + itemno +
				",imgurl=" + imgurl +
				",barid=" + barid +
				",barcode=" + barcode +
				",quantity=" + quantity +
				",saleprice=" + saleprice +
				",realpayamount=" + realpayamount +
				",commission=" + commission +
				",precommission=" + precommission +
				",dedcommission=" + dedcommission +
				",rate=" + rate +
				",ratetype=" + ratetype +
				",caculateargs=" + caculateargs +
				",createdate=" + createdate +
				",lastmodifieddate=" + lastmodifieddate +
				",promotionsource=" + promotionsource +
				",psid=" + psid +
				'}';
	}
}
