package com.mall4j.cloud.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 触点信息VO
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@Data
public class TentacleVO extends BaseVO{

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("组织节点ID")
    private Long orgId;

    @ApiModelProperty("触点名称")
    private String tentacleName;

    @ApiModelProperty("触点code")
    private String tentacleCode;

	@ApiModelProperty("业务ID 门店ID 会员ID")
	private Long businessId;

    @ApiModelProperty("触点类型 1门店 2会员 3威客 4导购")
    private Integer tentacleType;

    @ApiModelProperty("导购ID")
    private Long guideId;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("1正常 0冻结")
    private Integer status;

}
