package com.mall4j.cloud.flow.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public class ProductAnalyseUser extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long productAnalyseUserId;

    /**
     * 页面分析id
     */
    private Long productAnalyseId;

    /**
     * 用户id-未登陆的用户为uuId
     */
    private String userId;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 访问
     */
    private Integer isVisit;

    /**
     * 页面点击
     */
    private Integer isClick;

    /**
     * 加购
     */
    private Integer isPlusShopCart;

	public Long getProductAnalyseUserId() {
		return productAnalyseUserId;
	}

	public void setProductAnalyseUserId(Long productAnalyseUserId) {
		this.productAnalyseUserId = productAnalyseUserId;
	}

	public Long getProductAnalyseId() {
		return productAnalyseId;
	}

	public void setProductAnalyseId(Long productAnalyseId) {
		this.productAnalyseId = productAnalyseId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Integer getIsVisit() {
		return isVisit;
	}

	public void setIsVisit(Integer isVisit) {
		this.isVisit = isVisit;
	}

	public Integer getIsClick() {
		return isClick;
	}

	public void setIsClick(Integer isClick) {
		this.isClick = isClick;
	}

	public Integer getIsPlusShopCart() {
		return isPlusShopCart;
	}

	public void setIsPlusShopCart(Integer isPlusShopCart) {
		this.isPlusShopCart = isPlusShopCart;
	}

	@Override
	public String toString() {
		return "ProductAnalyseUser{" +
				"productAnalyseUserId=" + productAnalyseUserId +
				",productAnalyseId=" + productAnalyseId +
				",userId=" + userId +
				",createDate=" + createDate +
				",spuId=" + spuId +
				",isVisit=" + isVisit +
				",isClick=" + isClick +
				",isPlusShopCart=" + isPlusShopCart +
				'}';
	}
}
