package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分销推广-朋友圈商品
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public class DistributionMomentsProduct extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 朋友圈ID
     */
    private Long momentsId;

    /**
     * 商品ID
     */
    private Long productId;

	/**
	 * 商品名称
	 */
	@ApiModelProperty("商品名称")
	private String productName;

	/**
	 * 素材类型 1图片 2商品
	 */
	@ApiModelProperty("素材类型 1图片 2商品")
	private Integer materialType;

	/**
	 * 图片素材地址
	 */
	@ApiModelProperty("图片素材地址")
	private String materialImgUrl;

	@ApiModelProperty("展示排序")
	private Integer showSort;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMomentsId() {
		return momentsId;
	}

	public void setMomentsId(Long momentsId) {
		this.momentsId = momentsId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getMaterialType() {
		return materialType;
	}

	public void setMaterialType(Integer materialType) {
		this.materialType = materialType;
	}

	public String getMaterialImgUrl() {
		return materialImgUrl;
	}

	public void setMaterialImgUrl(String materialImgUrl) {
		this.materialImgUrl = materialImgUrl;
	}

	public Integer getShowSort() {
		return showSort;
	}

	public void setShowSort(Integer showSort) {
		this.showSort = showSort;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return "DistributionMomentsProduct{" +
				"id=" + id +
				", momentsId=" + momentsId +
				", productId=" + productId +
				", productName='" + productName + '\'' +
				", materialType=" + materialType +
				", materialImgUrl='" + materialImgUrl + '\'' +
				", showSort=" + showSort +
				'}';
	}
}
