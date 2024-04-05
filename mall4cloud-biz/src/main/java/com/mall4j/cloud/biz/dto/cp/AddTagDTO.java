package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 客户群表DTO
 *
 * @author hwy
 * @date 2022-02-16 09:29:57
 */
@Data
public class AddTagDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标签id组")
    @NotNull(message = "标签id不能为空")
    private List<Long> tagIds;

    @ApiModelProperty("群名Id")
    @NotBlank(message = "群id不能为空")
    private String id;
}
