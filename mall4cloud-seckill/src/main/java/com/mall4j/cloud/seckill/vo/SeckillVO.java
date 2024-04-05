package com.mall4j.cloud.seckill.vo;

import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

/**
 * 秒杀信息VO
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public class SeckillVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("秒杀活动id")
    private Long seckillId;

    @ApiModelProperty("活动名称")
    private String seckillName;

    @ApiModelProperty("开始日期")
    private Date startTime;

	@ApiModelProperty("结束日期")
	private Date endTime;

    @ApiModelProperty("所选批次")
    private Integer selectedLot;

	@ApiModelProperty("所选秒杀分类")
	private Long categoryId;

    @ApiModelProperty("活动标签")
    private String seckillTag;

    @ApiModelProperty("限购数量")
    private Integer maxNum;

    @ApiModelProperty("取消订单时间")
    private Integer maxCancelTime;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("0 未删除 1已删除")
    private Integer isDelete;

    @ApiModelProperty("状态(0:失效、1:正常、2:违规下架、3:等待审核)")
    private Integer status;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("商品信息")
    private SpuVO spuVO;

    @ApiModelProperty("秒杀活动剩余总库存")
    private Integer seckillTotalStocks;

    @ApiModelProperty("秒杀活动原始库存")
    private Integer seckillOriginStocks;

    @ApiModelProperty("秒杀活动最低价")
    private Long seckillPrice;

    @ApiModelProperty("秒杀活动关联sku信息")
    private List<SeckillSkuVO> seckillSkuList;

	@ApiModelProperty("sku列表")
	private List<SkuVO> skuList;

	@ApiModelProperty("指定门店类型 0-全部 1-部分")
	private Integer limitStoreType;

	@ApiModelProperty("指定门店id集合")
	private List<Long> limitStoreIdList;

	private boolean isRSpu;
	private boolean isActivityPrice;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public SpuVO getSpuVO() {
		return spuVO;
	}

	public void setSpuVO(SpuVO spuVO) {
		this.spuVO = spuVO;
	}

	public List<SkuVO> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<SkuVO> skuList) {
		this.skuList = skuList;
	}

	public List<SeckillSkuVO> getSeckillSkuList() {
		return seckillSkuList;
	}

	public void setSeckillSkuList(List<SeckillSkuVO> seckillSkuList) {
		this.seckillSkuList = seckillSkuList;
	}

	public Long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}

	public String getSeckillName() {
		return seckillName;
	}

	public void setSeckillName(String seckillName) {
		this.seckillName = seckillName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getSelectedLot() {
		return selectedLot;
	}

	public void setSelectedLot(Integer selectedLot) {
		this.selectedLot = selectedLot;
	}

	public String getSeckillTag() {
		return seckillTag;
	}

	public void setSeckillTag(String seckillTag) {
		this.seckillTag = seckillTag;
	}

	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public Integer getMaxCancelTime() {
		return maxCancelTime;
	}

	public void setMaxCancelTime(Integer maxCancelTime) {
		this.maxCancelTime = maxCancelTime;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Integer getSeckillTotalStocks() {
		return seckillTotalStocks;
	}

	public void setSeckillTotalStocks(Integer seckillTotalStocks) {
		this.seckillTotalStocks = seckillTotalStocks;
	}

	public Integer getSeckillOriginStocks() {
		return seckillOriginStocks;
	}

	public void setSeckillOriginStocks(Integer seckillOriginStocks) {
		this.seckillOriginStocks = seckillOriginStocks;
	}

	public Long getSeckillPrice() {
		return seckillPrice;
	}

	public void setSeckillPrice(Long seckillPrice) {
		this.seckillPrice = seckillPrice;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getLimitStoreType() {
		return limitStoreType;
	}

	public void setLimitStoreType(Integer limitStoreType) {
		this.limitStoreType = limitStoreType;
	}

	public List<Long> getLimitStoreIdList() {
		return limitStoreIdList;
	}

	public void setLimitStoreIdList(List<Long> limitStoreIdList) {
		this.limitStoreIdList = limitStoreIdList;
	}

	public boolean isRSpu() {
		return isRSpu;
	}

	public void setRSpu(boolean RSpu) {
		isRSpu = RSpu;
	}

	public boolean isActivityPrice() {
		return isActivityPrice;
	}

	public void setActivityPrice(boolean activityPrice) {
		isActivityPrice = activityPrice;
	}

	@Override
	public String toString() {
		return "SeckillVO{" +
				"seckillId=" + seckillId +
				", seckillName='" + seckillName + '\'' +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", selectedLot=" + selectedLot +
				", categoryId=" + categoryId +
				", seckillTag='" + seckillTag + '\'' +
				", maxNum=" + maxNum +
				", maxCancelTime=" + maxCancelTime +
				", shopId=" + shopId +
				", isDelete=" + isDelete +
				", status=" + status +
				", spuId=" + spuId +
				", spuVO=" + spuVO +
				", seckillTotalStocks=" + seckillTotalStocks +
				", seckillOriginStocks=" + seckillOriginStocks +
				", seckillPrice=" + seckillPrice +
				", seckillSkuList=" + seckillSkuList +
				", skuList=" + skuList +
				'}';
	}
}
