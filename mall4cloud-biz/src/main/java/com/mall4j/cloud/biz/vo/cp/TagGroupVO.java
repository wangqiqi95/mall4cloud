package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.Tag;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 标签组配置VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TagGroupVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标签组id")
    private Integer id;

    @ApiModelProperty("标签组名称")
    private String groupName;

    @ApiModelProperty("标签类型")
    private Integer type;


    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("删除标识")
    private Integer flag;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("标签列表")
    private List<Tag> tags;

	@Override
	public String toString() {
		return "TagGroupVO{" +
				"id=" + id +
				",groupName=" + groupName +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
