package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 标签组配置DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class TagGroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标签组id")
    private Integer id;

    @ApiModelProperty("标签组名称")
    private String groupName;

    @ApiModelProperty("标签组类型  0 唯一 1 多个")
    private Integer type;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("删除标识")
    private Integer flag;

    @ApiModelProperty("标签list")
    private List<TagDTO> tags;


	@Override
	public String toString() {
		return "TagGroupDTO{" +
				"id=" + id +
				",groupName=" + groupName +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
