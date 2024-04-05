package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 客群分配明细表 VO
 *
 * @author hwy
 * @date 2022-02-10 18:25:57
 */
public class CustGroupAssignDetailVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("客户/群ID")
    private String custGroupId;

    @ApiModelProperty("群客户数")
    private Integer nums;

    @ApiModelProperty("离职操作ID")
    private Long resignId;

    @ApiModelProperty("原添加员工/原群主")
    private Long addBy;

    @ApiModelProperty("原添加员工姓名/群主姓名")
    private String addByName;

    @ApiModelProperty("接替员工")
    private Long replaceBy;

    @ApiModelProperty("接替员工姓名")
    private String replaceByName;

    @ApiModelProperty("客户姓名/群名")
    private String name;

    @ApiModelProperty("客户手机号码")
    private String mobile;

    @ApiModelProperty("客户等级")
    private String level;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("分配类型")
    private Integer assignType;

    @ApiModelProperty("接替员工门店")
    private Long storeId;

    @ApiModelProperty("接替员工门店名称")
    private String storeName;

    @ApiModelProperty("0 客户分配  1 群分配")
    private Integer type;

    @ApiModelProperty("分配状态 0 分配中  1 分配成功  2 分配失败")
    private Integer status;

    @ApiModelProperty("删除标识")
    private Integer flag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustGroupId() {
		return custGroupId;
	}

	public void setCustGroupId(String custGroupId) {
		this.custGroupId = custGroupId;
	}

	public Integer getNums() {
		return nums;
	}

	public void setNums(Integer nums) {
		this.nums = nums;
	}

	public Long getResignId() {
		return resignId;
	}

	public void setResignId(Long resignId) {
		this.resignId = resignId;
	}

	public Long getAddBy() {
		return addBy;
	}

	public void setAddBy(Long addBy) {
		this.addBy = addBy;
	}

	public String getAddByName() {
		return addByName;
	}

	public void setAddByName(String addByName) {
		this.addByName = addByName;
	}

	public Long getReplaceBy() {
		return replaceBy;
	}

	public void setReplaceBy(Long replaceBy) {
		this.replaceBy = replaceBy;
	}

	public String getReplaceByName() {
		return replaceByName;
	}

	public void setReplaceByName(String replaceByName) {
		this.replaceByName = replaceByName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getAssignType() {
		return assignType;
	}

	public void setAssignType(Integer assignType) {
		this.assignType = assignType;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "CustGroupAssignDetailVO{" +
				"id=" + id +
				",custGroupId=" + custGroupId +
				",nums=" + nums +
				",resignId=" + resignId +
				",addBy=" + addBy +
				",addByName=" + addByName +
				",replaceBy=" + replaceBy +
				",replaceByName=" + replaceByName +
				",name=" + name +
				",mobile=" + mobile +
				",level=" + level +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",assignType=" + assignType +
				",storeId=" + storeId +
				",storeName=" + storeName +
				",type=" + type +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
