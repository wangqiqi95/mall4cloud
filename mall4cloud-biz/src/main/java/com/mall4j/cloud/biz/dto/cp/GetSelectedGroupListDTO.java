package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-02-16 16:28
 * @Version: 1.0
 */
@Data
public class GetSelectedGroupListDTO implements Serializable {
    @ApiModelProperty("活动id")
    @NotNull(message = "群活码id不能为空")
    private Long id;

    @ApiModelProperty("类型 0 未关联  1 已关联")
    @NotNull(message = "类型不能为空")
    private Long type;

    @ApiModelProperty("员工姓名")
    private String createName;

    @ApiModelProperty("群名称")
    private String groupName;
}
