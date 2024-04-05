package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 
 *
 * @author gmq
 * @date 2022-06-14 15:17:31
 */
public class ProtectActivitySpu extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 电商保护价
     */
    private Long protectPrice;

    /**
     * 0生效 1失效
     */
    private Integer status;

	/**
	 * 0待审核 1待执行 2进行中 3已结束 4驳回
	 */
    private Integer exStatus;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 0正常 1删除
     */
    private Integer delFlag;

	private String checkBy;
	private Date checkTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getProtectPrice() {
		return protectPrice;
	}

	public void setProtectPrice(Long protectPrice) {
		this.protectPrice = protectPrice;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Integer getExStatus() {
		return exStatus;
	}

	public void setExStatus(Integer exStatus) {
		this.exStatus = exStatus;
	}

	public String getCheckBy() {
		return checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	@Override
	public String toString() {
		return "ProtectActivitySpu{" +
				"id=" + id +
				",spuId=" + spuId +
				",protectPrice=" + protectPrice +
				",status=" + status +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
