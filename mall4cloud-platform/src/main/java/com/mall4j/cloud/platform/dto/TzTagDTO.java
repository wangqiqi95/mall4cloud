package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 门店标签DTO
 *
 * @author gmq
 * @date 2022-09-13 12:00:57
 */
@Data
public class TzTagDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id(编辑需要传)",required = false)
    private Long tagId;

    @ApiModelProperty(required = false)
    private Long parentId;

    @NotNull
    @ApiModelProperty(value = "标签名称",required = true)
    private String tagName;

    @ApiModelProperty("状态：0禁用 1启用")
    private Integer status;

//    @NotNull(message = "运营人员不能为空")
	@ApiModelProperty(value = "员工集合",required = false)
	private List<Long> staffIds;

    @NotNull(message = "相关门店不能为空")
	@ApiModelProperty(value = "门店集合",required = true)
	private List<Long> storeIds;

}
