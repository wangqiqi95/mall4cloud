package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销推广-加购记录VO
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public class DistributionPurchaseRecordVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("加购人ID")
    private Long purchaseId;

    @ApiModelProperty("加购人名称")
    private String purchaseName;

	@ApiModelProperty("加购人手机号")
	private String purchaseMobile;

    @ApiModelProperty("类型 1 导购 2威客 3会员")
    private String purchaseType;

    @ApiModelProperty("触点号")
    private String tentacleNo;

    @ApiModelProperty("分享人ID")
    private Long shareId;

    @ApiModelProperty("类型 1 导购 2威客 3会员")
    private Integer shareType;

    @ApiModelProperty("活动ID")
    private Long activityId;

    @ApiModelProperty("活动类型 1海报 2专题 3朋友圈 4商品")
    private Integer activityType;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getPurchaseName() {
		return purchaseName;
	}

	public void setPurchaseName(String purchaseName) {
		this.purchaseName = purchaseName;
	}

	public String getPurchaseMobile() {
		return purchaseMobile;
	}

	public void setPurchaseMobile(String purchaseMobile) {
		this.purchaseMobile = purchaseMobile;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public String getTentacleNo() {
		return tentacleNo;
	}

	public void setTentacleNo(String tentacleNo) {
		this.tentacleNo = tentacleNo;
	}

	public Long getShareId() {
		return shareId;
	}

	public void setShareId(Long shareId) {
		this.shareId = shareId;
	}

	public Integer getShareType() {
		return shareType;
	}

	public void setShareType(Integer shareType) {
		this.shareType = shareType;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return "DistributionPurchaseRecordVO{" +
				"id=" + id +
				",purchaseId=" + purchaseId +
				",purchaseName=" + purchaseName +
				",purchaseType=" + purchaseType +
				",tentacleNo=" + tentacleNo +
				",shareId=" + shareId +
				",shareType=" + shareType +
				",activityId=" + activityId +
				",activityType=" + activityType +
				",activityName=" + activityName +
				",productId=" + productId +
				",productName=" + productName +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
