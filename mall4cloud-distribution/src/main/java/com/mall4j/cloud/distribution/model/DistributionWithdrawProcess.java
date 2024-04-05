package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 佣金处理批次
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:48
 */
public class DistributionWithdrawProcess extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 表格名称
     */
    private String batchName;

    /**
     * 成功数量
     */
    private Integer successQuantity;

    /**
     * 失败数量
     */
    private Integer failQuantity;

    /**
     * 导入时间
     */
    private Date importDate;

    /**
     * 执行状态 0待执行 1已执行
     */
    private Integer executeStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Integer getSuccessQuantity() {
		return successQuantity;
	}

	public void setSuccessQuantity(Integer successQuantity) {
		this.successQuantity = successQuantity;
	}

	public Integer getFailQuantity() {
		return failQuantity;
	}

	public void setFailQuantity(Integer failQuantity) {
		this.failQuantity = failQuantity;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Integer getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(Integer executeStatus) {
		this.executeStatus = executeStatus;
	}

	@Override
	public String toString() {
		return "DistributionWithdrawProcess{" +
				"id=" + id +
				",batchName=" + batchName +
				",successQuantity=" + successQuantity +
				",failQuantity=" + failQuantity +
				",importDate=" + importDate +
				",executeStatus=" + executeStatus +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
