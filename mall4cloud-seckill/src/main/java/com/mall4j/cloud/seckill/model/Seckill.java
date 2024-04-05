package com.mall4j.cloud.seckill.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 秒杀信息
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public class Seckill extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 秒杀活动id
     */
    private Long seckillId;

    /**
     * 活动名称
     */
    private String seckillName;

    /**
     * 开始日期
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 所选批次
     */
    private Integer selectedLot;

    /**
     * 所选秒杀分类
     */
    private Long categoryId;

    /**
     * 活动标签
     */
    private String seckillTag;

    /**
     * 限购数量
     */
    private Integer maxNum;

    /**
     * 取消订单时间
     */
    private Integer maxCancelTime;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 0 未删除 1已删除
     */
    private Integer isDelete;

    /**
     * 状态(0:失效、1:正常、2:违规下架、3:等待审核)
     */
    private Integer status;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 秒杀活动剩余总库存
     */
    private Integer seckillTotalStocks;

    /**
     * 秒杀活动原始库存
     */
    private Integer seckillOriginStocks;

    /**
     * 秒杀活动最低价
     */
    private Long seckillPrice;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	@Override
	public String toString() {
		return "Seckill{" +
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
				", seckillTotalStocks=" + seckillTotalStocks +
				", seckillOriginStocks=" + seckillOriginStocks +
				", seckillPrice=" + seckillPrice +
				'}';
	}
}
