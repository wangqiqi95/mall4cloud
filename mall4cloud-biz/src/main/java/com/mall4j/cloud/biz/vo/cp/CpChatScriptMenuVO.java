package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 话术分类表VO
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
public class CpChatScriptMenuVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("分类名称")
    private String menuName;

    @ApiModelProperty("父分类id")
    private Integer parentId;

    @ApiModelProperty("是否显示")
    private Integer isShow;

    @ApiModelProperty("删除标识 1 删除 0 未删除")
    private Integer flag;

    @ApiModelProperty("状态 0无效 1有效")
    private Integer status;

    @ApiModelProperty("是否置顶 0否 1是")
    private Integer isTop;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	@Override
	public String toString() {
		return "CpChatScriptMenuVO{" +
				"id=" + id +
				",menuName=" + menuName +
				",parentId=" + parentId +
				",isShow=" + isShow +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",flag=" + flag +
				",status=" + status +
				",isTop=" + isTop +
				'}';
	}
}
