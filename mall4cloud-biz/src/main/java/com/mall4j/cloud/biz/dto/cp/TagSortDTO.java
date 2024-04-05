package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 标签组配置DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class TagSortDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("标签组id")
    @NotNull(message = "id 不能为空")
    private Long id;

    @ApiModelProperty("排序")
    @NotNull(message = "排序 不能为空")
    private Integer sort;
}
