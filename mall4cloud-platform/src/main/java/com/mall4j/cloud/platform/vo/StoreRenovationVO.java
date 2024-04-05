package com.mall4j.cloud.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 店铺装修信息VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Data
public class StoreRenovationVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺装修id")
    private Long renovationId;

    @ApiModelProperty("门店id")
    private Long storeId;

    @ApiModelProperty("装修名称")
    private String name;

    @ApiModelProperty("装修内容")
    private String content;

    @ApiModelProperty("是否主页 1.是 0.不是")
    private Integer homeStatus;

	@ApiModelProperty("0-未启动，1-已启用")
	private Integer status;

	@ApiModelProperty("适用门店")
	private List<Long> renoApplyStoreList;

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
		return "StoreRenovationVO{" +
				"renovationId=" + renovationId +
				",storeId=" + storeId +
				",name=" + name +
				",content=" + content +
				",homeStatus=" + homeStatus +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
