package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
/**
 * 客户_券_券库信息_SKU绑定(针对SKU)
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:30
 */
public class CrmCouponGrpSku extends BaseModel implements Serializable{
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
     * 券库编号
     */
    private Integer coupongrpid;

    /**
     * 商城商品编号
     */
    private Long prodid;

    /**
     * 商品SKU
     */
    private String sku;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 创建人
     */
    private String createuser;

    /**
     * 创建时间
     */
    private Date createdate;

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

	public Integer getCoupongrpid() {
		return coupongrpid;
	}

	public void setCoupongrpid(Integer coupongrpid) {
		this.coupongrpid = coupongrpid;
	}

	public Long getProdid() {
		return prodid;
	}

	public void setProdid(Long prodid) {
		this.prodid = prodid;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "CrmCouponGrpSku{" +
				"id=" + id +
				",copid=" + copid +
				",brandid=" + brandid +
				",coupongrpid=" + coupongrpid +
				",prodid=" + prodid +
				",sku=" + sku +
				",name=" + name +
				",createuser=" + createuser +
				",createdate=" + createdate +
				'}';
	}
}
