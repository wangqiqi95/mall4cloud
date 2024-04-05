package com.mall4j.cloud.flow.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 流量分析—用户访问商品数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public class UserVisitProdAnalysis extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 用户访问商品id
     */
    private Long userVisitProdAnalysisId;


    /**
     * 用户统计id
     */
    private Long userAnalysisId;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 是否有改变
     */
    private Boolean isChange;

	public Long getUserVisitProdAnalysisId() {
		return userVisitProdAnalysisId;
	}

	public void setUserVisitProdAnalysisId(Long userVisitProdAnalysisId) {
		this.userVisitProdAnalysisId = userVisitProdAnalysisId;
	}

	public Long getUserAnalysisId() {
		return userAnalysisId;
	}

	public void setUserAnalysisId(Long userAnalysisId) {
		this.userAnalysisId = userAnalysisId;
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

	public Boolean getIsChange() {
		return isChange;
	}

	public void setIsChange(Boolean isChange) {
		this.isChange = isChange;
	}

	@Override
	public String toString() {
		return "UserVisitProdAnalysis{" +
				"userVisitProdAnalysisId=" + userVisitProdAnalysisId +
				", userAnalysisId=" + userAnalysisId +
				", createDate=" + createDate +
				", spuId=" + spuId +
				", isChange=" + isChange +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
