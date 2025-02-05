package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

/**
 * 商品分组表VO
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
public class SpuTagVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分组标签id")
    private Long id;

    @ApiModelProperty("分组标题")
    private String title;

    @ApiModelProperty("店铺Id")
    private Long shopId;

    @ApiModelProperty("状态(1为正常,-1 为删除)")
    private Integer status;

    @ApiModelProperty("默认类型(0:商家自定义,1:系统默认)")
    private Integer isDefault;

    @ApiModelProperty("商品数量")
    private Long prodCount;

    @ApiModelProperty("列表样式(0:一列一个,1:一列两个,2:一列三个)")
    private Integer style;

    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("删除时间")
    private Date deleteTime;

    @ApiModelProperty("商品列表")
    private List<SpuSearchVO> spuList;

	@ApiModelProperty("商品id列表")
	private List<Long> spuIds;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Long getProdCount() {
		return prodCount;
	}

	public void setProdCount(Long prodCount) {
		this.prodCount = prodCount;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public List<Long> getSpuIds() {
		return spuIds;
	}

	public void setSpuIds(List<Long> spuIds) {
		this.spuIds = spuIds;
	}

	public List<SpuSearchVO> getSpuList() {
		return spuList;
	}

	public void setSpuList(List<SpuSearchVO> spuList) {
		this.spuList = spuList;
	}

	@Override
	public String toString() {
		return "SpuTagVO{" +
				"id=" + id +
				", title='" + title + '\'' +
				", shopId=" + shopId +
				", status=" + status +
				", isDefault=" + isDefault +
				", prodCount=" + prodCount +
				", style=" + style +
				", seq=" + seq +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", deleteTime=" + deleteTime +
				", spuIds=" + spuIds +
				", spuList=" + spuList +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
