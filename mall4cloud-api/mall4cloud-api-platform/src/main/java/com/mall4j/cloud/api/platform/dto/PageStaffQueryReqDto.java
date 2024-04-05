package com.mall4j.cloud.api.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author axin
 * @Date 2023-05-15
 **/
@Getter
@Setter
public class PageStaffQueryReqDto extends  StaffQueryDTO{
    /**
     * 当前页
     */
    @NotNull(message = "pageNum 不能为空")
    @ApiModelProperty(value = "当前页", required = true)
    private Integer pageNum;

    @NotNull(message = "pageSize 不能为空")
    @ApiModelProperty(value = "每页大小", required = true)
    private Integer pageSize;
}
