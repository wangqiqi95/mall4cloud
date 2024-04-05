package com.mall4j.cloud.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 流量分析—用户流量商品数据DTO
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public class UserVisitProdAnalysisDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户访问商品id")
    private Long userVisitProdAnalysisId;

    @ApiModelProperty("用户路径id")
    private Long userAnalysisId;

    @ApiModelProperty("创建日期")
    private Date createDate;

    @ApiModelProperty("商品id")
    private Long spuId;

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

	@Override
	public String toString() {
		return "UserVisitProdAnalysisDTO{" +
				"userVisitProdAnalysisId=" + userVisitProdAnalysisId +
				", userAnalysisId=" + userAnalysisId +
				", createDate=" + createDate +
				", spuId=" + spuId +
				'}';
	}
}
