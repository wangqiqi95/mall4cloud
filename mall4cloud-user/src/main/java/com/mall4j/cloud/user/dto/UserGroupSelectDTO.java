package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户分组阶段配置DTO
 *
 * @author gmq
 * @date 2023-11-10 11:01:57
 */
@Data
public class UserGroupSelectDTO {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("名称")
    private String groupName;

    @ApiModelProperty("排序")
    private Integer weight;

    @ApiModelProperty("父级分组id")
    private Long parentId;

    @ApiModelProperty("0分组/1阶段")
    private Integer type;
}
