package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 素材分类表DTO
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
public class MaterialTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("分类名称")
    private String typeName;

    @ApiModelProperty("父分类id  添加一级分类时传0")
    private Integer parentId;

    @ApiModelProperty("是否显示")
    private Integer isShow;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除标识 1 删除 0 未删除")
    private Integer flag;

    @ApiModelProperty("状态 0无效 1有效")
    private Integer status;

    @ApiModelProperty("是否置顶 0否 1是")
    private Integer isTop;


	@Override
	public String toString() {
		return "MaterialTypeDTO{" +
				"id=" + id +
				",typeName=" + typeName +
				",parentId=" + parentId +
				",isShow=" + isShow +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",flag=" + flag +
				",status=" + status +
				'}';
	}
}
