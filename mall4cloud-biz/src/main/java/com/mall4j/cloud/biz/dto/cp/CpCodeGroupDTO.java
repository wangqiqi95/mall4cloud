package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企微活码分组DTO
 *
 * @author gmq
 * @date 2023-10-23 14:03:45
 */
@Data
public class CpCodeGroupDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("分组名称")
    private String name;

    @ApiModelProperty("分组类型：0-渠道活码/1-群活码/2-自动拉群")
    private Integer type;

    @ApiModelProperty("")
    private Long parentId;

}
