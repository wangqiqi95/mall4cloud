package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 店铺装修信息DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Data
public class StoreRenovationDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺装修id")
    private Long renovationId;

    @ApiModelProperty("门店id")
    private Long storeId;

    @ApiModelProperty("装修名称")
    private String name;

    @ApiModelProperty("装修内容")
    private String content;

	@ApiModelProperty("备注")
	private String remarks;

    @ApiModelProperty("页面类型 0-微页面，1 -主页，2-底部导航，3-分类页，4-会员主页")
    private Integer homeStatus;

	@ApiModelProperty("状态 0-未启用 1-已启用")
	private Integer status;

	@ApiModelProperty("适用门店")
	private List<Long> renoApplyStoreList;

	@ApiModelProperty("预约发布时间(精确到小时)")
	private Date makePushTime;
	@ApiModelProperty("0立即发布 1定时发布")
	private Integer pushType;

	public Long getRenovationId() {
		return renovationId;
	}

	public void setRenovationId(Long renovationId) {
		this.renovationId = renovationId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getHomeStatus() {
		return homeStatus;
	}

	public void setHomeStatus(Integer homeStatus) {
		this.homeStatus = homeStatus;
	}

	@Override
	public String toString() {
		return "StoreRenovationDTO{" +
				"renovationId=" + renovationId +
				",storeId=" + storeId +
				",name=" + name +
				",content=" + content +
				",homeStatus=" + homeStatus +
				'}';
	}
}
