package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销推广-下单记录VO
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public class DistributionBuyRecordVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("下单人ID")
    private Long buyId;

    @ApiModelProperty("下单人名称")
    private String buyName;

	@ApiModelProperty("下单人手机号")
	private String buyMobile;

    @ApiModelProperty("类型 1 导购 2威客 3会员")
    private Integer buyType;

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

	public Long getBuyId() {
		return buyId;
	}

	public void setBuyId(Long buyId) {
		this.buyId = buyId;
	}

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public String getBuyMobile() {
		return buyMobile;
	}

	public void setBuyMobile(String buyMobile) {
		this.buyMobile = buyMobile;
	}

	public Integer getBuyType() {
		return buyType;
	}

	public void setBuyType(Integer buyType) {
		this.buyType = buyType;
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
		return "DistributionBuyRecordVO{" +
				"id=" + id +
				",buyId=" + buyId +
				",buyName=" + buyName +
				",buyType=" + buyType +
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
