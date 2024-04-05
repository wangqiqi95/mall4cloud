package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.MaterialType;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 素材分类表VO
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialTypeVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("分类名称")
    private String typeName;

    @ApiModelProperty("父分类id")
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

    @ApiModelProperty("是否置顶 0 否 1 是")
    private Integer isTop;

	@ApiModelProperty("子类型集合")
	private List<MaterialType> children;

	@Override
	public String toString() {
		return "MaterialTypeVO{" +
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
