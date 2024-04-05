package com.mall4j.cloud.flow.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mall4j.cloud.common.model.BaseModel;
import nonapi.io.github.classgraph.utils.LogNode;

/**
 * 流量分析—商品分析
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public class ProductAnalyse extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long productAnalyseId;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 系统类型
     */
    private Integer systemType;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 访问次数
     */
    private Long visis;

    /**
     * 点击数
     */
    private Long click;

    /**
     * 分享访问次数
     */
    private Long shareVisit;

    /**
     * 加购数量
     */
    private Integer plusShopCart;

    /**
     * 操作信息
     */
    private List<ProductAnalyseUser> productAnalyseUsers;

	public Long getProductAnalyseId() {
		return productAnalyseId;
	}

	public void setProductAnalyseId(Long productAnalyseId) {
		this.productAnalyseId = productAnalyseId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getSystemType() {
		return systemType;
	}

	public void setSystemType(Integer systemType) {
		this.systemType = systemType;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getVisis() {
		return visis;
	}

	public void setVisis(Long visis) {
		this.visis = visis;
	}

	public Long getClick() {
		return click;
	}

	public void setClick(Long click) {
		this.click = click;
	}

	public Long getShareVisit() {
		return shareVisit;
	}

	public void setShareVisit(Long shareVisit) {
		this.shareVisit = shareVisit;
	}


	public Integer getPlusShopCart() {
		return plusShopCart;
	}

	public void setPlusShopCart(Integer plusShopCart) {
		this.plusShopCart = plusShopCart;
	}

	public List<ProductAnalyseUser> getProductAnalyseUsers() {
		return productAnalyseUsers;
	}

	public void setProductAnalyseUsers(List<ProductAnalyseUser> productAnalyseUsers) {
		this.productAnalyseUsers = productAnalyseUsers;
	}

	@Override
	public String toString() {
		return "ProductAnalyse{" +
				"productAnalyseId=" + productAnalyseId +
				",createDate=" + createDate +
				",systemType=" + systemType +
				",spuId=" + spuId +
				",shopId=" + shopId +
				",visis=" + visis +
				",click=" + click +
				",shareVisit=" + shareVisit +
				",plusShopCart=" + plusShopCart +
				",productAnalyseUsers=" + productAnalyseUsers +
				'}';
	}
}
