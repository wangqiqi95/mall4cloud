package com.mall4j.cloud.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 门店标签VO
 *
 * @author gmq
 * @date 2022-09-13 12:00:57
 */
@Data
public class TzTagVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long tagId;

    @ApiModelProperty("标签编码")
    private String tagCode;

    @ApiModelProperty("标签名称")
    private String tagName;

    @ApiModelProperty("状态：0禁用 1启用")
    private Integer status;

    @ApiModelProperty("关联员工总数")
    private Integer staffCount;

    @ApiModelProperty("关联门店总数")
    private Integer storeCount;

	@ApiModelProperty("员工集合")
	private List<Long> staffIds;

	@ApiModelProperty("门店集合")
	private List<Long> storeIds;

}
