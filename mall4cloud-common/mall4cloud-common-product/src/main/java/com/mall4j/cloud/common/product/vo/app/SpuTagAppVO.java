package com.mall4j.cloud.common.product.vo.app;

import com.mall4j.cloud.common.product.vo.SpuVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 商品分组表VO
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
public class SpuTagAppVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分组标签id")
    private Long id;

    @ApiModelProperty("分组标题")
    private String title;

    @ApiModelProperty("店铺Id")
    private Long shopId;

    @ApiModelProperty("商品列表")
    private List<SpuVO> spus;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public List<SpuVO> getSpus() {
		return spus;
	}

	public void setSpus(List<SpuVO> spus) {
		this.spus = spus;
	}

	@Override
	public String toString() {
		return "SpuTagAppVO{" +
				"id=" + id +
				", title='" + title + '\'' +
				", shopId=" + shopId +
				", spus=" + spus +
				'}';
	}
}
