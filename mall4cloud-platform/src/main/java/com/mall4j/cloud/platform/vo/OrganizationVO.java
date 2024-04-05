package com.mall4j.cloud.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 组织结构表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 13:44:09
 */
@Data
public class OrganizationVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("组织节点id")
    private Long orgId;

    @ApiModelProperty("上级节点id")
    private Long parentId;

    @ApiModelProperty("组织节点名称")
    private String orgName;

    @ApiModelProperty("节点类型 1-品牌 ，2-片区，3-店群，4-门店")
    private Integer type;

    @ApiModelProperty("组织节点id")
    private String orgCode;

    @ApiModelProperty("组织节点简称")
    private String shortName;

	@ApiModelProperty("子节点")
	private List<OrganizationVO> childList;

	private String path;
}
