package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;

/**
 * 企微活码分组
 *
 * @author gmq
 * @date 2023-10-23 14:03:45
 */
public class CpCodeGroup extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 是否逻辑删除:默认0未删除;1已删除
     */
    private Integer isDeleted;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组类型：0-渠道活码/1-群活码/2-自动拉群
     */
    private Integer type;

    /**
     * 
     */
    private Long parentId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return "CpCodeGroup{" +
				"id=" + id +
				",createTime=" + createTime +
				",createBy=" + createBy +
				",updateTime=" + updateTime +
				",updateBy=" + updateBy +
				",isDeleted=" + isDeleted +
				",name=" + name +
				",type=" + type +
				",parentId=" + parentId +
				'}';
	}
}
