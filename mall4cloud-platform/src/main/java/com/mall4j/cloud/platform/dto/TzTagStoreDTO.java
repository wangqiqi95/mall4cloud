package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 标签关联门店DTO
 *
 * @author gmq
 * @date 2022-09-13 12:01:58
 */
@Data
public class TzTagStoreDTO{
    private static final long serialVersionUID = 1L;

    @NotNull(message = "门店id不能为空")
    @ApiModelProperty("门店id")
    private Long storeId;

    @NotNull(message = "标签不能为空")
	@ApiModelProperty("标签id集合")
	private List<Long> tagIds;

}
