package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 素材分类表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialType extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String typeName;

    /**
     * 父分类id
     */
    private Integer parentId;

    /**
     * 是否显示
     */
    private Integer isShow;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标识 1 删除 0 未删除
     */
    private Integer flag;

    /**
     * 状态 0无效 1有效
     */
    private Integer status;

    /**
     * 是否置顶 0 否 1 是
     */
    private Integer isTop;

	@Override
	public String toString() {
		return "MaterialType{" +
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
