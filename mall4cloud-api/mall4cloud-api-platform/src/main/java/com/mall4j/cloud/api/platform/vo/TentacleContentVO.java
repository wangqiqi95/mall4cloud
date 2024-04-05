package com.mall4j.cloud.api.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 触点内容信息VO
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@Data
public class TentacleContentVO extends BaseVO{

	@ApiModelProperty("主键ID")
	private Long id;

	@ApiModelProperty("组织节点ID")
	private Long orgId;

	@ApiModelProperty("业务ID 门店ID 会员ID")
	private Long businessId;

	@ApiModelProperty("触点号")
	private String tentacleNo;

	@ApiModelProperty("触点ID")
	private Long tentacleId;

	@ApiModelProperty("触点类型 1门店 2会员 3威客 4导购")
	private Integer tentacleType;

	@ApiModelProperty("内容标题")
	private String contentTitle;

	@ApiModelProperty("内容类型")
	private Integer contentType;

	@ApiModelProperty("内容唯一标识")
	private Long contentId;

	@ApiModelProperty("具体营销内容")
	private String content;

	@ApiModelProperty("描述")
	private String description;

	@ApiModelProperty("1正常 0冻结")
	private Integer status;

	@ApiModelProperty("触点信息")
	private TentacleVo tentacle;

}
