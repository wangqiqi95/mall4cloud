package com.mall4j.cloud.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 店铺装修信息
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Data
@ApiModel("店铺装修实体")
public class StoreRenovation extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 店铺装修id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("店铺装修id")
    private Long renovationId;

    /**
     * 门店id
     */
	@ApiModelProperty("门店id")
    private Long storeId;

	@ApiModelProperty("状态 0-未启用 1-已启用")
	private Integer status;

    /**
     * 装修名称
     */
	@ApiModelProperty("装修名称")
    private String name;

    /**
     * 装修内容
     */
	@ApiModelProperty("装修内容")
    private String content;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remarks;

    @ApiModelProperty("创建人id")
    private Long createId;
    @ApiModelProperty("创建人name")
    private String createName;

    @ApiModelProperty("修改人id")
    private Long updateId;
    @ApiModelProperty("修改人name")
    private String updateName;

	@ApiModelProperty("预约发布时间(精确到小时)")
	private Date makePushTime;
	@ApiModelProperty("发布时间")
	private Date pushTime;
	@ApiModelProperty("发布人")
	private String pushBy;
	@ApiModelProperty("是否发布：0未发布 1已发布")
	private Integer pushStatus;
	@ApiModelProperty("0立即发布 1定时发布")
	private Integer pushType;

	@ApiModelProperty("短链")
	private String shortLink;

	@ApiModelProperty("打开次数")
	private Integer openNumber;

	@ApiModelProperty("打开人数")
	private Integer openPeople;

    /**
     * 是否主页 1.是 0.不是
     */
	@ApiModelProperty("页面类型 0-微页面，1 -主页，2-底部导航，3-分类页，4-会员主页")
    private Integer homeStatus;
	@TableField(exist = false)
	@ApiModelProperty("适用门店集合")
	private List<Long> storeIdList;

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
		return "StoreRenovation{" +
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
